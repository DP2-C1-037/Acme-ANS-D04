
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
public class AirlineManagerLegCreateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractGuiService interfaced -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Leg leg = new Leg();
		leg.setDraftMode(true);
		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "aircraft", "flight");
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		assert leg != null;

		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {

		Dataset dataset;
		int airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Flight> flightsList = this.repository.findFlightsByAirlineManagerId(airlineManagerId);
		Collection<Airport> airportsList = this.repository.findAllAirports();
		Collection<Aircraft> aircraftsList = this.repository.findAllAircrafts();
		SelectChoices flights = SelectChoices.from(flightsList, "tag", leg.getFlight());
		SelectChoices arrivalAiports = SelectChoices.from(airportsList, "iataCode", leg.getArrivalAirport());
		SelectChoices departureAirports = SelectChoices.from(airportsList, "iataCode", leg.getDepartureAirport());
		SelectChoices aircrafts = SelectChoices.from(aircraftsList, "registrationNumber", leg.getAircraft());
		SelectChoices status = SelectChoices.from(LegStatus.class, leg.getStatus());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode", "flight", "arrivalAirport", "departureAirport", "aircraft");
		dataset.put("flight", flights.getSelected().getKey());
		dataset.put("flights", flights);
		dataset.put("arrivalAirport", arrivalAiports.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalAiports);
		dataset.put("departureAirport", departureAirports.getSelected().getKey());
		dataset.put("departureAirports", departureAirports);
		dataset.put("aircraft", aircrafts.getSelected().getKey());
		dataset.put("aircrafts", aircrafts);
		dataset.put("status", status.getSelected().getKey());
		dataset.put("statuses", status);
		super.addPayload(dataset, leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode", "flight", "arrivalAirport", "departureAirport", "aircraft");

		super.getResponse().addData(dataset);
	}
}
