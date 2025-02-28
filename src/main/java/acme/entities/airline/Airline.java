
package acme.entities.airline;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.checkerframework.common.aliasing.qual.Unique;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidUrl;
import acme.datatypes.Phone;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Airline extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Max(50)
	private String				name;

	@Mandatory
	@Unique
	@Pattern(regexp = "[A-Z]{2}X", message = "Invalid IATA_code. It is three-uppercase-letter, being the last and X")
	private String				iataCode;

	@Mandatory
	@ValidUrl
	private String				website;

	@Mandatory
	private Type				type;

	@Mandatory
	@Past
	private Date				foundationMoment;

	@Optional
	@ValidEmail
	private String				email;

	@Optional
	private Phone				phoneNumber;

}
