
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidLegFlightNumber;
import acme.entities.aircraft.Aircraft;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidLegFlightNumber
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@ManyToOne
	@Automapped
	private Flight				flight;

	@Mandatory
	@ValidString(min = 3, max = 3, pattern = "[A-Z]{2}X\\d{4}$")
	@Automapped
	@Column(unique = true)
	// composed of the airline's IATA code followed by four digits, unique -> Done with @ValidLegFlightNumber
	private String				flightNumber;

	@Mandatory
	@ValidMoment
	@Automapped
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Automapped
	private Date				scheduledArrival;

	@Mandatory
	@ValidNumber(min = 1, max = 24) // Consultar
	@Automapped
	private Integer				duration;

	@Mandatory
	@Automapped
	private Status				status;

	@Mandatory
	@Automapped
	@OneToOne
	private Airport				departureAirport;

	@Mandatory
	@Automapped
	@OneToOne
	private Airport				arrivalAirport;

	@Mandatory
	@Automapped
	@ManyToOne
	private Aircraft			deployedAircraft;

}
