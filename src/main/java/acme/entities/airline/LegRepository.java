
package acme.entities.airline;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("SELECT MIN(l.scheduledDeparture) FROM Leg l where l.id = :legId")
	public Date findScheduledDeparture(int legId);// devolver objeto y luego acceder a los atributos

	@Query("SELECT MAX(l.scheduledArrival) FROM Leg l where l.id = :legId")
	public Date findScheduledArrival(int legId);

	@Query("SELECT l.departureAirport.city FROM Leg l WHERE l.id = :legId AND l.scheduledDeparture = (SELECT MIN(l2.scheduledDeparture) FROM Leg l2 WHERE l2.id = :legId)") // esta condicion la cumplen dos legs, pueden darse varios casos, puede devolver una lista esta, la fecha de partida no es un buen identificador
	public String findOriginCity(int legId);

	@Query("SELECT l.arrivalAirport.city FROM Leg l WHERE l.id = :legId AND l.scheduledArrival = (SELECT MAX(l2.scheduledArrival) FROM Leg l2 WHERE l2.id = :legId)")
	public String findDestinationCity(int legId);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.id = :legId")
	public Integer legsNumberFromFlightId(int legId);

}
