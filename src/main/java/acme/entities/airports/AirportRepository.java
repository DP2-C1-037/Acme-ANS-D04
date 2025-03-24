
package acme.entities.airports;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirportRepository extends AbstractRepository {

	@Query("select a from Airport a where a.code = :code")
	Airport findAirportByCode(String code);
}
