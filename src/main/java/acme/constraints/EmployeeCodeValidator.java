
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import acme.realms.AssistanceAgent;

public class EmployeeCodeValidator implements ConstraintValidator<ValidEmployeeCode, AssistanceAgent> {

	@Override
	public void initialize(final ValidEmployeeCode constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent value, final ConstraintValidatorContext context) {
		if (value == null || value.getEmployeeCode() == null || value.getUserAccount() == null)
			return false;

		String employeeCode = value.getEmployeeCode();
		String name = value.getUserAccount().getIdentity().getFullName();

		if (!employeeCode.matches("^[A-Z]{2,3}\\d{6}$"))
			return false;

		String initialsFromCode = employeeCode.substring(0, employeeCode.length() == 9 ? 3 : 2);

		String[] nameParts = name.split(" ");
		StringBuilder initialsFromName = new StringBuilder();
		for (String part : nameParts)
			if (!part.isEmpty())
				initialsFromName.append(part.charAt(0));

		return initialsFromCode.equals(initialsFromName.toString().toUpperCase());
	}
}
