
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AirlineManager extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{2-3}\\d{6}$", message = "Phone number must be between 6 and 15 digits long and may start with a +")
	// the first two or three letters correspond to their initials
	private String				identifierNumber;

	@Mandatory
	@ValidNumber
	private Integer				experienceYears;

	@Mandatory
	@ValidMoment(past = true)
	private Date				birthDate;

	// MUST BE STORED SOMEWHERE ELSE
	@Optional
	@ValidUrl
	private String				pictureLink;

}
