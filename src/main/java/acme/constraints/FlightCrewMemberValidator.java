
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.features.flightCrewMember.FlightCrewMemberRepository;
import acme.realms.FlightCrewMember;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Autowired
	private FlightCrewMemberRepository repository;


	@Override
	protected void initialise(final ValidFlightCrewMember constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember flightCrewMember, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (flightCrewMember == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueFlightCrewMember;
				FlightCrewMember existingFlightCrewMember;

				existingFlightCrewMember = this.repository.findFlightCrewMemberByEmployeeCode(flightCrewMember.getEmployeeCode());
				uniqueFlightCrewMember = existingFlightCrewMember == null || existingFlightCrewMember.equals(flightCrewMember);

				super.state(context, uniqueFlightCrewMember, "flightCrewMember", "acme.validation.flight-crew-member.employee-code-duplicated.message");
			}
			{
				String employeeCode = flightCrewMember.getEmployeeCode();
				boolean matchesPattern = employeeCode.matches("^[A-Z]{2,3}\\d{6}$");

				super.state(context, matchesPattern, "employeeCode", "{acme.validation.flight-crew-member.employee-code-invalid-format.message}");

			}
			{
				DefaultUserIdentity identity = flightCrewMember.getUserAccount().getIdentity();
				String employeeCode = flightCrewMember.getEmployeeCode();

				String nameInitial = StringHelper.capitaliseInitial(StringHelper.smallInitial(identity.getName())).substring(0, 1);
				String firstNameInitial = StringHelper.capitaliseInitial(StringHelper.smallInitial(identity.getSurname())).substring(0, 1);
				String expectedInitials = nameInitial + firstNameInitial;

				String employeeCodeInitials = employeeCode.substring(0, 2);

				if (!StringHelper.isEqual(expectedInitials, employeeCodeInitials, false))
					super.state(context, false, "employeeCode", "{acme.validation.flight-crew-member.employee-code-not-matching-initials.message}");
			}
		}

		result = !super.hasErrors(context);
		return result;
	}
}
