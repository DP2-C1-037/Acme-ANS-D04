
package acme.entities.airline;

import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirlineRepository extends AbstractRepository {

	@Query("SELECT a.iataCode FROM Airline a WHERE a.id = (SELECT l.deployedAircraft.airline.id FROM Leg l WHERE l.id = :legId)")
	public String getIataCodeFromLegId(int legId);
}
