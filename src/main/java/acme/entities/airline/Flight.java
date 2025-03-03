
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Max(50)
	@ValidString
	private String				tag;

	@Mandatory
	@ValidNumber
	@Min(0)
	@Max(1)
	private Integer				requiresSelfTransfer;

	@Mandatory
	@ValidMoney
	private Money				cost;

	@Optional
	@ValidString
	private String				description;

	@Mandatory
	@ValidMoment
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	private Date				scheduledArrival;

	@Mandatory
	@ValidString
	private String				originCity;

	@Mandatory
	@ValidString
	private String				destinationCity;

	@Mandatory
	// Numero de escalas
	@ValidNumber
	private Integer				layovers;

}
