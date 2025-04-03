
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
import acme.realms.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

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
		{
			boolean lastNibbleStored;

			lastNibbleStored = !(booking.getLastNibble() == null || booking.getLastNibble().isBlank());
			super.state(lastNibbleStored, "lastNibble", "acme.validation.booking.lastNibble.message");
		}
		{
			boolean atLeastAPassengerAssigned;
			Integer numberOfPassengersAssigned;

			numberOfPassengersAssigned = this.repository.findNumberOfPassengersAssignedToBookingById(booking.getId());

			atLeastAPassengerAssigned = numberOfPassengersAssigned != 0;

			super.state(atLeastAPassengerAssigned, "*", "acme.validation.booking.passengers.message");
		}

	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClassesChoices;
		Collection<Flight> flights;
		SelectChoices flightsChoices;
		Dataset dataset;

		flights = this.repository.findAllFlightsPublished();

		travelClassesChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		flightsChoices = SelectChoices.from(flights, "id", booking.getFlight());

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
