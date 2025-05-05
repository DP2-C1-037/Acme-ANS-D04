
package acme.entities.leg;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidLeg;
import acme.datatypes.LegStatus;
import acme.entities.aircraft.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidLeg
@Table(indexes = {
	@Index(columnList = "flight_id, scheduledDeparture"), // findScheduledDeparture y findLegByFlightByScheduledDeparture
	@Index(columnList = "flight_id, scheduledArrival"), // findScheduledArrival y findLegByFlightByScheduledArrival
	@Index(columnList = "flight_id, scheduledDeparture, departure_airport_id"), // findOriginCity
	@Index(columnList = "flight_id, scheduledArrival, arrival_airport_id"), // findDestinationCity
	@Index(columnList = "flight_id"), // findLegsFromFlightId (x4)
	@Index(columnList = "flightNumber"), // findLegByFlightNumber
	@Index(columnList = "flight_id, draftMode"), // findPublishedLegsByFlightId (x2)
// findAllLegsByAirlineManagerId
})
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "[A-Z]{3}\\d{4}$")
	@Automapped
	// composed of the airline's IATA code followed by four digits, unique -> Done with @ValidLeg
	private String				flightNumber;

	@Mandatory
	@ValidMoment(min = "2000/01/01 00:00", max = "2201/01/01  00:00")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment(min = "2000/01/01 00:00", max = "2201/01/01  00:00")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes


	@Transient
	public Double getDuration() {
		double res = 0.;
		if (this.scheduledDeparture != null && this.scheduledArrival != null)
			res = (this.scheduledArrival.getTime() - this.scheduledDeparture.getTime()) / (1000.0 * 60 * 60);
		return res;
	}

	// Relationships


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft	aircraft;

}
