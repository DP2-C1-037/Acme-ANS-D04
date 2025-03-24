
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.SpringHelper;
import acme.entities.airline.AirlineRepository;
import acme.entities.airline.Leg;
import acme.entities.airline.LegRepository;

public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// PREGUNTAR SI VALE CON ESE IF CON AND O SI TENGO QUE ACUMULAR LOS ERRORES (NO SÉ COMO SE  HACE)
	@Override
	public boolean isValid(final Leg legToValidate, final ConstraintValidatorContext context) {
		AirlineRepository airlineRepository = SpringHelper.getBean(AirlineRepository.class);
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		boolean result = false;
		if (legToValidate != null && legToValidate.getFlightNumber() != null) {
			String airlineIataCode = airlineRepository.getIataCodeFromLegId(legToValidate.getId());
			String flightNumberIataCodeToValidate = legToValidate.getFlightNumber().substring(0, 3);
			String flightNumberToValidate = legToValidate.getFlightNumber();
			result = airlineIataCode.equals(flightNumberIataCodeToValidate);
			if (!result)
				super.state(context, false, "flightNumber", "acme.validation.leg.flightNumber.iataCode.message");
			if (!legRepository.flightNumberAlreadyExists(flightNumberToValidate)) {
				super.state(context, false, "flightNumber", "acme.validation.leg.flightNumber.unique.message");
				result = false;
			}
		}
		return result;
	}
}
