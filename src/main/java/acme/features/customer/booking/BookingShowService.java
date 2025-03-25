
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class BookingShowService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private BookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		status = super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);

		Collection<Passenger> passengers;

		passengers = this.repository.findPassengersFromBookingById(bookingId);

		super.getBuffer().addData(booking);
		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;

		dataset = super.unbindObject(booking, "locator-code", "purcharse-moment", "travel-class", "price", "last-nibble");
		//dataset.put("origin-city", booking.getFlight().getOriginCity());
		//dataset.put("destination-city", booking.getFlight().getDestinationCity());

		//		Collection<Passenger> passengers = this.repository.findPassengersFromBookingById(booking.getId());
		//		String mappedPassengers = passengers.stream().map(p -> p.getFullName()).toString();
		//
		//		dataset.put("passengers", mappedPassengers);

		super.getResponse().addData(dataset);
	}
}
