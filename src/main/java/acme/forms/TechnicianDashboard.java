
package acme.forms;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import acme.client.components.basis.AbstractForm;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long				serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	private Map<MaintenanceStatus, Integer>	maintenanceRecordsByStatus;

	@Optional
	@Valid
	private MaintenanceRecord				nearestInspectionMaintenanceRecord;

	@Mandatory
	private List<Aircraft>					higherTaskNumberAircrafts;

	@Optional
	@ValidNumber(min = 0)
	private Double							lastYearAverageMaintenanceCost;

	@Optional
	@ValidNumber(min = 0)
	private Double							lastYearMaxMaintenanceCost;

	@Optional
	@ValidNumber(min = 0)
	private Double							lastYearMinMaintenanceCost;

	@Optional
	@ValidNumber(min = 0)
	private Double							lastYearStandardDeviationMaintenanceCost;

	@Mandatory
	@ValidNumber(min = 0)
	private Double							taskAverageDuration;

	@Mandatory
	@ValidNumber(min = 0)
	private Integer							taskMaxDuration;

	@Mandatory
	@ValidNumber(min = 0)
	private Integer							tasgMinDuration;

	@Mandatory
	@ValidNumber(min = 0)
	private Double							taskStandardDeviationDuration;

}
