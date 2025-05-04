
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.StringHelper;
import acme.entities.leg.Leg;
import acme.entities.leg.LegRepository;

public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegRepository legRepository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	// Con el if de las propiedades no nulas vale
	// CADA VALOR QUE NO EXISTA TENGO QUE ENVIAR UNA EXCEPCIÓN POR HACKEO
	// LAS URLS TAMBIÉN HAY QUE TENERLAS EN CUENTA
	@Override
	public boolean isValid(final Leg legToValidate, final ConstraintValidatorContext context) {
		boolean result = false;
		boolean flightNumberNotNull = legToValidate.getFlightNumber() != null;
		super.state(context, flightNumberNotNull, "flightNumber", "acme.validation.leg.flightNumber.notNull.message");

		boolean scheduledDepartureNotNull = legToValidate.getScheduledDeparture() != null;
		super.state(context, scheduledDepartureNotNull, "scheduledDeparture", "acme.validation.leg.scheduledDeparture.notNull.message");

		boolean scheduledArrivalNotNull = legToValidate.getScheduledArrival() != null;
		super.state(context, scheduledArrivalNotNull, "scheduledArrival", "acme.validation.leg.scheduledArrival.notNull.message");

		boolean statusNotNull = legToValidate.getStatus() != null;
		super.state(context, statusNotNull, "status", "acme.validation.leg.status.notNull.message");

		boolean flightNotNull = legToValidate.getFlight() != null;
		super.state(context, flightNotNull, "flight", "acme.validation.leg.flight.notNull.message");

		boolean departureAirportNotNull = legToValidate.getDepartureAirport() != null;
		super.state(context, departureAirportNotNull, "departureAirport", "acme.validation.leg.departureAirport.notNull.message");

		boolean arrivalAirportNotNull = legToValidate.getArrivalAirport() != null;
		super.state(context, arrivalAirportNotNull, "arrivalAirport", "acme.validation.leg.arrivalAirport.notNull.message");

		boolean aircraftNotNull = legToValidate.getAircraft() != null;
		super.state(context, aircraftNotNull, "aircraft", "acme.validation.leg.aircraft.notNull.message");

		result = !super.hasErrors(context);

		if (result && Integer.valueOf(legToValidate.getId()) != null) {

			// flightNumber uniqueness
			Leg existingLeg = this.legRepository.findLegByFlightNumber(legToValidate.getFlightNumber());
			boolean uniqueness = existingLeg == null || existingLeg.getId() == legToValidate.getId();
			super.state(context, uniqueness, "flightNumber", "acme.validation.leg.flightNumber.unique.message");

			// flightNumber letters correspond to airline's iataCode
			String airlineIataCode = this.legRepository.getIataCodeFromAircraftId(legToValidate.getAircraft().getId());
			boolean flightNumberIataCode = StringHelper.startsWith(legToValidate.getFlightNumber(), airlineIataCode, true);
			super.state(context, flightNumberIataCode, "flightNumber", "acme.validation.leg.flightNumber.iataCode.message");

			// departureAirport different than arrivalAirport
			boolean differentAirports = legToValidate.getDepartureAirport().getId() != legToValidate.getArrivalAirport().getId();
			super.state(context, differentAirports, "departureAirport", "acme.validation.leg.departureAirport.equals.message");

			// Diferencia de al menos 1 minuto entre scheduledDeparture y scheduledArrival
			boolean differentSchedules = legToValidate.getScheduledArrival().getTime() - legToValidate.getScheduledDeparture().getTime() >= 60 * 1000;
			super.state(context, differentSchedules, "scheduledArrival", "acme.validation.leg.scheduledArrival.equals.message");

			// No hay 2 legs asociadas al mismo vuelo con misma fecha de salida
			boolean differentScheduledDeparture = this.legRepository.findLegByFlightByScheduledDeparture(legToValidate.getFlight().getId(), legToValidate.getId(), legToValidate.getScheduledDeparture()) == null;
			super.state(context, differentScheduledDeparture, "scheduledDeparture", "acme.validation.leg.scheduledDepartureAnotherLeg.equals.message");

			// No hay 2 legs asociadas al mismo vuelo con misma fecha de llegada
			boolean differentScheduledArrival = this.legRepository.findLegByFlightByScheduledArrival(legToValidate.getFlight().getId(), legToValidate.getId(), legToValidate.getScheduledArrival()) == null;
			super.state(context, differentScheduledArrival, "scheduledArrival", "acme.validation.leg.scheduledArrivalAnotherLeg.equals.message");

			result = !super.hasErrors(context);
		}

		return result;
	}
}
