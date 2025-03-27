
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;

public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select a from ActivityLog a where a.flightAssignment.id = :assignmentId")
	Collection<ActivityLog> findActivityLogsByFlightAssignmentId(int assignmentId);

	@Query("select fa from FlightAssignment fa where fa.id = :assignmentId")
	FlightAssignment findAssignmentById(Integer assignmentId);
}
