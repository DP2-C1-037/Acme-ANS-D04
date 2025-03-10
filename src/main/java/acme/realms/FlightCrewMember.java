
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.datatypes.AvailabilityStatus;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
// @ValidFlightCrewMember

public class FlightCrewMember extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airline				airline;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	private String				employeeCode;

	@Mandatory
	@Automapped
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	private String				phoneNumber;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				languageSkills;

	@Mandatory
	@Automapped
	@Valid
	private AvailabilityStatus	availabilityStatus;

	@Mandatory
	@Automapped
	@ValidMoney(min = 0.00, max = 1000000.00)
	private Money				salary;

	@Optional
	@Automapped
	@ValidNumber(min = 0, max = 120)
	private Integer				yearsOfExperience;

}
