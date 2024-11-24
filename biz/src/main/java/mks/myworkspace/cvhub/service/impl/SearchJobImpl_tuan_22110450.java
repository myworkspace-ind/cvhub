package mks.myworkspace.cvhub.service.impl;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.repository.JobRequestRepository_tuan_22110450;
import mks.myworkspace.cvhub.service.SearchJobService;
import mks.myworkspace.cvhub.service.SearchJobService_tuan_22110450;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchJobImpl_tuan_22110450 implements SearchJobService_tuan_22110450 {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JobRequestRepository_tuan_22110450 jobRepository;

	@Override
	public Page<JobRequest> searchJobRequest(String keyword, int locationCD, Long industryCD, String sort, boolean search, int page, int size) {
		Sort sorting = getSortOrder(sort); // Xác định thứ tự sắp xếp
		Pageable pageable = PageRequest.of(page, size, sorting);

		// Sử dụng phương thức tìm kiếm tùy chỉnh trong repository
		try {

			return jobRepository.findBySearchCriteria(locationCD, industryCD, keyword, keyword, search, pageable);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private Sort getSortOrder(String sort) {
		switch (sort) {
			case "new":
				return Sort.by(Sort.Direction.ASC, "createdDate");
			case "up_top":
				return Sort.by(Sort.Direction.ASC, "modified");
			case "high_salary":
				return Sort.by(Sort.Direction.DESC, "salary");
			case "experientia":
				return Sort.by(Sort.Direction.ASC, "experience");
			default:
				return Sort.unsorted();
		}
	}
}