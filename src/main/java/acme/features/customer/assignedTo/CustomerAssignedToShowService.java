
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
public class CustomerAssignedToShowService extends AbstractGuiService<Customer, AssignedTo> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerAssignedToRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int assignedToId;
		AssignedTo assignedTo;

		assignedToId = super.getRequest().getData("id", int.class);
		assignedTo = this.repository.findAssignedToById(assignedToId);
		status = assignedTo != null && super.getRequest().getPrincipal().hasRealm(assignedTo.getBooking().getCustomer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int assignedToId;
		AssignedTo assignedTo;

		assignedToId = super.getRequest().getData("id", int.class);
		assignedTo = this.repository.findAssignedToById(assignedToId);

		super.getBuffer().addData(assignedTo);
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

		bookings = this.repository.findAllBookingsFromCustomerId(customer.getId());
		passengers = this.repository.findAllPassengersFromCustomerId(customer.getId());

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
