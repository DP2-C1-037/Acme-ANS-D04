
package acme.features.customer.assignedTo;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.mappings.AssignedTo;
import acme.realms.Customer;

@GuiService
public class AssignedToShowService extends AbstractGuiService<Customer, AssignedTo> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssignedToRepository repository;

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
		Dataset dataset;

		dataset = super.unbindObject(assignedTo, "booking.locatorCode", "passenger.passportNumber");
		super.getResponse().addData(dataset);
	}
}
