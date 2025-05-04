
package acme.entities.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

public interface FlightRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id = :flightId")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("select l from Leg l where l.flight.id = :flightId and l.draftMode = false")
	Collection<Leg> findPublishedLegsByFlightId(int flightId);
}
