
package acme.features.customer.assignedTo;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.mappings.AssignedTo;
import acme.realms.Customer;

@GuiService
public class CustomerAssignedToListService extends AbstractGuiService<Customer, AssignedTo> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerAssignedToRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<AssignedTo> assignedTos;
		int customerId;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assignedTos = this.repository.findAssignedTosByCustomerId(customerId);

		super.getBuffer().addData(assignedTos);
	}

	@Override
	public void unbind(final AssignedTo assignedTo) {
		Dataset dataset;

		dataset = super.unbindObject(assignedTo, "booking.locatorCode", "passenger.passportNumber");
		super.getResponse().addData(dataset);
	}
}
