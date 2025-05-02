
package acme.features.airlineManager.leg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.airline.AirlineManager;
import acme.entities.leg.Leg;

@GuiController
public class AirlineManagerLegController extends AbstractGuiController<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegListService				listService;

	@Autowired
	private AirlineManagerLegListByFlightService		listByFlightService;

	@Autowired
	private AirlineManagerLegShowService				showService;

	@Autowired
	private AirlineManagerLegCreateService				createService;

	@Autowired
	private AirlineManagerLegCreateFromFlightService	createFromFlightService;

	@Autowired
	private AirlineManagerLegUpdateService				updateService;

	@Autowired
	private AirlineManagerLegDeleteService				deleteService;

	@Autowired
	private AirlineManagerLegPublishService				publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listByFlightService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createFromFlightService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
		super.addCustomCommand("list-all-mine", "list", this.listService);
		super.addCustomCommand("create-from-all-mine", "create", this.createService);
	}

}
