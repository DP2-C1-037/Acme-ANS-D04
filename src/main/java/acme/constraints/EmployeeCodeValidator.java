
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AssistanceAgent;

@Validator
public class EmployeeCodeValidator extends AbstractValidator<ValidEmployeeCode, AssistanceAgent> {

	@Override
	protected void initialise(final ValidEmployeeCode constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		boolean isNull;
		isNull = assistanceAgent == null || assistanceAgent.getEmployeeCode() == null;

		if (!isNull) {
			String employeeCode = assistanceAgent.getEmployeeCode();
			boolean matchesPattern = employeeCode.matches("^[A-Z]{2,3}\\d{6}$");

			super.state(context, matchesPattern, "employeeCode", "{acme.validation.employee-code.invalid-format.message}");

			if (matchesPattern) {
				DefaultUserIdentity identity = assistanceAgent.getUserAccount().getIdentity();

				String nameInitial = String.valueOf(identity.getName().charAt(0)).toUpperCase();
				String firstNameInitial = String.valueOf(identity.getSurname().charAt(0)).toUpperCase();
				String secondNameInitial = "";
				Integer initialsLength = 2;

				if (identity.getSurname().contains(" ")) {
					secondNameInitial = String.valueOf(identity.getSurname().split(" ")[1].charAt(0)).toUpperCase();
					initialsLength++;
				}

				String expectedInitials = nameInitial + firstNameInitial + secondNameInitial;
				String employeeCodeInitials = employeeCode.substring(0, initialsLength);

				boolean initialsMatch = expectedInitials.equals(employeeCodeInitials);
				super.state(context, initialsMatch, "licenseNumber", "{acme.validation.employee-code.not-matching-initials.message}");
			}
		}

		result = !super.hasErrors(context);
		return result;
	}
}
