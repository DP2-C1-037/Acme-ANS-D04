
package acme.forms;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import acme.client.components.basis.AbstractForm;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	// Serialisation version -----------------------------------------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	List<String>				lastFiveDestinations;

	@Mandatory
	@Valid
	@ValidNumber(min = 0)
	Double						lastYearMoneySpentInBookings;

	@Mandatory
	@Valid
	Map<String, Integer>		numberOfBookingsByTravelClass;

	@Mandatory
	@Valid
	@ValidNumber(min = 0)
	Double						lastFiveYearsBookingCostsCount;

	@Mandatory
	@Valid
	@ValidNumber(min = 0)
	Double						lastFiveYearsBookingCostsAverage;

	@Mandatory
	@Valid
	@ValidNumber(min = 0)
	Double						lastFiveYearsBookingCostsMinimun;

	@Mandatory
	@Valid
	@ValidNumber(min = 0)
	Double						lastFiveYearsBookingCostsMaximum;

	@Mandatory
	@Valid
	@ValidNumber(min = 0)
	Double						lastFiveYearsBookingCostsStandardDeviation;

	@Mandatory
	@Valid
	@ValidNumber(min = 0)
	Integer						bookingsNumberOfPassengersCount;

	@Mandatory
	@Valid
	@ValidNumber(min = 0)
	Double						bookingsNumberOfPassengersAverage;

	@Mandatory
	@Valid
	@ValidNumber(min = 0)
	Integer						bookingsNumberOfPassengersMinimum;

	@Mandatory
	@Valid
	@ValidNumber(min = 0)
	Integer						bookingsNumberOfPassengersMaximum;

	@Mandatory
	@Valid
	@ValidNumber(min = 0)
	Double						bookingsNumberOfPassengersStandardDeviation;

}
