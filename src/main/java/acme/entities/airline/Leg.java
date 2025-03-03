
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.aircraft.Aircraft;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@ManyToOne
	private Flight				flight;

	@Mandatory
	@ValidString
	@Automapped
	@Column(unique = true)
	// composed of the airline's IATA code followed by four digits, unique
	private String				flightNumber;

	@Mandatory
	@ValidMoment
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	private Date				scheduledArrival;

	@Mandatory
	@ValidNumber
	private Integer				duration;

	@Mandatory
	private Status				status;

	@Mandatory
	private Airport				departureAirport;

	@Mandatory
	private Airport				arrivalAirport;

	@Mandatory
	private Aircraft			deployedAircraft;

}
