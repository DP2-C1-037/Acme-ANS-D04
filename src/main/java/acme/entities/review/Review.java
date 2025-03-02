
package acme.entities.review;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.principals.UserAccount;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review extends AbstractEntity {
	// Serialisation version -----------------------------------------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes ----------------------------------------------------------------------------------------------------
	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	String						name;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	String						subject;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	String						text;

	@Optional
	@ValidNumber(min = 0.0, max = 10.0)
	@Automapped
	Double						score;

	@Optional
	@Valid
	@Automapped
	Boolean						recommended;

	// Relationships ----------------------------------------------------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private UserAccount			userAccount;

}
