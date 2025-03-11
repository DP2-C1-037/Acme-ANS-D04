
package acme.constraints;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import acme.entities.customer.Customer;

public class CustomerIdentifierValidator implements ConstraintValidator<ValidCustomerIdentifier, Customer> {

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		if (customer == null || customer.getIdentity() == null || customer.getIdentity().getFullName() == null || customer.getIdentifier() == null)
			return false;

		String fullName = customer.getIdentity().getFullName().trim();
		String identifier = customer.getIdentifier().trim();

		String initials = CustomerIdentifierValidator.getInitials(fullName);

		String identifierPrefix = identifier.substring(0, initials.length());

		if (!initials.equalsIgnoreCase(identifierPrefix)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Identifier does not match customer initials.").addConstraintViolation();
			return false;
		}

		return true;
	}

	private static String getInitials(final String fullName) {
		return Arrays.stream(fullName.split("\\s+")).filter(p -> !p.isEmpty()).map(p -> String.valueOf(p.charAt(0)).toUpperCase()).collect(Collectors.joining());
	}
}
