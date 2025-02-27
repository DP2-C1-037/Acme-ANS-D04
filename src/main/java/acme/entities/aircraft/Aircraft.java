
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L; // olde Java remora

	/*
	 * @Mandatory
	 * 
	 * @ManyToOne (optional = false)
	 * 
	 * @Valid
	 * Airline airline // an aircraft belongs to an arline
	 */

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	String						model;

	@Mandatory
	@Column(unique = true)
	@ValidString(min = 1, max = 50)
	String						registrationNumber;

	@Mandatory
	@Automapped
	Integer						capacity; // number of passengers

	@Mandatory
	@Automapped
	@ValidNumber(min = 2000, max = 50000)
	Integer						cargoWeight; //kgs

	@Mandatory
	@Automapped
	Boolean						status; // true means "in active service" and false "under maintenance"

	@Optional
	@Automapped
	@ValidString(min = 1, max = 255)
	String						optionalDetails;
}
