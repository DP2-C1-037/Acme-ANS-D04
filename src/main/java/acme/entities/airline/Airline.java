
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.datatypes.Phone;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Max(50)
	@ValidString
	private String				name;

	@Mandatory
	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{2}X", message = "Invalid IATA_code. It is three-uppercase-letter, being the last and X")
	@ValidString
	private String				iataCode;

	@Mandatory
	@ValidUrl
	private String				website;

	@Mandatory
	private Type				type;

	@Mandatory
	@ValidMoment(past = true)
	private Date				foundationMoment;

	@Optional
	@ValidEmail
	private String				email;

	@Optional
	private Phone				phoneNumber;

}
