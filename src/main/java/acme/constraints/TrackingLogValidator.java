
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.trackingLog.TrackingLog;
import acme.entities.trackingLog.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		// HINT: trackingLog can be null
		assert context != null;

		boolean result;
		boolean isNull;

		isNull = trackingLog == null || trackingLog.getResolPercentage() == null || trackingLog.getStatus() == null;

		if (!isNull) {
			{
				Double resolPercentage;
				boolean validPercentage;

				resolPercentage = trackingLog.getResolPercentage();
				validPercentage = resolPercentage >= 0.0 && resolPercentage <= 100.0;
				super.state(context, validPercentage, "resolPercentage", "acme.validation.trackingLog.resolPercentage.message");
			}
			{
				TrackingLogStatus status;
				boolean validStatus;

				status = trackingLog.getStatus();
				if (trackingLog.getResolPercentage() == 100.0)
					validStatus = status == TrackingLogStatus.ACCEPTED || status == TrackingLogStatus.DENIED;
				else
					validStatus = status == TrackingLogStatus.PENDING;
				super.state(context, validStatus, "status", "acme.validation.trackingLog.status.message");
			}
			{
				String resolution;
				boolean validResolution;

				resolution = trackingLog.getResolution();
				if (trackingLog.getResolPercentage() == 100.0)
					validResolution = resolution != null && !resolution.isEmpty();
				else
					validResolution = true;
				super.state(context, validResolution, "resolution", "acme.validation.trackingLog.resolution.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}
}
