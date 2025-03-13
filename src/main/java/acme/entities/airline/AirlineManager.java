
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
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
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	// the first two or three letters correspond to their initials -> @ValidIdentifierNumber
	private String				identifierNumber;

	@Mandatory
	@ValidNumber
	@Automapped
	private Integer				experienceYears;

	@Mandatory
	@ValidMoment(min = "1965/01/01 00:00", past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				birthDate;

	// MUST BE STORED SOMEWHERE ELSE
	@Optional
	@ValidUrl
	@Automapped
	private String				pictureLink;

	// Relationships

	@Mandatory
	@Valid
	@ManyToOne
	private Airline				airline;

}
