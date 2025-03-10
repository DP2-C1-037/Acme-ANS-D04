
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L; // olde Java remora

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airline				airline; // an aircraft belongs to an arline

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				model;

	@Mandatory
	@Column(unique = true)
	@ValidString(min = 1, max = 50)
	private String				registrationNumber;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 255)
	private Integer				capacity; // number of passengers

	@Mandatory
	@Automapped
	@ValidNumber(min = 2000, max = 50000)
	private Integer				cargoWeight; //kgs

	@Mandatory
	@Automapped
	@Valid
	private AircraftStatus		status;

	@Optional
	@Automapped
	@ValidString(min = 0, max = 255)
	private String				optionalDetails;
}
