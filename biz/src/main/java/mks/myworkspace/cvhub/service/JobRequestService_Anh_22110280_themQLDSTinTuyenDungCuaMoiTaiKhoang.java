package mks.myworkspace.cvhub.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.repository.JobRequestRepository_Anh_22110280;
@Service
public interface JobRequestService_Anh_22110280_themQLDSTinTuyenDungCuaMoiTaiKhoang {
	Page<JobRequest> findJobsByCriteria(Long organizationId, String searchTerm, String locationCode, String sortBy, int page, int size);

	JobRequestRepository_Anh_22110280 getRepo();

	void deleteJobRequest(JobRequest jobRequest);

	List<JobRequest> findAllByOrganizationId(Long id);

}
