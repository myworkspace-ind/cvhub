package mks.myworkspace.cvhub.service;

import mks.myworkspace.cvhub.entity.JobRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchJobService_tuan_22110450 {
// Page<JobRequest> searchJobRequest(String keyword, int locationCD, Long industryCD, String sort, boolean search, int page, int size);
	Page<JobRequest> searchJobRequest(String keyword, int locationCD, List<Long> selectedIndustries , String sort, boolean search, int page, int size);
}
