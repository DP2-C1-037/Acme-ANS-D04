
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidLegFlightNumber;
import acme.datatypes.LegStatus;
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

	@Mandatory
	@ValidString(pattern = "[A-Z]{3}\\d{4}$")
	@Column(unique = true)
	// composed of the airline's IATA code followed by four digits, unique -> Done with @ValidLegFlightNumber
	private String				flightNumber;

	@Mandatory
	@ValidMoment(min = "2000/01/01 00:00")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment(min = "2000/01/01 00:00")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@ValidNumber(min = 1, max = 24) // Consultar -> CAMBIAR A DERIVADA
	@Automapped
	private Integer				duration;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	// Relationships

	@Mandatory
	@Valid
	@ManyToOne
	private Flight				flight;

	@Mandatory
	@Valid
	@ManyToOne
	private Airport				departureAirport;

	@Mandatory
	@Valid
	@ManyToOne
	private Airport				arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne
	private Aircraft			deployedAircraft;

}
