
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import javax.persistence.OneToOne;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidIdentifierNumber;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidIdentifierNumber
public class AirlineManager extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@ValidString(min = 8, max = 9, pattern = "[A-Z]{2,3}\\d{6}$")
	@Automapped
	// the first two or three letters correspond to their initials -> @ValidIdentifierNumber
	private String				identifierNumber;

	@Mandatory
	@ValidNumber
	@Automapped
	private Integer				experienceYears;

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				birthDate;

	// MUST BE STORED SOMEWHERE ELSE
	@Optional
	@ValidUrl
	@Automapped
	private String				pictureLink;

}
