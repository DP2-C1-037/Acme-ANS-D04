
package acme.entities.activityLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidActivityLog;
import acme.constraints.ValidLongText;
import acme.entities.flightAssignment.FlightAssignment;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidActivityLog
public class ActivityLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;
	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private FlightAssignment	flightAssignment;
	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true, max = "2201/01/01 00:00:00")
	private Date				registrationMoment;
	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				typeOfIncident;
	@Mandatory
	@Automapped
	@ValidLongText
	private String				description;
	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 10)
	private Integer				severityLevel;
}
