
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.SpringHelper;
import acme.client.helpers.StringHelper;
import acme.entities.airline.AirlineManager;
import acme.entities.airline.AirlineManagerRepository;

public class AirlineManagerValidator extends AbstractValidator<ValidAirlineManager, AirlineManager> {

	// PREGUNTAR SI VALE CON ESE IF CON AND O SI TENGO QUE ACUMULAR LOS ERRORES (NO SÉ COMO SE  HACE)
	@Override
	public boolean isValid(final AirlineManager airlineManager, final ConstraintValidatorContext context) {
		AirlineManagerRepository airlineManagerRepository = SpringHelper.getBean(AirlineManagerRepository.class);
		boolean result = false;
		if (airlineManager != null && airlineManager.getUserAccount() != null && airlineManager.getUserAccount().getIdentity().getName() != null && airlineManager.getUserAccount().getIdentity().getSurname() != null
			&& airlineManager.getIdentifierNumber() != null) {

			DefaultUserIdentity managerIdentity = airlineManager.getUserAccount().getIdentity();
			String initials = AirlineManagerValidator.getInitials(managerIdentity.getName(), managerIdentity.getSurname());
			String identifierNumberToValidate = airlineManager.getIdentifierNumber();
			result = StringHelper.startsWith(initials, airlineManager.getIdentifierNumber().substring(0, initials.length()), true);
			if (!result)
				super.state(context, false, "identifierNumber", "acme.validation.airlineManager.identifierNumber.initials.message");
			if (!airlineManagerRepository.identifierNumberAlreadyExists(identifierNumberToValidate)) {
				super.state(context, false, "identifierNumber", "acme.validation.airlineManager.identifierNumber.unique.message");
				result = false;
			}
		}

		return result;
	}

	private static String getInitials(final String name, final String surname) {
		StringBuilder result = new StringBuilder();
		result.append(name.charAt(0));
		result.append(surname.charAt(0));
		return result.toString();
	}
}
