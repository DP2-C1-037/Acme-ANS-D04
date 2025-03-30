
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Leg;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa")
	Collection<FlightAssignment> findAllFlightAssignments();

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select fa from FlightAssignment fa where fa.leg.scheduledArrival < :now and fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findCompletedFlightAssignmentsByMemberId(Date now, int id);

	@Query("select fa from FlightAssignment fa where fa.leg.scheduledDeparture > :now and fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findPlannedFlightAssignmentsByMemberId(Date now, int id);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);

	@Query("select m from FlightCrewMember m")
	Collection<FlightCrewMember> findAllFlightCrewMembers();

	@Query("select m from FlightCrewMember m where m.id = :memberId")
	FlightCrewMember findFlightCrewMemberById(int memberId);
}
