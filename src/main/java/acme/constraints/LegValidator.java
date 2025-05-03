
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
		if (legToValidate != null && legToValidate.getFlightNumber() != null && Integer.valueOf(legToValidate.getId()) != null && legToValidate.getAircraft() != null && legToValidate.getScheduledArrival() != null
			&& legToValidate.getScheduledDeparture() != null && legToValidate.getDepartureAirport() != null && legToValidate.getDepartureAirport() != null) {

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

			result = !super.hasErrors(context);
		}

		return result;
	}
}
