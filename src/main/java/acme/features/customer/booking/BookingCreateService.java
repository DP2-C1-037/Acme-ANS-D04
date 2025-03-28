
package acme.features.customer.booking;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Flight;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.realms.Customer;

@GuiService
public class BookingCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private BookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Date moment;
		Customer customer;
		Booking booking;

		moment = MomentHelper.getCurrentMoment();
		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		booking = new Booking();
		booking.setLocatorCode("");
		booking.setPurcharseMoment(moment);
		booking.setTravelClass(TravelClass.ECONOMY);
		booking.setLastNibble(null);
		booking.setPrice(null);
		booking.setCustomer(customer);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "travelClass", "lastNibble", "flight", "price");
	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
		Flight selectedFlight;
		selectedFlight = booking.getFlight();

		booking.setPrice(selectedFlight.getCost());
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices travelClassesChoices;
		List<Flight> flights;
		SelectChoices flightsChoices;
		Dataset dataset;

		flights = this.repository.findAllFlights().stream().collect(Collectors.toList());

		travelClassesChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		flightsChoices = SelectChoices.from(flights, "id", flights.get(0));

		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "lastNibble", "flight", "price");
		dataset.put("travelClasses", travelClassesChoices);
		dataset.put("flights", flightsChoices);

		super.getResponse().addData(dataset);
	}

}
