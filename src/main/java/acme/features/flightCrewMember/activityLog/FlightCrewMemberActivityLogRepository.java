
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;

public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select al from ActivityLog al where al.flightAssignment.flightCrewMember.id = :memberId")
	Collection<ActivityLog> findAllLogsByFlightCrewMemberId(int memberId);

	@Query("select al from ActivityLog al where al.id = :logId")
	ActivityLog findActivityLogById(int logId);

	@Query("select fa from FlightAssignment fa")
	Collection<FlightAssignment> findAllAssignments();

	@Query("select fa from FlightAssignment fa where fa.id = :assignmentId and fa.draftMode = false")
	FlightAssignment findPublishedFlightAssignmentById(Integer assignmentId);
}
