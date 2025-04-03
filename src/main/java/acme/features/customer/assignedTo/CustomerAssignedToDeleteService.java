
package acme.features.customer.assignedTo;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.mappings.AssignedTo;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerAssignedToDeleteService extends AbstractGuiService<Customer, AssignedTo> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerAssignedToRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		AssignedTo assignedTo;
		Booking booking;

		id = super.getRequest().getData("id", int.class);
		assignedTo = this.repository.findAssignedToById(id);
		booking = assignedTo == null ? null : assignedTo.getBooking();
		status = assignedTo != null && booking != null && assignedTo.getBooking().isDraftMode() && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

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
		int passengerId;
		Passenger passenger;

		passengerId = super.getRequest().getData("passenger", int.class);
		passenger = this.repository.findPassengerById(passengerId);

		super.bindObject(assignedTo, "passenger");

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
		Dataset dataset;

		dataset = super.unbindObject(assignedTo, "passenger", "passenger.fullName", "passenger.email", "passenger.passportNumber", "passenger.birthDate", "passenger.specialNeeds");
		dataset.put("passenger", assignedTo.getPassenger().getId());
		dataset.put("draftMode", assignedTo.getBooking().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
