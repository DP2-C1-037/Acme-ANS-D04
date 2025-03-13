
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Technician;

@Validator
public class TechnicianValidator extends AbstractValidator<ValidTechnician, Technician> {

	@Override
	public void initialise(final ValidTechnician constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final Technician technician, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		boolean isNull;

		isNull = technician == null || technician.getLicenseNumber() == null || technician.getUserAccount() == null;

		if (!isNull) {
			boolean licenseNumberValid;

			String licenseNumber = technician.getLicenseNumber();
			DefaultUserIdentity identity = technician.getUserAccount().getIdentity();
			String name = identity.getName().trim();
			String surname = identity.getSurname().trim();
			licenseNumberValid = name.charAt(0) == licenseNumber.charAt(0) && surname.charAt(0) == licenseNumber.charAt(1);

			super.state(context, licenseNumberValid, "licenseNumber", "{acme.validation.technician.licenseNumber.message}");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
