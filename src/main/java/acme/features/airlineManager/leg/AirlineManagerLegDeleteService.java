
package acme.features.airlineManager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.LegStatus;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.AirlineManager;
import acme.entities.airline.Flight;
import acme.entities.airline.Leg;
import acme.entities.airports.Airport;

@GuiService
public class AirlineManagerLegDeleteService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;


	@Override
	public void authorise() {

		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		AirlineManager manager = leg == null ? null : leg.getFlight().getAirlineManager();
		boolean status = manager != null && super.getRequest().getPrincipal().hasRealm(manager) && leg.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		Collection<Flight> flightsList = this.repository.findAllFlights();
		Collection<Aircraft> aircraftsList = this.repository.findAllAircrafts();
		Collection<Airport> airportsList = this.repository.findAllAirports();

		SelectChoices status = SelectChoices.from(LegStatus.class, leg.getStatus());
		SelectChoices flights = SelectChoices.from(flightsList, "tag", leg.getFlight());
		SelectChoices aircrafts = SelectChoices.from(aircraftsList, "registrationNumber", leg.getDeployedAircraft());
		SelectChoices airports = SelectChoices.from(airportsList, "name", leg.getDepartureAirport());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
		dataset.put("confirmation", false);
		dataset.put("status", status);
		dataset.put("flights", flights);
		dataset.put("aircrafts", aircrafts);
		dataset.put("airports", airports);
		dataset.put("duration", leg.getDuration());
		super.getResponse().addData(dataset);
	}

}
