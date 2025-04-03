
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Flight;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.mappings.AssignedTo;
import acme.realms.Customer;

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

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		status = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

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
		SelectChoices flightsChoices;
		Dataset dataset;

		flights = this.repository.findAllFlightsPublished();

		travelClassesChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		flightsChoices = SelectChoices.from(flights, "tag", booking.getFlight());
		// TODO: Change choices display text from tag to the origin and destiny of the flight, to be implemented when flight derived attributes are fixed

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "lastNibble", "draftMode");
		dataset.put("price", booking.getPrice());
		dataset.put("travelClasses", travelClassesChoices);
		dataset.put("travelClass", travelClassesChoices.getSelected().getKey());
		dataset.put("flights", flightsChoices);
		dataset.put("flight", flightsChoices.getSelected().getKey());
		//dataset.put("originCity", booking.getFlight().getOriginCity());
		//dataset.put("destinationCity", booking.getFlight().getDestinationCity());
		//dataset.put("scheduledDeparture", booking.getFlight().getScheduledDeparture());
		//dataset.put("scheduledArrival", booking.getFlight().getScheduledDeparture());
		//TODO: When flight custom attributes are fixed

		super.getResponse().addData(dataset);
	}

}
