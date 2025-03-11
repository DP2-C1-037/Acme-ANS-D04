
package acme.constraints;

import java.util.Map;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.datatypes.AssignmentStatus;

@Validator
public class FlightAssignmentsStatusValidator extends AbstractValidator<ValidFlightAssignmentsStatus, Map<String, Integer>> {

	@Override
	protected void initialise(final ValidFlightAssignmentsStatus annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Map<String, Integer> value, final ConstraintValidatorContext context) {
		assert context != null;

		if (value == null || value.isEmpty())
			return false;

		boolean result = true;

		for (String key : value.keySet()) {
			boolean isValidStatus = this.isValidAssignmentStatus(key);
			if (!isValidStatus) {
				super.state(context, false, "flightAssignments", "acme.validation.assignments-status.message");
				result = false;
			}
		}

		return result;
	}

	private boolean isValidAssignmentStatus(final String status) {
		try {
			AssignmentStatus.valueOf(status);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
