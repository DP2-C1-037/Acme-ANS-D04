
package acme.features.customer.assignedTo;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.mappings.AssignedTo;

@Repository
public interface AssignedToRepository extends AbstractRepository {

	@Query("select at from AssignedTo at where at.booking.customer.id = :customerId")
	Collection<AssignedTo> findAssignedTosByCustomerId(final int customerId);
}
