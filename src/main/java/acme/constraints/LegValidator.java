
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.StringHelper;
import acme.entities.airline.Leg;
import acme.entities.airline.LegRepository;

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
		if (legToValidate != null && legToValidate.getFlightNumber() != null && Integer.valueOf(legToValidate.getId()) != null) {

			// flightNumber uniqueness
			Leg existingLeg = this.legRepository.findLegByFlightNumber(legToValidate.getFlightNumber());
			result = existingLeg == null || existingLeg.equals(legToValidate);
			super.state(context, result, "flightNumber", "acme.validation.leg.flightNumber.unique.message");

			// flightNumber letters correspond to airline's iataCode
			String airlineIataCode = this.legRepository.getIataCodeFromLegId(legToValidate.getId());
			result = StringHelper.startsWith(legToValidate.getFlightNumber(), airlineIataCode, true);
			super.state(context, result, "flightNumber", "acme.validation.leg.flightNumber.iataCode.message");

			// departureAirport different than arrivalAirport
			result = !legToValidate.getDepartureAirport().equals(legToValidate.getArrivalAirport());
			super.state(context, result, "departureAirport", "acme.validation.leg.departureAirport.equals.message");

			// Diferencia de al menos 1 minuto entre scheduledDeparture y scheduledArrival
			result = legToValidate.getScheduledArrival().getTime() - legToValidate.getScheduledDeparture().getTime() >= 60 * 1000;
			super.state(context, result, "scheduledArrival", "acme.validation.leg.scheduledArrival.equals.message");

		}
		return result;
	}
}
