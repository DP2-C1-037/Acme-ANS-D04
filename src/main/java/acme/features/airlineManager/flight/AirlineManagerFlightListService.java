
package acme.features.airlineManager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.AirlineManager;
import acme.entities.airline.Flight;

@GuiService
public class AirlineManagerFlightListService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		AirlineManager manager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		Collection<Flight> flights = this.repository.findAllFlightsByAirlineManagerId(manager.getId());

		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost");
		super.addPayload(dataset, flight, "description");
		super.getResponse().addData(dataset);

	}

}
