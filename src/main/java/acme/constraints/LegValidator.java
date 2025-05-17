
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.StringHelper;
import acme.entities.leg.Leg;
import acme.entities.leg.LegRepository;

public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private LegRepository legRepository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidLeg annotation) {
	}

	@Override
	public boolean isValid(final Leg legToValidate, final ConstraintValidatorContext context) {
		boolean flightNumberNotNull = legToValidate.getFlightNumber() != null;
		super.state(context, flightNumberNotNull, "flightNumber", "acme.validation.leg.flightNumber.notNull.message");

		boolean scheduledDepartureNotNull = legToValidate.getScheduledDeparture() != null;
		super.state(context, scheduledDepartureNotNull, "scheduledDeparture", "acme.validation.leg.scheduledDeparture.notNull.message");

		boolean scheduledArrivalNotNull = legToValidate.getScheduledArrival() != null;
		super.state(context, scheduledArrivalNotNull, "scheduledArrival", "acme.validation.leg.scheduledArrival.notNull.message");

		boolean statusNotNull = legToValidate.getStatus() != null;
		super.state(context, statusNotNull, "status", "acme.validation.leg.status.notNull.message");

		boolean flightNotNull = legToValidate.getFlight() != null;
		super.state(context, flightNotNull, "flight", "acme.validation.leg.flight.notNull.message");

		boolean departureAirportNotNull = legToValidate.getDepartureAirport() != null;
		super.state(context, departureAirportNotNull, "departureAirport", "acme.validation.leg.departureAirport.notNull.message");

		boolean arrivalAirportNotNull = legToValidate.getArrivalAirport() != null;
		super.state(context, arrivalAirportNotNull, "arrivalAirport", "acme.validation.leg.arrivalAirport.notNull.message");

		boolean aircraftNotNull = legToValidate.getAircraft() != null;
		super.state(context, aircraftNotNull, "aircraft", "acme.validation.leg.aircraft.notNull.message");

		boolean result = !super.hasErrors(context);

		if (result && Integer.valueOf(legToValidate.getId()) != null) {

			// flightNumber uniqueness
			Leg existingLeg = this.legRepository.findLegByFlightNumber(legToValidate.getFlightNumber());
			boolean uniqueness = existingLeg == null || existingLeg.getId() == legToValidate.getId();
			super.state(context, uniqueness, "flightNumber", "acme.validation.leg.flightNumber.unique.message");

			// flightNumber letters correspond to airline's iataCode
			String airlineIataCode = this.legRepository.getIataCodeFromAircraftId(legToValidate.getAircraft().getId());
			boolean flightNumberIataCode = StringHelper.startsWith(legToValidate.getFlightNumber(), airlineIataCode, true);
			super.state(context, flightNumberIataCode, "flightNumber", "acme.validation.leg.flightNumber.iataCode.message");

			// departureAirport different than arrivalAirport
			boolean differentAirports = legToValidate.getDepartureAirport().getId() != legToValidate.getArrivalAirport().getId();
			super.state(context, differentAirports, "departureAirport", "acme.validation.leg.departureAirport.equals.message");

			// Diferencia de al menos 1 minuto entre scheduledDeparture y scheduledArrival
			boolean differentSchedules = legToValidate.getScheduledArrival().getTime() - legToValidate.getScheduledDeparture().getTime() >= 60 * 1000;
			super.state(context, differentSchedules, "scheduledArrival", "acme.validation.leg.scheduledArrival.equals.message");

			// No se solapan Legs para el mismo vuelo
			boolean noOverlappingLegs = this.legRepository.findOverlappingLegs(legToValidate.getFlight().getId(), legToValidate.getId(), legToValidate.getScheduledDeparture(), legToValidate.getScheduledArrival()).isEmpty();
			super.state(context, noOverlappingLegs, "scheduledDeparture", "acme.validation.leg.scheduledDeparture.noOverlappingLegs.message");

			// Diferencia máxima de 24 horas con la última escala
			//boolean noMoreThan1DayWithPreviousLeg = this.legRepository.findScheduledArrival(legToValidate.getFlight().getId()).getTime() - legToValidate.getScheduledDeparture().getTime() <= 60 * 60 * 24 * 1000;
			//super.state(context, noMoreThan1DayWithPreviousLeg, "scheduledDeparture", "acme.validation.leg.scheduledDeparture.noMoreThan1DayWithPreviousLeg.message");

			// No se usa la misma aeronave en diferentes escalas
			boolean noSameAircraft = this.legRepository.findLegByAircraftIdSameTime(legToValidate.getAircraft().getId(), legToValidate.getId(), legToValidate.getScheduledDeparture(), legToValidate.getScheduledArrival()).isEmpty();
			super.state(context, noSameAircraft, "aircraft", "acme.validation.leg.aircraft.noSameAircraft.message");

			// No se usa el mismo aeropuerto en el mismo momento (tienen una entrada y una salida)
			boolean noDepartureAirportOverlap = this.legRepository.findLegByAirportIdSameDeparture(legToValidate.getDepartureAirport().getId(), legToValidate.getId(), legToValidate.getScheduledDeparture()).isEmpty();
			super.state(context, noDepartureAirportOverlap, "departureAirport", "acme.validation.leg.departureAirport.noDepartureAirportOverlap.message");
			// El de llegada
			boolean noArrivalAirportOverlap = this.legRepository.findLegByAirportIdSameArrival(legToValidate.getArrivalAirport().getId(), legToValidate.getId(), legToValidate.getScheduledArrival()).isEmpty();
			super.state(context, noArrivalAirportOverlap, "arrivalAirport", "acme.validation.leg.arrivalAirport.noArrivalAirportOverlap.message");

			// Coherencia entre departureAirport y arrivalAirport con la leg i y la i+1
			boolean consecutiveLegsDepartureAirportEqualsArrivalAirport = this.legRepository.findNextLegWithWrongDeparture(legToValidate.getFlight().getId(), legToValidate.getArrivalAirport().getId(), legToValidate.getScheduledArrival()).isEmpty();
			super.state(context, consecutiveLegsDepartureAirportEqualsArrivalAirport, "arrivalAirport", "acme.validation.leg.arrivalAirport.consecutiveLegsDepartureAirportEqualsArrivalAirport.message");
			// El de llegada
			boolean consecutiveLegsArrivalAirportEqualsDepartureAirport = this.legRepository.findPreviousLegWithWrongArrival(legToValidate.getFlight().getId(), legToValidate.getDepartureAirport().getId(), legToValidate.getScheduledDeparture()).isEmpty();
			super.state(context, consecutiveLegsArrivalAirportEqualsDepartureAirport, "departureAirport", "acme.validation.leg.departureAirport.consecutiveLegsDepartureAirportEqualsArrivalAirport.message");

			result = !super.hasErrors(context);
		}

		return result;
	}
}
