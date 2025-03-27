
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.SpringHelper;
import acme.client.helpers.StringHelper;
import acme.entities.airline.AirlineRepository;
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
	@Override
	public boolean isValid(final Leg legToValidate, final ConstraintValidatorContext context) {
		AirlineRepository airlineRepository = SpringHelper.getBean(AirlineRepository.class);
		boolean result = false;
		if (legToValidate != null && legToValidate.getFlightNumber() != null) {

			// flightNumber letters correspond to airline's iataCode
			String airlineIataCode = airlineRepository.getIataCodeFromLegId(legToValidate.getId());
			result = StringHelper.startsWith(legToValidate.getFlightNumber(), airlineIataCode, true);
			super.state(context, result, "flightNumber", "acme.validation.leg.flightNumber.iataCode.message");

			// flightNumber uniqueness
			Leg existingLeg = this.legRepository.findLegByFlightNumber(legToValidate.getFlightNumber());
			result = existingLeg == null || existingLeg.equals(legToValidate);
			super.state(context, false, "flightNumber", "acme.validation.leg.flightNumber.unique.message");
		}
		return result;
	}
}
