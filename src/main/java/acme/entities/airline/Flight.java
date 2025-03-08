
package acme.entities.airline;

import javax.persistence.Entity;
import javax.persistence.Transient;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
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
	@ValidNumber(min = 0, max = 1)
	@Automapped
	private Integer				requiresSelfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString
	@Automapped
	private String				description;

	/*
	 * @Transient
	 * public FlightLegInfo getScheduledDepartureAndOriginCity() {
	 * LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
	 * return legRepository.flightArrivalDateAndCity(this.getId());
	 * }
	 * 
	 * @Transient
	 * public FlightLegInfo getScheduledArrivalAndDestinyCity() {
	 * LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
	 * return legRepository.flightDepartureDateAndCity(this.getId());
	 * }
	 */


	public FlightLegInfo getArrivalAndOriginData() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return legRepository.flightData(this.getId());
	}

	@Transient
	private Integer getLayoversNumber() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return legRepository.findAll().size();
	}

}
