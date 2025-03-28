
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class PassengerShowService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private PassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int passengerId;

		passengerId = super.getRequest().getData("id", int.class);
		Collection<Booking> bookings = this.repository.findBookingsByPassengerId(passengerId);

		status = bookings.stream().allMatch(b -> super.getRequest().getPrincipal().hasRealm(b.getCustomer()));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int passengerId;
		Passenger passenger;

		passengerId = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(passengerId);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds");

		super.getResponse().addData(dataset);
	}
}
