
package acme.features.airlineManager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Flight;

public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f WHERE f.airlineManager.id = :managerId")
	Collection<Flight> findAllFlightsByAirlineManagerId(int managerId);

	@Query("SELECT f FROM Flight f WHERE f.id = :flightId")
	Flight findFlightById(int flightId);
}
