
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
public class AirlineManagerLegUpdateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;


	@Override
	public void authorise() {

		boolean validFlight = true;
		boolean validDepartureAirport = true;
		boolean validArrivalAirport = true;
		boolean validAircraft = true;
		boolean managerOwnsLeg = true;

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		boolean validLeg = leg != null && leg.isDraftMode();

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

		boolean status = validLeg && managerOwnsLeg && validFlight && validDepartureAirport && validArrivalAirport && validAircraft;

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
		Collection<Flight> flightsList = this.repository.findAllFlightsByAirlineManagerId(airlineManagerId);
		Collection<Aircraft> aircraftsList = this.repository.findAllAircrafts();
		Collection<Airport> airportsList = this.repository.findAllAirports();

		SelectChoices status = SelectChoices.from(LegStatus.class, leg.getStatus());
		SelectChoices flights = SelectChoices.from(flightsList, "tag", leg.getFlight());
		SelectChoices aircrafts = SelectChoices.from(aircraftsList, "registrationNumber", leg.getAircraft());
		SelectChoices arrivalAirports = SelectChoices.from(airportsList, "iataCode", leg.getArrivalAirport());
		SelectChoices departureAirports = SelectChoices.from(airportsList, "iataCode", leg.getDepartureAirport());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode", "duration", "departureAirport", "arrivalAirport", "aircraft", "flight");
		dataset.put("flight", flights.getSelected().getKey());
		dataset.put("flights", flights);
		dataset.put("arrivalAirport", arrivalAirports.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalAirports);
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
