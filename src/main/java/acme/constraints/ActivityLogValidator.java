
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.activityLog.ActivityLog;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Override
	protected void initialise(final ValidActivityLog constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog log, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		boolean isNull = log == null || log.getFlightAssignment() == null || log.getRegistrationMoment() == null;

		if (!isNull) {
			var leg = log.getFlightAssignment().getLeg();
			boolean legIsNull = leg == null || leg.getScheduledArrival() == null;

			if (!legIsNull) {
				Date endMoment = leg.getScheduledArrival();
				boolean isAfter = log.getRegistrationMoment().after(endMoment);

				super.state(context, isAfter, "registrationMoment", "{acme.validation.registration-moment-log.message}");
			}
		}

		result = !super.hasErrors(context);
		return result;
	}
}
