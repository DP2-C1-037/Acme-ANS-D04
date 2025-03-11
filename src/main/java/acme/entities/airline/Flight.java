
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Entity;
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
	private boolean				requiresSelfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString
	@Automapped
	private String				description;


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
	private int getLayoversNumber() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return legRepository.findAll().size() - 1;
	}

}
