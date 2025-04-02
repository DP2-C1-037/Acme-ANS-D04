
package acme.features.airlineManager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.AirlineManager;
import acme.entities.airline.Leg;

@GuiService
public class AirlineManagerLegListService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	// ORDERED BY DATE
	@Override
	public void load() {
		AirlineManager manager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		Collection<Leg> legs = this.repository.findAllLegsByAirlineManagerId(manager.getId());

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "status", "scheduledDeparture", "arrivalAirport");
		super.addPayload(dataset, leg, "flight");
		super.addPayload(dataset, leg, "departureAirport");
		super.addPayload(dataset, leg, "scheduledArrival");
		super.getResponse().addData(dataset);

	}

}
