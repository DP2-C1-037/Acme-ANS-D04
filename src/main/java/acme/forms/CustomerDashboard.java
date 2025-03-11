
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.booking.TravelClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	// Serialisation version -----------------------------------------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<String>				lastFiveDestinations;
	Money						lastYearMoneySpentInBookings;
	Map<TravelClass, Integer>	numberOfBookingsByTravelClass;
	Double						lastFiveYearsBookingCostsCount;
	Double						lastFiveYearsBookingCostsAverage;
	Double						lastFiveYearsBookingCostsMinimun;
	Double						lastFiveYearsBookingCostsMaximum;
	Double						lastFiveYearsBookingCostsStandardDeviation;
	Integer						bookingsNumberOfPassengersCount;
	Double						bookingsNumberOfPassengersAverage;
	Integer						bookingsNumberOfPassengersMinimum;
	Integer						bookingsNumberOfPassengersMaximum;
	Double						bookingsNumberOfPassengersStandardDeviation;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
