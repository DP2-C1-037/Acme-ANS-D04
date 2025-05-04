
package acme.features.airlineManager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.FlightSelfTransfer;
import acme.entities.airline.AirlineManager;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@GuiService
public class AirlineManagerFlightShowService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {

		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(flightId);
		AirlineManager manager = flight == null ? null : flight.getAirlineManager();
		boolean status = manager != null && super.getRequest().getPrincipal().getActiveRealm().getId() == manager.getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int flightId = super.getRequest().getData("id", int.class);
		Flight flight = this.repository.findFlightById(flightId);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		Collection<Leg> legs = this.repository.findLegsByFlightId(flight.getId());
		SelectChoices selfTransfer = SelectChoices.from(FlightSelfTransfer.class, flight.getRequiresSelfTransfer());

		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");
		dataset.put("confirmation", false);
		dataset.put("selfTransfer", selfTransfer);

		if (!legs.isEmpty()) {
			dataset.put("originCity", flight.getOriginCity());
			dataset.put("destinationCity", flight.getDestinationCity());
			dataset.put("scheduledDeparture", flight.getScheduledDeparture());
			dataset.put("scheduledArrival", flight.getScheduledArrival());
			dataset.put("layovers", flight.getLayoversNumber());
		} else {
			dataset.put("originCity", null);
			dataset.put("destinationCity", null);
			dataset.put("scheduledDeparture", null);
			dataset.put("scheduledArrival", null);
			dataset.put("layovers", null);

			super.getResponse().addData(dataset);
		}
	}

}
