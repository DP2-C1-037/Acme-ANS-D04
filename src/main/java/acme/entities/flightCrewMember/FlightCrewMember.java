
package acme.entities.flightCrewMember;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMember extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	String						employeeCode;

	@Mandatory
	@Automapped
	@ValidString(pattern = "^+?\\d{6,15}$")
	String						phoneNumber;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	String						languageSkills;

	@Mandatory
	@Automapped
	@Valid
	AvailabilityStatus			availabilityStatus;

	/*
	 * @Mandatory
	 * 
	 * @Automapped
	 * 
	 * @Valid
	 * Airline airline;
	 */

	@Mandatory
	@Automapped
	@ValidMoney
	Money						salary;

	@Optional
	@Automapped
	@ValidNumber(min = 1, max = 150) // no person can have more than 150 years of experience
	Integer						yearsOfExperience;

}
