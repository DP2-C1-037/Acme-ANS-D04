
package acme.entities.leg;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("SELECT MIN(l.scheduledDeparture) FROM Leg l where l.flight.id = :flightId")
	public Date findScheduledDeparture(int flightId);

	@Query("SELECT MAX(l.scheduledArrival) FROM Leg l where l.flight.id = :flightId")
	public Date findScheduledArrival(int flightId);

	@Query("SELECT l.departureAirport.city FROM Leg l WHERE l.flight.id = :flightId AND l.scheduledDeparture = (SELECT MIN(l2.scheduledDeparture) FROM Leg l2 WHERE l2.flight.id = :flightId)")
	public String findOriginCity(int flightId);

	@Query("SELECT l.arrivalAirport.city FROM Leg l WHERE l.flight.id = :flightId AND l.scheduledArrival = (SELECT MAX(l2.scheduledArrival) FROM Leg l2 WHERE l2.flight.id = :flightId)")
	public String findDestinationCity(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId")
	public Collection<Leg> findLegsFromFlightId(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flightNumber = :flightNumber")
	public Leg findLegByFlightNumber(String flightNumber);

	@Query("SELECT a.airline.iataCode FROM Aircraft a WHERE a.id = :aircrafId")
	public String getIataCodeFromAircraftId(int aircrafId);

	@Query("SELECT l FROM Leg l WHERE l.scheduledDeparture = :scheduledDeparture AND l.flight.id = :flightId AND l.id <> :legId")
	public Leg findLegByFlightByScheduledDeparture(int flightId, int legId, Date scheduledDeparture);

	@Query("SELECT l FROM Leg l WHERE l.scheduledArrival = :scheduledArrival AND l.flight.id = :flightId AND l.id <> :legId")
	public Leg findLegByFlightByScheduledArrival(int flightId, int legId, Date scheduledArrival);

}
