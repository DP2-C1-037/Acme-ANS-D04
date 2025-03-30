
package acme.features.technician.maintenanceRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.realms.technicians.Technician;

@GuiController
public class TechnicianMaintenanceRecordController extends AbstractGuiController<Technician, MaintenanceRecord> {

	// Internal state ------------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordListService		listService;

	@Autowired
	private TechnicianMaintenanceRecordListMineService	listMineService;

	@Autowired
	private TechnicianMaintenanceRecordShowService		showService;

	// Constructors --------------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);

		super.addCustomCommand("list-mine", "list", this.listMineService);
	}
}
