
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.airline.Leg;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId")
	Collection<FlightAssignment> findFlightAssignmentsByMemberId(int memberId);

	@Query("select al from ActivityLog al where al.flightAssignment.flightCrewMember.id = :memberId or al.draftMode = false")
	Collection<ActivityLog> findLogsPublishedAndByFlightCrewMemberId(int memberId);

	@Query("select al from ActivityLog al where al.id = :logId")
	ActivityLog findActivityLogById(int logId);

	@Query("select fa from FlightAssignment fa where fa.flightCrewMember.id = :memberId or fa.draftMode = false")
	Collection<FlightAssignment> findFlightAssignmentsByMemberIdAndPublished(int memberId);

	@Query("select fa from FlightAssignment fa where fa.id = :assignmentId")
	FlightAssignment findFlightAssignmentById(Integer assignmentId);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select lo from ActivityLog lo where lo.flightAssignment.id = :masterId")
	Collection<ActivityLog> findActivityLogsByMasterId(int masterId);
}
