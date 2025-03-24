
package acme.features.customer.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@GuiController
public class BookingController extends AbstractGuiController<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private BookingListService listService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}

}
