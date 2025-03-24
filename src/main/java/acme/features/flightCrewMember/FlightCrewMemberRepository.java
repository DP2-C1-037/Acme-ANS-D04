
package acme.features.flightCrewMember;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.FlightCrewMember;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("select m from FlightCrewMember m where m.employeeCode = :employeeCode")
	FlightCrewMember findFlightCrewMemberByEmployeeCode(String employeeCode);

}
