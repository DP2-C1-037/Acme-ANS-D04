
package acme.features.customer.assignedTo;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.mappings.AssignedTo;
import acme.entities.passenger.Passenger;

@Repository
public interface AssignedToRepository extends AbstractRepository {

	@Query("select at from AssignedTo at where at.id = :assignedToId")
	AssignedTo findAssignedToById(final int assignedToId);

	@Query("select at from AssignedTo at where at.booking.customer.id = :customerId")
	Collection<AssignedTo> findAssignedTosByCustomerId(final int customerId);

	@Query("select b from Booking b where b.draftMode = true")
	Collection<Booking> findAllNotPublishedBookings();

	@Query("select p from Passenger p where p.draftMode = true")
	Collection<Passenger> findAllNotPublishedPassengers();
}
