
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.SpringHelper;
import acme.entities.airline.AirlineRepository;
import acme.entities.airline.Leg;

public class LegFlightNumberValidator extends AbstractValidator<ValidLegFlightNumber, Leg> {

	@Override
	public boolean isValid(final Leg legToValidate, final ConstraintValidatorContext context) {
		AirlineRepository airlineRepository = SpringHelper.getBean(AirlineRepository.class);
		String airlineIataCode = airlineRepository.getIataCodeFromLegId(legToValidate.getId());
		String legToValidateFlightNumber = legToValidate.getFlightNumber().substring(0, 3);
		boolean result = airlineIataCode.equals(legToValidateFlightNumber);
		if (!result)
			super.state(context, false, "flightNumber", "acme.validation.leg.flightNumber.message");
		return result;
	}
}
