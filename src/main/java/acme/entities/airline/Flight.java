
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.datatypes.FlightSelfTransfer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private FlightSelfTransfer	requiresSelfTransfer;

	@Mandatory
	@ValidMoney(min = 0, max = 1000000)
	@Automapped
	private Money				cost;

	@Optional
	@ValidString
	@Automapped
	private String				description;

	@Mandatory
	@Automapped
	private boolean				draftMode			= true;

	// Derived attributes


	@Transient
	public Date getScheduledDeparture() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return legRepository.findScheduledDeparture(this.getId());
	}

	@Transient
	public Date getScheduledArrival() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return legRepository.findScheduledArrival(this.getId());
	}

	@Transient
	public String getOriginCity() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return legRepository.findOriginCity(this.getId());
	}

	@Transient
	public String getDestinationCity() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return legRepository.findDestinationCity(this.getId());
	}

	@Transient
	public int getLayoversNumber() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return legRepository.legsNumberFromFlightId(this.getId());
	}

	// Relationships


	/*
	 * PREGUNTAR. Inicialmente, un objeto" de la clase "Flight" podría no tener ningún leg asociado.
	 * La comprobación de que un objeto "Flight" tiene al menos un "Leg" es necesaria antes de poder publicar un vuelo;
	 * es entonces cuando se debe rechazar la posibilidad de publicar si un vuelo no tiene ningún "Leg" asociado.
	 */
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager airlineManager;
}
