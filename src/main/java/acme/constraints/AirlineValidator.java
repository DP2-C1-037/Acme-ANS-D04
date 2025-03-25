
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.SpringHelper;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineRepository;

public class AirlineValidator extends AbstractValidator<ValidAirline, Airline> {

	// PREGUNTAR SI VALE CON ESE IF CON AND O SI TENGO QUE ACUMULAR LOS ERRORES (NO SÉ COMO SE  HACE)
	@Override
	public boolean isValid(final Airline airlineToValidate, final ConstraintValidatorContext context) {
		AirlineRepository airlineRepository = SpringHelper.getBean(AirlineRepository.class);
		boolean result = false;
		if (airlineToValidate != null && airlineToValidate.getIataCode() != null) {
			result = airlineRepository.iataCodeAlreadyExists(airlineToValidate.getIataCode());
			if (!result)
				super.state(context, false, "flightNumber", "acme.validation.airline.iataCode.message");
		}
		return result;
	}
}
