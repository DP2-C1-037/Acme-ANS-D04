
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.airline.AirlineManager;

public class IdentifierNumberValidator extends AbstractValidator<ValidIdentifierNumber, AirlineManager> {

	@Override
	public boolean isValid(final AirlineManager airlineManager, final ConstraintValidatorContext context) {
		String initials = IdentifierNumberValidator.getInitials(airlineManager.getUserAccount().getIdentity().getName(), airlineManager.getUserAccount().getIdentity().getSurname());
		String identifierNumberToValidate = airlineManager.getIdentifierNumber().substring(0, initials.length());
		boolean result = initials.equals(identifierNumberToValidate);
		if (!result)
			super.state(context, false, "identifierNumber", "acme.validation.flight.identifierNumber.message");
		return result;
	}

	private static String getInitials(final String name, final String surname) {
		StringBuilder result = new StringBuilder();
		result.append(name.charAt(0));
		result.append(surname.charAt(0));
		return result.toString();
	}
}
