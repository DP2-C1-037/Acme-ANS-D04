
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import acme.entities.activityLog.ActivityLog;

public class ActivityLogValidator implements ConstraintValidator<ValidActivityLog, ActivityLog> {

	@Override
	public void initialize(final ValidActivityLog constraintAnnotation) {
		assert constraintAnnotation != null;
	}
	@Override
	public boolean isValid(final ActivityLog log, final ConstraintValidatorContext context) {
		if (log == null || log.getFlightAssignment() == null || log.getRegistrationMoment() == null)
			return false;
		var leg = log.getFlightAssignment().getLeg();
		if (leg == null || leg.getScheduledArrival() == null)
			return false;
		Date endMoment = leg.getScheduledArrival();
		return log.getRegistrationMoment().after(endMoment);
	}
}
