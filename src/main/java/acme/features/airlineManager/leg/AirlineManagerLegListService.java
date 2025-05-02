
package acme.features.airlineManager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.AirlineManager;
import acme.entities.leg.Leg;

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

		super.getResponse().addGlobal("masterId", null); // Necesario para poder tener 2 create diferentes en función de masterId

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Leg> legs = this.repository.findAllLegsByAirlineManagerId(managerId);

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "status", "scheduledDeparture");
		super.addPayload(dataset, leg);
		super.getResponse().addGlobal("masterId", null);
		super.getResponse().addData(dataset);

	}

}
