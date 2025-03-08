
package acme.entities.airline;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	/*
	 * @Query("SELECT new acme.entities.airline.FlightLegInfo(l.scheduledDeparture, l.departureAirport.city) FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC LIMIT 1")
	 * 
	 * public FlightLegInfo flightDepartureDateAndCity(@Param("flightId") Integer flightId);
	 * 
	 * @Query("SELECT new acme.entities.airline.FlightLegInfo(l.scheduledArrival, l.arrivalAirport.city) FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledArrival DESC LIMIT 1")
	 * 
	 * public FlightLegInfo flightArrivalDateAndCity(@Param("flightId") Integer flightId);
	 */

	@Query("SELECT new acme.entities.airline.FlightLegInfo(firstLeg.scheduledDeparture, firstLeg.departureAirport.city, lastLeg.scheduledArrival, lastLeg.arrivalAirport.city) FROM Leg firstLeg, Leg lastLeg WHERE firstLeg.flight.id = :flightId AND lastLeg.flight.id = :flightId AND firstLeg.scheduledDeparture = (SELECT MIN(l.scheduledDeparture) FROM Leg l WHERE l.flight.id = :flightId) AND lastLeg.scheduledArrival = (SELECT MAX(l.scheduledArrival) FROM Leg l WHERE l.flight.id = :flightId)")
	public FlightLegInfo flightData(@Param("flightId") Integer flightId);

}
