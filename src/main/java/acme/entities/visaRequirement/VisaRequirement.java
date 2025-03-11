
package acme.entities.visaRequirement;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
// https://travel-buddy.ai/api/
public class VisaRequirement extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString(min = 0, max = 50)
	private String				passportOf;

	@Mandatory
	@Automapped
	@ValidString(min = 2, max = 2)
	private String				passportCode;

	@Mandatory
	@Automapped
	@ValidString(min = 0, max = 50)
	private String				destination;

	@Mandatory
	@Automapped
	@ValidString(min = 0, max = 50)
	private String				passValid;

	@Optional
	@Automapped
	@ValidString(min = 0, max = 255)
	private String				exceptText;

	@Mandatory
	@Automapped
	@ValidString(min = 0, max = 50)
	private String				visa;

	@Optional
	@Automapped
	@ValidString(min = 0, max = 255)
	private String				stayOf;

	@Mandatory
	@Automapped
	@ValidUrl
	private String				link;

	@Mandatory
	@Automapped
	@Valid
	private Boolean				error;

	@Mandatory
	@Automapped
	@ValidString(min = 0, max = 10)
	private String				color;
}
