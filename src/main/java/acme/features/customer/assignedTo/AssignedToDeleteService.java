
package acme.features.customer.assignedTo;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.mappings.AssignedTo;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class AssignedToDeleteService extends AbstractGuiService<Customer, AssignedTo> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssignedToRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		AssignedTo assignedTo;

		id = super.getRequest().getData("id", int.class);
		assignedTo = this.repository.findAssignedToById(id);
		status = assignedTo != null && super.getRequest().getPrincipal().hasRealm(assignedTo.getBooking().getCustomer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AssignedTo assignedTo;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignedTo = this.repository.findAssignedToById(id);

		super.getBuffer().addData(assignedTo);
	}

	@Override
	public void bind(final AssignedTo assignedTo) {
		int bookingId;
		int passengerId;
		Booking booking;
		Passenger passenger;

		bookingId = super.getRequest().getData("booking", int.class);
		passengerId = super.getRequest().getData("passenger", int.class);
		booking = this.repository.findBookingById(bookingId);
		passenger = this.repository.findPassengerById(passengerId);

		super.bindObject(assignedTo, "booking", "passenger");

		assignedTo.setBooking(booking);
		assignedTo.setPassenger(passenger);
	}

	@Override
	public void validate(final AssignedTo assignedTo) {
		;
	}

	@Override
	public void perform(final AssignedTo assignedTo) {
		this.repository.delete(assignedTo);
	}

	@Override
	public void unbind(final AssignedTo assignedTo) {
		SelectChoices bookingChoices;
		Collection<Booking> bookings;
		SelectChoices passengerChoices;
		Collection<Passenger> passengers;
		Customer customer;

		Dataset dataset;

		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		bookings = this.repository.findAllNotPublishedBookingsFromCustomerId(customer.getId());
		passengers = this.repository.findAllNotPublishedPassengersFromCustomerId(customer.getId());

		bookingChoices = SelectChoices.from(bookings, "locatorCode", assignedTo.getBooking());
		passengerChoices = SelectChoices.from(passengers, "passportNumber", assignedTo.getPassenger());

		dataset = super.unbindObject(assignedTo, "booking", "passenger");
		dataset.put("booking", bookingChoices.getSelected().getKey());
		dataset.put("bookings", bookingChoices);
		dataset.put("passenger", passengerChoices.getSelected().getKey());
		dataset.put("passengers", passengerChoices);

		super.getResponse().addData(dataset);
	}

}
