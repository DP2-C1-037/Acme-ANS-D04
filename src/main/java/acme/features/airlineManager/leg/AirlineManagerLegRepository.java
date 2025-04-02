
package acme.features.airlineManager.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.Flight;
import acme.entities.airline.Leg;
import acme.entities.airports.Airport;

public interface AirlineManagerLegRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l WHERE l.flight.airlineManager.id = :managerId")
	Collection<Leg> findAllLegsByAirlineManagerId(int managerId);

	@Query("SELECT l FROM Leg l WHERE l.id = :legId")
	Leg findLegById(int legId);

	@Query("SELECT f FROM Flight f")
	Collection<Flight> findAllFlights();

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

}
