
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

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
		boolean status;
		int assignmentId;
		FlightAssignment assignment;

		assignmentId = super.getRequest().getData("flightAssignmentId", int.class);
		assignment = this.repository.findAssignmentById(assignmentId);

		// status = !assignment.isDraftMode();

		//super.getResponse().setAuthorised(status);
	}

}
