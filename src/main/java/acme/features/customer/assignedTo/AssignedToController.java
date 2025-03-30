
package acme.features.customer.assignedTo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.mappings.AssignedTo;
import acme.realms.Customer;

@GuiController
public class AssignedToController extends AbstractGuiController<Customer, AssignedTo> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssignedToListService	listService;

	@Autowired
	private AssignedToShowService	showService;

	@Autowired
	private AssignedToCreateService	createService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);

	}

}
