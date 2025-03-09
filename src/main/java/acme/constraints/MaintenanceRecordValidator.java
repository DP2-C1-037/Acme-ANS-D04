
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.maintenanceRecords.MaintenanceRecord;

public class MaintenanceRecordValidator extends AbstractValidator<ValidMaintenanceRecord, MaintenanceRecord> {

	@Override
	public void initialize(final ValidMaintenanceRecord constraintAnnotation) {
		assert constraintAnnotation != null;
	}

	@Override
	public boolean isValid(final MaintenanceRecord maintenanceRecord, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		boolean isNull;

		isNull = maintenanceRecord == null || maintenanceRecord.getMaintenanceDate() == null && maintenanceRecord.getNextInspectionDueDate() == null;

		if (!isNull) {
			boolean nextInspectionIsAfterMaintenance;

			Date maintenanceDate = maintenanceRecord.getMaintenanceDate();
			Date nextInspection = maintenanceRecord.getNextInspectionDueDate();
			nextInspectionIsAfterMaintenance = MomentHelper.isAfter(nextInspection, maintenanceDate);

			super.state(context, nextInspectionIsAfterMaintenance, "nextInspectionDueDate", "{acme.validation.next-inspection.message}");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
