
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airports.Airport;
import acme.entities.airports.AirportRepository;

@Validator
public class AirportValidator extends AbstractValidator<ValidAirport, Airport> {

	@Autowired
	AirportRepository repository;


	@Override
	protected void initialise(final ValidAirport constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final Airport airport, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		boolean isNull;

		isNull = airport == null || airport.getCode() == null;

		if (!isNull) {
			boolean uniqueAirport;
			Airport existingAirport;

			existingAirport = this.repository.findAirportByCode(airport.getCode());
			uniqueAirport = existingAirport == null || existingAirport.equals(airport);

			super.state(context, uniqueAirport, "code", "acme.validation.airport.code-duplicated.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
