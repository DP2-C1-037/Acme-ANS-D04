
package acme.features.customer.booking;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.entities.mappings.AssignedTo;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;

		status = !super.getRequest().getMethod().equals("GET");

		if (status) {

			bookingId = super.getRequest().getData("id", int.class);
			booking = this.repository.findBookingById(bookingId);
			status = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "travelClass", "lastNibble", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
		Collection<AssignedTo> bookingAssignedPassengers;
		bookingAssignedPassengers = this.repository.findAllAssignedToByBookingId(booking.getId());

		this.repository.deleteAll(bookingAssignedPassengers);
		this.repository.delete(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClassesChoices;
		Collection<Flight> flights;
		List<Flight> flightsInFuture;
		SelectChoices flightsChoices;
		Dataset dataset;

		flights = this.repository.findAllFlightsPublished();
		flightsInFuture = flights.stream().filter(f -> MomentHelper.isFuture(f.getScheduledDeparture())).collect(Collectors.toList());

		travelClassesChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		flightsChoices = SelectChoices.from(flightsInFuture, "originDestinationTag", booking.getFlight());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "lastNibble", "draftMode");
		dataset.put("price", booking.getPrice());
		dataset.put("travelClasses", travelClassesChoices);
		dataset.put("travelClass", travelClassesChoices.getSelected().getKey());
		dataset.put("flights", flightsChoices);
		dataset.put("flight", flightsChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
