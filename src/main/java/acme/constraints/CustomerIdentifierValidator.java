
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.realms.Customer;

public class CustomerIdentifierValidator extends AbstractValidator<ValidCustomerIdentifier, Customer> {

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {

		boolean result = true;

		if (customer == null || customer.getIdentity() == null || customer.getIdentity().getFullName() == null || customer.getIdentifier() == null)
			return false;

		String name = customer.getIdentity().getName().trim();
		String surname = customer.getIdentity().getSurname().trim();
		String initials = "" + name.charAt(0) + surname.charAt(0);

		String identifier = customer.getIdentifier().trim();
		String identifierPrefix = identifier.substring(0, initials.length());

		if (!initials.equals(identifierPrefix)) {
			super.state(context, false, "customers", "acme.validation.customer.identifier.message");
			result = false;
		}

		return result;
	}

}
