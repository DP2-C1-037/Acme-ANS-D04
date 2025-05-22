
package acme.features.airlineManager.leg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.LegStatus;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.AirlineManager;
import acme.entities.airports.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@GuiService
public class AirlineManagerLegCreateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractGuiService interfaced -------------------------------------------


	@Override
	public void authorise() {
		boolean validFlight = true;
		boolean validDepartureAirport = true;
		boolean validArrivalAirport = true;
		boolean validAircraft = true;
		boolean managerOwnsLeg = true;
		boolean idNotTampered = true;

		if (super.getRequest().hasData("id", int.class))
			idNotTampered = super.getRequest().getData("id", int.class) == 0;

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Flight flightFromForm = null;
		if (super.getRequest().hasData("flight", int.class)) {
			int flightId = super.getRequest().getData("flight", int.class);
			if (flightId != 0)
				flightFromForm = this.repository.findFlightById(flightId);
			validFlight = flightFromForm != null;
		}
		AirlineManager manager = flightFromForm == null ? null : flightFromForm.getAirlineManager();
		if (manager != null)
			managerOwnsLeg = managerId == manager.getId();

		if (super.getRequest().hasData("departureAirport", int.class)) {
			int departureAirportId = super.getRequest().getData("departureAirport", int.class);
			if (departureAirportId != 0)
				validDepartureAirport = this.repository.findAirportById(departureAirportId) != null;
		}

		if (super.getRequest().hasData("arrivalAirport", int.class)) {
			int arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
			if (arrivalAirportId != 0)
				validArrivalAirport = this.repository.findAirportById(arrivalAirportId) != null;
		}

		if (super.getRequest().hasData("aircraft", int.class)) {
			int aircraftId = super.getRequest().getData("aircraft", int.class);
			if (aircraftId != 0)
				validAircraft = this.repository.findAircraftById(aircraftId) != null;
		}

		boolean status = idNotTampered && managerOwnsLeg && validFlight && validDepartureAirport && validArrivalAirport && validAircraft;

		super.getResponse().setAuthorised(status);
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
		boolean scheduledDepartureIsFuture = false;
		try {
			scheduledDepartureIsFuture = leg.getScheduledDeparture().after(new SimpleDateFormat("yyyy/MM/dd HH:mm").parse("2025/01/01 00:00"));
		} catch (ParseException e) {
		}
		super.state(scheduledDepartureIsFuture, "scheduledDeparture", "acme.validation.leg.scheduledDepartureIsFuture.message");

		boolean scheduledArrivalIsFuture = false;
		try {
			scheduledArrivalIsFuture = leg.getScheduledArrival().after(new SimpleDateFormat("yyyy/MM/dd HH:mm").parse("2025/01/01 00:01"));
		} catch (ParseException e) {
		}
		super.state(scheduledArrivalIsFuture, "scheduledArrival", "acme.validation.leg.scheduledArrivalIsFuture.message");
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {

		Dataset dataset;
		int airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Flight> flights = this.repository.findAllFlightsByAirlineManagerId(airlineManagerId);
		Collection<Airport> airports = this.repository.findAllAirports();
		Collection<Aircraft> aircrafts = this.repository.findAllAircrafts();
		SelectChoices choicesFlight = SelectChoices.from(flights, "tag", leg.getFlight());
		SelectChoices choicesArrivalAirports = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		SelectChoices choicesDepartureAirports = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		SelectChoices choicesAircraft = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		SelectChoices choicesStatus = SelectChoices.from(LegStatus.class, leg.getStatus());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode", "flight", "arrivalAirport", "departureAirport", "aircraft");
		dataset.put("flight", choicesFlight.getSelected().getKey());
		dataset.put("flights", choicesFlight);
		dataset.put("arrivalAirport", choicesArrivalAirports.getSelected().getKey());
		dataset.put("arrivalAirports", choicesArrivalAirports);
		dataset.put("departureAirport", choicesDepartureAirports.getSelected().getKey());
		dataset.put("departureAirports", choicesDepartureAirports);
		dataset.put("aircraft", choicesAircraft.getSelected().getKey());
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("status", choicesStatus.getSelected().getKey());
		dataset.put("statuses", choicesStatus);
		super.addPayload(dataset, leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode", "flight", "arrivalAirport", "departureAirport", "aircraft");

		super.getResponse().addData(dataset);
	}
}
