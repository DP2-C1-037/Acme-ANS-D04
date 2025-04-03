
package acme.features.airlineManager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.FlightSelfTransfer;
import acme.entities.airline.AirlineManager;
import acme.entities.airline.Flight;

@GuiService
public class AirlineManagerFlightDeleteService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {

		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		AirlineManager manager = flight == null ? null : flight.getAirlineManager();
		boolean status = manager != null && super.getRequest().getPrincipal().hasRealm(manager) && flight.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(flightId);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {

		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		SelectChoices selfTransfer = SelectChoices.from(FlightSelfTransfer.class, flight.getRequiresSelfTransfer());

		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
		dataset.put("confirmation", false);
		dataset.put("selfTransfer", selfTransfer);
		super.getResponse().addData(dataset);
	}

}
