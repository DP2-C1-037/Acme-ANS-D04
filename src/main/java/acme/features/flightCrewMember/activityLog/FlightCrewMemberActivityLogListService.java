
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int masterId;
		Collection<ActivityLog> logs;

		masterId = super.getRequest().getData("masterId", int.class);
		logs = this.repository.findActivityLogsByMasterId(masterId);

		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		int masterId;
		FlightAssignment assignment;
		boolean showingCreate;
		boolean correctFlightCrewMember;
		int memberId;
		int userId;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		userId = super.getRequest().getPrincipal().getActiveRealm().getId();
		memberId = assignment.getFlightCrewMember().getId();

		correctFlightCrewMember = memberId == userId;
		showingCreate = !assignment.isDraftMode() && correctFlightCrewMember;

		dataset = super.unbindObject(log, "typeOfIncident", "severityLevel", "registrationMoment");
		dataset.put("flightNumber", log.getFlightAssignment().getLeg().getFlightNumber());

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showingCreate", showingCreate);
		super.addPayload(dataset, log, "description");
		super.getResponse().addData(dataset);

	}

}
