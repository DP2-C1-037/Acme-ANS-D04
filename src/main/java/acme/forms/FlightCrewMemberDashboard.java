
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidScore;
import acme.constraints.ValidFlightAssignmentsStatus;
import acme.constraints.ValidIncidentCounts;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	private List<String>			lastFiveDestinations;

	@Mandatory
	@ValidIncidentCounts
	private Map<String, Integer>	incidentCountsBySeverity; // Example: {"0-3": 5, "4-7": 2, "8-10": 1}

	@Mandatory
	private List<String>			lastLegCrewMembers;

	@Mandatory
	@ValidFlightAssignmentsStatus
	private Map<String, Integer>	flightAssignmentsByStatus;

	@Mandatory
	@ValidScore
	private Double					averageFlightAssignments;

	@Mandatory
	@ValidNumber(min = 0)
	private Integer					minFlightAssignments;

	@Mandatory
	@ValidNumber(min = 0)
	private Integer					maxFlightAssignments;

	@Mandatory
	@ValidScore
	private Double					stdDevFlightAssignments;
}
