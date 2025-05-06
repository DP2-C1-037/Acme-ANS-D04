
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.mappings.InvolvedIn;
import acme.entities.tasks.Task;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianInvolvedInCreateService extends AbstractGuiService<Technician, InvolvedIn> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		masterId = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);
		technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnician();
		status = maintenanceRecord != null && maintenanceRecord.getDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		InvolvedIn object;
		MaintenanceRecord maintenanceRecord;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);

		object = new InvolvedIn();
		object.setMaintenanceRecord(maintenanceRecord);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final InvolvedIn involvedIn) {
		Task task;
		int taskId;

		taskId = super.getRequest().getData("task", int.class);

		super.bindObject(involvedIn);

		task = this.repository.findTaskByTaskId(taskId);
		involvedIn.setTask(task);
	}

	@Override
	public void validate(final InvolvedIn involvedIn) {
		boolean isNull;

		isNull = involvedIn == null || involvedIn.getTask() == null || involvedIn.getMaintenanceRecord() == null;

		if (!isNull) {
			boolean isNotDuplicated;
			InvolvedIn existingInvolvedIn;

			existingInvolvedIn = this.repository.findInvolvedInByMaintenanceRecordIdAndTaskId(involvedIn.getMaintenanceRecord().getId(), involvedIn.getTask().getId());
			isNotDuplicated = existingInvolvedIn == null;

			super.state(isNotDuplicated, "*", "technician.involved-in.create.duplicated");
		}
	}

	@Override
	public void perform(final InvolvedIn involvedIn) {
		this.repository.save(involvedIn);
	}

	@Override
	public void unbind(final InvolvedIn involvedIn) {
		Collection<Task> tasks;
		SelectChoices choices;
		Dataset dataset;
		int id;

		id = super.getRequest().getPrincipal().getActiveRealm().getId();

		tasks = this.repository.findAllAvailableTasks(id);
		choices = SelectChoices.from(tasks, "description", involvedIn.getTask());

		dataset = super.unbindObject(involvedIn);
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);

		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}
}
