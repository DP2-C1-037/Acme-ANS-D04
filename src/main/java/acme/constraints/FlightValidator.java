
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;

public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightRepository flightRepository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	// Con el if de las propiedades no nulas vale
	// CADA VALOR QUE NO EXISTA TENGO QUE ENVIAR UNA EXCEPCIÓN POR HACKEO
	// LAS URLS TAMBIÉN HAY QUE TENERLAS EN CUENTA
	@Override
	public boolean isValid(final Flight flightToValidate, final ConstraintValidatorContext context) {
		boolean result = true;

		if (Boolean.valueOf(flightToValidate.isDraftMode()) != null && Integer.valueOf(flightToValidate.getId()) != null)
			if (!flightToValidate.isDraftMode()) {
				int publishedLegsSize = this.flightRepository.findPublishedLegsByFlightId(flightToValidate.getId()).size();
				int legsSize = this.flightRepository.findLegsByFlightId(flightToValidate.getId()).size();
				result = publishedLegsSize > 0 && legsSize > 0 && publishedLegsSize == legsSize;
				super.state(context, result, "*", "acme.validation.flight.*.notAllLegsPublishedOrNoLegs.message");
			}
		return result;
	}
}
