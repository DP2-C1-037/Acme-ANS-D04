
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceStatus;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianMaintenanceRecordUpdateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ------------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		masterId = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
		technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnician();
		status = maintenanceRecord != null && maintenanceRecord.getDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		super.bindObject(maintenanceRecord, "maintenanceDate", "nextInspectionDueDate", "status", "estimatedCost", "notes");
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		;
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		SelectChoices statuses;
		Collection<Aircraft> aircrafts;
		SelectChoices choices;
		Dataset dataset;

		statuses = SelectChoices.from(MaintenanceStatus.class, maintenanceRecord.getStatus());

		aircrafts = this.repository.findAvailableAircrafts();
		choices = SelectChoices.from(aircrafts, "model", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "technician.identity.name", "maintenanceDate", "nextInspectionDueDate", "status", "estimatedCost", "notes", "draftMode");
		dataset.put("statuses", statuses);
		dataset.put("aircraft", choices.getSelected().getKey());
		dataset.put("aircrafts", choices);

		super.getResponse().addData(dataset);
	}
}
