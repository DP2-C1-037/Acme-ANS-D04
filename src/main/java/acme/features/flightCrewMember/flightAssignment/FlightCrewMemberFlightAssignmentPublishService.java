
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AssignmentStatus;
import acme.datatypes.AvailabilityStatus;
import acme.datatypes.FlightCrewDuty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean validLeg;
		int masterId;
		FlightAssignment assignment;
		FlightCrewMember member;

		masterId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = assignment != null && assignment.isDraftMode() && super.getRequest().getPrincipal().hasRealm(member);

		if (super.getRequest().getMethod().equals("POST")) {
			int legId = super.getRequest().getData("leg", int.class);
			Leg leg = this.repository.findLegById(legId);

			validLeg = legId == 0 || leg != null;
			if (validLeg && leg != null) {
				boolean isSameAsAssigned = assignment != null && assignment.getLeg() != null && leg.getId() == assignment.getLeg().getId();
				boolean isFuture = MomentHelper.isBefore(MomentHelper.getCurrentMoment(), leg.getScheduledArrival());
				boolean isMyAirline = member != null && leg.getAircraft().getAirline().getId() == member.getAirline().getId();
				validLeg = !leg.isDraftMode() && isMyAirline && (isFuture || isSameAsAssigned);
			}
			status = status && validLeg;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		Integer legId;
		Leg leg;
		FlightCrewMember member;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		super.bindObject(assignment, "flightCrewDuty", "status", "remarks");
		assignment.setLeg(leg);
		assignment.setFlightCrewMember(member);
		assignment.setLastUpdateMoment(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		if (assignment.getLeg() != null && assignment.getFlightCrewDuty() != null) {
			this.validateLegCompatibility(assignment);
			this.validateDutyAssignment(assignment);
			this.validateStatusAvailability(assignment.getFlightCrewMember());
			this.validateLegHasNotOccurred(assignment.getLeg());
		}
	}

	private void validateLegHasNotOccurred(final Leg leg) {
		Date scheduledArrival = leg.getScheduledArrival();
		Date now = MomentHelper.getCurrentMoment();
		boolean hasOccurred = MomentHelper.isAfter(now, scheduledArrival);
		if (hasOccurred)
			super.state(false, "*", "acme.validation.flight-assignment.leg-has-occurred.message");

	}

	private void validateStatusAvailability(final FlightCrewMember member) {
		AvailabilityStatus status = member.getAvailabilityStatus();
		AvailabilityStatus requiredStatus = AvailabilityStatus.AVAILABLE;
		boolean available = status.equals(requiredStatus);
		if (!available)
			super.state(false, "*", "acme.validation.flight-assignment.member-not-available.message");
	}

	private void validateLegCompatibility(final FlightAssignment assignment) {
		int memberId = assignment.getFlightCrewMember().getId();
		int assignmentId = assignment.getId();

		Collection<Leg> existingLegs = this.repository.findLegsByFlightCrewMemberIdExcludingAssignment(memberId, assignmentId);

		boolean hasIncompatibleLeg = existingLegs.stream().anyMatch(existingLeg -> this.legIsNotOverlapping(assignment.getLeg(), existingLeg));

		if (hasIncompatibleLeg)
			super.state(false, "*", "acme.validation.flight-assignment.member-with-overlapping-legs.message");
	}

	private void validateDutyAssignment(final FlightAssignment flightAssignment) {
		Collection<FlightAssignment> assignedDuties = this.repository.findFlightAssignmentByLegId(flightAssignment.getLeg().getId());

		boolean legWithCopilot = assignedDuties.stream().anyMatch(assignment -> assignment.getFlightCrewDuty().equals(FlightCrewDuty.COPILOT));
		boolean legWithPilot = assignedDuties.stream().anyMatch(assignment -> assignment.getFlightCrewDuty().equals(FlightCrewDuty.PILOT));

		super.state(!(flightAssignment.getFlightCrewDuty().equals(FlightCrewDuty.PILOT) && legWithPilot), "*", "acme.validation.flight-assignment.leg-has-pilot.message");
		super.state(!(flightAssignment.getFlightCrewDuty().equals(FlightCrewDuty.COPILOT) && legWithCopilot), "*", "acme.validation.flight-assignment.leg-has-copilot.message");
	}

	private boolean legIsNotOverlapping(final Leg newLeg, final Leg existingLeg) {
		boolean isDepartureOverlapping = MomentHelper.isInRange(newLeg.getScheduledDeparture(), existingLeg.getScheduledDeparture(), existingLeg.getScheduledArrival());
		boolean isArrivalOverlapping = MomentHelper.isInRange(newLeg.getScheduledArrival(), existingLeg.getScheduledDeparture(), existingLeg.getScheduledArrival());
		return isDepartureOverlapping && isArrivalOverlapping;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices statuses;
		SelectChoices duties;
		Collection<Leg> legs;
		SelectChoices selectedLegs;
		String employeeCode;
		FlightCrewMember member;

		member = assignment.getFlightCrewMember();

		legs = this.repository.findPublishedFutureOwnedLegs(MomentHelper.getCurrentMoment(), member.getAirline());
		Leg currentLeg = assignment.getLeg();
		if (currentLeg != null && !legs.contains(currentLeg))
			legs.add(currentLeg);
		statuses = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		duties = SelectChoices.from(FlightCrewDuty.class, assignment.getFlightCrewDuty());
		selectedLegs = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		employeeCode = assignment.getFlightCrewMember().getEmployeeCode();
		dataset = super.unbindObject(assignment, "flightCrewDuty", "lastUpdateMoment", "status", "remarks", "draftMode");
		dataset.put("employeeCode", employeeCode);
		dataset.put("statuses", statuses);
		dataset.put("duties", duties);
		dataset.put("leg", selectedLegs.getSelected().getKey());
		dataset.put("legs", selectedLegs);

		super.getResponse().addData(dataset);
	}
}
