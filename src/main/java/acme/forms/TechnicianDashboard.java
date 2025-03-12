
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Map<String, Integer>	maintenanceRecordsByStatus;
	private String					nearestInspectionMaintenanceRecord;
	private List<String>			higherTaskNumberAircrafts;
	private Double					lastYearAverageMaintenanceCost;
	private Double					lastYearMaxMaintenanceCost;
	private Double					lastYearMinMaintenanceCost;
	private Double					lastYearStandardDeviationMaintenanceCost;
	private Double					taskAverageDuration;
	private Integer					taskMaxDuration;
	private Integer					tasgMinDuration;
	private Double					taskStandardDeviationDuration;

}
