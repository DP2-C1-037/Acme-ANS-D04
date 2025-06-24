
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	// Serialisation version -----------------------------------------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// TODO: REPLACE MONEY VALUES FROM DOUBLE TO MONEY DATATYPE

	List<String>				lastFiveDestinations;
	Double						lastYearMoneySpentInBookings;
	Map<String, Integer>		numberOfBookingsByTravelClass;
	Double						lastFiveYearsBookingCostsCount;
	Double						lastFiveYearsBookingCostsAverage;
	Double						lastFiveYearsBookingCostsMinimum;
	Double						lastFiveYearsBookingCostsMaximum;
	Double						lastFiveYearsBookingCostsStandardDeviation;
	Integer						bookingsNumberOfPassengersCount;
	Double						bookingsNumberOfPassengersAverage;
	Integer						bookingsNumberOfPassengersMinimum;
	Integer						bookingsNumberOfPassengersMaximum;
	Double						bookingsNumberOfPassengersStandardDeviation;

}
