package mks.myworkspace.cvhub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import mks.myworkspace.cvhub.entity.JobRequest;

public interface JobRequestRepository_Anh_22110280 extends JpaRepository<JobRequest, Long> {
	 Page<JobRequest> findByOrganizationIdAndTitleContainingIgnoreCase(Long organizationId, String title, Pageable pageable);

	 Page<JobRequest> findByOrganizationId(Long organizationId, Pageable pageable);

	Page<JobRequest> findByOrganizationIdAndTitleContainingIgnoreCaseAndLocation_Code(Long organizationId,
			String titleFilter, String locationCode, Pageable pageable);
}
