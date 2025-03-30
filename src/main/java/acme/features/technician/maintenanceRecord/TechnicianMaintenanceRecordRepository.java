
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecord;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select m from MaintenanceRecord m where m.draftMode = false")
	Collection<MaintenanceRecord> findPublishedMaintenanceRecords();

	@Query("select m from MaintenanceRecord m where m.technician.id = :technicianId")
	Collection<MaintenanceRecord> findMaintenanceRecordsByTechnicianId(int technicianId);

	@Query("select m from MaintenanceRecord m where m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);
}
