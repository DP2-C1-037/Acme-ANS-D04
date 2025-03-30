
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
public class AssignedToCreateService extends AbstractGuiService<Customer, AssignedTo> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssignedToRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		AssignedTo assignedTo;

		assignedTo = new AssignedTo();
		assignedTo.setBooking(null);
		assignedTo.setPassenger(null);

		super.getBuffer().addData(assignedTo);
	}

	@Override
	public void bind(final AssignedTo assignedTo) {
		super.bindObject(assignedTo, "booking", "passenger");
	}

	@Override
	public void validate(final AssignedTo assignedTo) {
		{
			boolean bothInDraftMode;

			bothInDraftMode = assignedTo.getBooking().isDraftMode() && assignedTo.getPassenger().isDraftMode();
			super.state(bothInDraftMode, "booking", "acme.validation.assignedTo.draftMode.message");
		}
		{
			//boolean alreadyAssigned;

			//alreadyAssigned = true;
			//super.state(alreadyAssigned, "booking", "acme.validation.assignedTo.alreadyAssigned.message");
		}
	}

	@Override
	public void perform(final AssignedTo assignedTo) {
		this.repository.save(assignedTo);
	}

	@Override
	public void unbind(final AssignedTo assignedTo) {
		SelectChoices bookingChoices;
		Collection<Booking> bookings;
		SelectChoices passengerChoices;
		Collection<Passenger> passengers;

		Dataset dataset;

		bookings = this.repository.findAllNotPublishedBookings();
		passengers = this.repository.findAllNotPublishedPassengers();

		bookingChoices = SelectChoices.from(bookings, "locatorCode", null);
		passengerChoices = SelectChoices.from(passengers, "passportNumber", null);

		dataset = super.unbindObject(assignedTo, "booking", "passenger");
		dataset.put("bookings", bookingChoices);
		dataset.put("passengers", passengerChoices);

		super.getResponse().addData(dataset);
	}
}
