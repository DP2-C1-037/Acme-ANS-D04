
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
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
		FlightCrewMember member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		Collection<ActivityLog> logs = this.repository.findLogsPublishedAndByFlightCrewMemberId(member.getId());

		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "typeOfIncident", "severityLevel", "registrationMoment");
		dataset.put("flightNumber", log.getFlightAssignment().getLeg().getFlightNumber());
		super.addPayload(dataset, log, "description");
		super.getResponse().addData(dataset);

	}

}
