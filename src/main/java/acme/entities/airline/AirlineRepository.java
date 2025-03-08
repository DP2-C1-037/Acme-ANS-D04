
package acme.entities.airline;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.client.repositories.AbstractRepository;

public interface AirlineRepository extends AbstractRepository {

	@Query("SELECT a.iataCode FROM Airline a where a.id = (SELECT l.aircraft.airline.id FROM Leg l where l.id =: legId")
	public String getIataCodeFromLegId(@Param("legId") Integer legId);
}
