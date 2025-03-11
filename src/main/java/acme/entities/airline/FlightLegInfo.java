
package acme.entities.airline;

import java.util.Date;

public record FlightLegInfo(Date scheduledDeparture, String originCity, Date scheduledArrival, String arrivalCity) {

}
