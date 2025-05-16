
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

	@Query("SELECT l.departureAirport.iataCode FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	Collection<String> findDepartureAirport(int flightId);

	@Query("SELECT l.arrivalAirport.iataCode FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledArrival DESC")
	Collection<String> findDestinationAirport(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId")
	public Collection<Leg> findLegsFromFlightId(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flightNumber = :flightNumber")
	public Leg findLegByFlightNumber(String flightNumber);

	@Query("SELECT a.airline.iataCode FROM Aircraft a WHERE a.id = :aircrafId")
	public String getIataCodeFromAircraftId(int aircrafId);

	@Query("select l from Leg l where l.flight.id = :flightId")
	Collection<Leg> findLegsByFlightId(int flightId);

	@Query("select l from Leg l where l.flight.id = :flightId and l.draftMode = false")
	Collection<Leg> findPublishedLegsByFlightId(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId AND l.id <> :legId AND (:scheduledDeparture BETWEEN l.scheduledDeparture AND l.scheduledArrival OR l.scheduledDeparture BETWEEN :scheduledDeparture AND :scheduledArrival)")
	Collection<Leg> findOverlappingLegs(int flightId, int legId, Date scheduledDeparture, Date scheduledArrival);

	@Query("select l from Leg l where l.aircraft.id = :aircraftId and l.id <> :legId and (:scheduledDeparture BETWEEN l.scheduledDeparture AND l.scheduledArrival OR l.scheduledDeparture BETWEEN :scheduledDeparture AND :scheduledArrival)")
	Collection<Leg> findLegByAircraftIdSameTime(int aircraftId, int legId, Date scheduledDeparture, Date scheduledArrival);

	@Query("select l from Leg l where l.departureAirport.id = :airportId and l.id <> :legId and l.scheduledDeparture = :scheduledDeparture")
	Collection<Leg> findLegByAirportIdSameDeparture(int airportId, int legId, Date scheduledDeparture);

	@Query("select l from Leg l where l.arrivalAirport.id = :airportId and l.id <> :legId and l.scheduledArrival = :scheduledArrival")
	Collection<Leg> findLegByAirportIdSameArrival(int airportId, int legId, Date scheduledArrival);

}
