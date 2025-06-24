
package acme.features.customer.dashboard;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.forms.CustomerDashboard;
import acme.realms.customer.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
		// TODO: ADD HACKING SECURITY
	}

	@Override
	public void load() {
		int id = super.getRequest().getPrincipal().getActiveRealm().getId();
		CustomerDashboard dashboard;
		List<String> lastFiveDestinations;
		Double lastYearMoneySpentInBookings;
		Map<String, Integer> numberOfBookingsByTravelClass;
		Double lastFiveYearsBookingCostsCount;
		Double lastFiveYearsBookingCostsAverage;
		Double lastFiveYearsBookingCostsMinimum;
		Double lastFiveYearsBookingCostsMaximum;
		Double lastFiveYearsBookingCostsStandardDeviation;
		Integer bookingsNumberOfPassengersCount;
		Double bookingsNumberOfPassengersAverage;
		Integer bookingsNumberOfPassengersMinimum;
		Integer bookingsNumberOfPassengersMaximum;
		Double bookingsNumberOfPassengersStandardDeviation;

		Pageable pageable = PageRequest.of(0, 5);
		List<Flight> lastYearFiveFlights = this.repository.findLastFiveFlights(id, pageable);
		lastFiveDestinations = lastYearFiveFlights.isEmpty() ? null : lastYearFiveFlights.stream().map(Flight::getDestinationCity).toList();

		Date lastYear = MomentHelper.deltaFromCurrentMoment(-1L, ChronoUnit.YEARS);
		List<Booking> lastYearBookings = this.repository.findDeltaYearBookings(id, lastYear);
		lastYearMoneySpentInBookings = lastYearBookings.stream().mapToDouble(b -> b.getPrice().getAmount()).sum();

		List<Object[]> numberOfBookingsAndTravelClasses = this.repository.findNumberOfBookingsByTravelClass(id);
		if (!numberOfBookingsAndTravelClasses.isEmpty()) {
			numberOfBookingsByTravelClass = new HashMap<>();
			for (Object[] o : numberOfBookingsAndTravelClasses) {
				TravelClass travelClass = (TravelClass) o[0];
				Long count = (Long) o[1];
				numberOfBookingsByTravelClass.put(travelClass.toString(), count.intValue());
			}
		} else
			numberOfBookingsByTravelClass = null;

		Date lastFiveYears = MomentHelper.deltaFromCurrentMoment(-5L, ChronoUnit.YEARS);
		List<Booking> lastFiveYearBookings = this.repository.findDeltaYearBookings(id, lastFiveYears);
		if (!lastFiveYearBookings.isEmpty()) {
			List<Double> lastFiveYearBookingsPrices = lastFiveYearBookings.stream().map(b -> b.getPrice().getAmount()).collect(Collectors.toList());
			lastFiveYearsBookingCostsCount = lastFiveYearBookingsPrices.stream().mapToDouble(p -> p).sum();
			lastFiveYearsBookingCostsAverage = lastFiveYearBookingsPrices.stream().mapToDouble(p -> p).average().getAsDouble();
			lastFiveYearsBookingCostsMinimum = lastFiveYearBookingsPrices.stream().mapToDouble(p -> p).min().getAsDouble();
			lastFiveYearsBookingCostsMaximum = lastFiveYearBookingsPrices.stream().mapToDouble(p -> p).max().getAsDouble();
			Double lastFiveYearBookingsSquaredSum = lastFiveYearBookingsPrices.stream().mapToDouble(p -> Math.pow(p - lastFiveYearsBookingCostsAverage, 2)).sum();
			lastFiveYearsBookingCostsStandardDeviation = Math.sqrt(lastFiveYearBookingsSquaredSum / (lastFiveYearBookings.size() - 1));
		} else {
			lastFiveYearsBookingCostsCount = 0.;
			lastFiveYearsBookingCostsAverage = 0.;
			lastFiveYearsBookingCostsMinimum = 0.;
			lastFiveYearsBookingCostsMaximum = 0.;
			lastFiveYearsBookingCostsStandardDeviation = 0.;
		}

		List<Booking> bookingsFromCustomer = this.repository.findBookingsFromCustomer(id);
		if (!bookingsFromCustomer.isEmpty()) {
			List<Integer> bookingsNumberOfPassengers = bookingsFromCustomer.stream().map(b -> this.repository.findNumberOfPassengersFromBooking(b.getId())).toList();
			bookingsNumberOfPassengersCount = (int) bookingsNumberOfPassengers.stream().mapToInt(n -> n).sum();
			bookingsNumberOfPassengersAverage = bookingsNumberOfPassengers.isEmpty() ? null : bookingsNumberOfPassengers.stream().mapToInt(n -> n).average().getAsDouble();
			bookingsNumberOfPassengersMinimum = bookingsNumberOfPassengers.stream().mapToInt(n -> n).min().getAsInt();
			bookingsNumberOfPassengersMaximum = bookingsNumberOfPassengers.stream().mapToInt(n -> n).max().getAsInt();
			Double numberOfPassengersSquaredSum = bookingsNumberOfPassengers.stream().mapToInt(n -> n).mapToDouble(p -> Math.pow(p - bookingsNumberOfPassengersAverage, 2)).sum();
			bookingsNumberOfPassengersStandardDeviation = Math.sqrt(numberOfPassengersSquaredSum / (bookingsFromCustomer.size() - 1));
		} else {
			bookingsNumberOfPassengersCount = 0;
			bookingsNumberOfPassengersAverage = 0.;
			bookingsNumberOfPassengersMinimum = 0;
			bookingsNumberOfPassengersMaximum = 0;
			bookingsNumberOfPassengersStandardDeviation = 0.;
		}

		dashboard = new CustomerDashboard();
		dashboard.setLastFiveDestinations(lastFiveDestinations);
		dashboard.setLastYearMoneySpentInBookings(lastYearMoneySpentInBookings);
		dashboard.setNumberOfBookingsByTravelClass(numberOfBookingsByTravelClass);
		dashboard.setLastFiveYearsBookingCostsCount(lastFiveYearsBookingCostsCount);
		dashboard.setLastFiveYearsBookingCostsAverage(lastFiveYearsBookingCostsAverage);
		dashboard.setLastFiveYearsBookingCostsMinimum(lastFiveYearsBookingCostsMinimum);
		dashboard.setLastFiveYearsBookingCostsMaximum(lastFiveYearsBookingCostsMaximum);
		dashboard.setLastFiveYearsBookingCostsStandardDeviation(lastFiveYearsBookingCostsStandardDeviation);
		dashboard.setBookingsNumberOfPassengersCount(bookingsNumberOfPassengersCount);
		dashboard.setBookingsNumberOfPassengersAverage(bookingsNumberOfPassengersAverage);
		dashboard.setBookingsNumberOfPassengersMinimum(bookingsNumberOfPassengersMinimum);
		dashboard.setBookingsNumberOfPassengersMaximum(bookingsNumberOfPassengersMaximum);
		dashboard.setBookingsNumberOfPassengersStandardDeviation(bookingsNumberOfPassengersStandardDeviation);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, //
			"lastFiveDestinations", "lastYearMoneySpentInBookings", // 
			"numberOfBookingsByTravelClass", "lastFiveYearsBookingCostsCount", //
			"lastFiveYearsBookingCostsAverage", "lastFiveYearsBookingCostsMinimum", //
			"lastFiveYearsBookingCostsMaximum", "lastFiveYearsBookingCostsStandardDeviation", //
			"bookingsNumberOfPassengersCount", "bookingsNumberOfPassengersAverage", //
			"bookingsNumberOfPassengersMinimum", "bookingsNumberOfPassengersMaximum", //
			"bookingsNumberOfPassengersStandardDeviation");

		super.getResponse().addData(dataset);
	}

}
