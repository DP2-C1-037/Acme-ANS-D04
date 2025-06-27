
package acme.features.administrator.assignedTo;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.mappings.AssignedTo;

@Repository
public interface AdministratorAssignedToRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :bookingId")
	Booking findBookingById(final int bookingId);

	@Query("select at from AssignedTo at where at.id = :assignedToId")
	AssignedTo findAssignedToById(final int assignedToId);

	@Query("select at from AssignedTo at where at.booking.id = :bookingId")
	Collection<AssignedTo> findAssignedTosByBookingId(final int bookingId);

}
