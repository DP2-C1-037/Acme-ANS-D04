
package acme.features.airlineManager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.AirlineManager;
import acme.entities.airline.Flight;
import acme.entities.airline.Leg;

public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f WHERE f.airlineManager.id = :managerId")
	Collection<Flight> findFlightsByAirlineManagerId(int managerId);

	@Query("SELECT f FROM Flight f WHERE f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("SELECT am FROM AirlineManager am")
	Collection<AirlineManager> findAllManagers();

	@Query("select l from Leg l where l.flight.id = :flightId")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("select l from Leg l where l.flight.id = :flightId and l.draftMode = false")
	Collection<Leg> findPublishedLegsByFlightId(int flightId);
}
