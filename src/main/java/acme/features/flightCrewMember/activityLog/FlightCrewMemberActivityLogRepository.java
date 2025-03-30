
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;

public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select al from ActivityLog al where al.flightAssignment.flightCrewMember.id = :memberId")
	Collection<ActivityLog> findAllLogsByFlightCrewMemberId(int memberId);
}
