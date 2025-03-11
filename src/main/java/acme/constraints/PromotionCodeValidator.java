
package acme.constraints;

import java.text.SimpleDateFormat;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.service.Service;

public class PromotionCodeValidator extends AbstractValidator<ValidPromotionCode, Service> {

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {

		boolean result = true;

		if (service == null || service.getPromotionCode() == null)
			result = false;

		SimpleDateFormat sdf = new SimpleDateFormat("yy");

		String promotionCode = service.getPromotionCode();
		String promotionCodeLastTwoDigits = promotionCode.substring(promotionCode.length() - 2);

		String currentYearLastTwoDigits = sdf.format(MomentHelper.getBaseMoment());

		if (!promotionCodeLastTwoDigits.equals(currentYearLastTwoDigits)) {
			super.state(context, false, "services", "acme.validation.service.promotionCode.message");
			return false;
		}

		return result;
	}

}
