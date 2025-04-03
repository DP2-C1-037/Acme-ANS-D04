
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.mappings.InvolvedIn;
import acme.entities.tasks.Task;
import acme.realms.technicians.Technician;

@GuiService
public class TechnicianInvolvedInDeleteService extends AbstractGuiService<Technician, InvolvedIn> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Technician technician;
		InvolvedIn involvedIn;

		id = super.getRequest().getData("id", int.class);
		involvedIn = this.repository.findInvolvedInById(id);
		technician = involvedIn == null ? null : involvedIn.getMaintenanceRecord().getTechnician();
		status = involvedIn != null && involvedIn.getMaintenanceRecord().getDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		InvolvedIn involvedIn;
		int id;

		id = super.getRequest().getData("id", int.class);
		involvedIn = this.repository.findInvolvedInById(id);

		super.getBuffer().addData(involvedIn);
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
		;
	}

	@Override
	public void perform(final InvolvedIn involvedIn) {
		this.repository.delete(involvedIn);
	}

	@Override
	public void unbind(final InvolvedIn involvedIn) {
		Collection<Task> tasks;
		SelectChoices choices;
		Dataset dataset;

		tasks = this.repository.findAllAvailableTasks();
		choices = SelectChoices.from(tasks, "description", involvedIn.getTask());

		dataset = super.unbindObject(involvedIn);
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);

		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("draftMode", involvedIn.getMaintenanceRecord().getDraftMode());

		super.getResponse().addData(dataset);
	}
}
