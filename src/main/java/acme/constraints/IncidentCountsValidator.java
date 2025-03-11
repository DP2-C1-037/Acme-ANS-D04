
package acme.constraints;

import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class IncidentCountsValidator extends AbstractValidator<ValidIncidentCounts, Map<String, Integer>> {

	@Override
	protected void initialise(final ValidIncidentCounts annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Map<String, Integer> value, final ConstraintValidatorContext context) {
		assert context != null;

		if (value == null || value.isEmpty())
			return false;

		boolean result = true;

		// Verificar las claves del mapa, que deben seguir el patrón "0-3", "4-7", "8-10"
		for (String key : value.keySet()) {
			boolean matches = Pattern.matches("^(0-3|4-7|8-10)$", key);
			if (!matches) {
				super.state(context, false, "incidentCounts", "acme.validation.incident-counts.message");
				result = false;
			}
		}

		return result;
	}
}
