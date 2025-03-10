
package acme.entities.airline;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	//	 @Query("SELECT new acme.entities.airline.FlightLegInfo(l.scheduledDeparture, l.departureAirport.city) FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC LIMIT 1")
	//	  
	//	 public FlightLegInfo flightDepartureDateAndCity(@Param("flightId") Integer flightId);
	//	  
	//	 @Query("SELECT new acme.entities.airline.FlightLegInfo(l.scheduledArrival, l.arrivalAirport.city) FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledArrival DESC LIMIT 1")
	//	 
	//	 public FlightLegInfo flightArrivalDateAndCity(@Param("flightId") Integer flightId);

	@Query("SELECT NEW acme.entities.airline.FlightLegInfo(MIN(l.scheduledDeparture), (SELECT a.city FROM Leg l2 JOIN l2.departureAirport a WHERE l2.flight.id = :flightId AND l2.scheduledDeparture = MIN(l.scheduledDeparture)), MAX(l.scheduledArrival), (SELECT a.city FROM Leg l3 JOIN l3.arrivalAirport a WHERE l3.flight.id = :flightId AND l3.scheduledArrival = MAX(l.scheduledArrival))) FROM Leg l WHERE l.flight.id = :flightId")
	FlightLegInfo flightData(@Param("flightId") Integer flightId);
}
