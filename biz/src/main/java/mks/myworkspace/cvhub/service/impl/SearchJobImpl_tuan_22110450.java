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

//		// Sử dụng phương thức tìm kiếm tùy chỉnh trong repository
//		return jobRepository.findBySearchCriteria(locationCD, industryCD, keyword, keyword, sorting);
		// Sử dụng phương thức tìm kiếm tùy chỉnh trong repository
		try {

			return jobRepository.findBySearchCriteria(locationCD, industryCD, keyword, keyword, search, pageable);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

//		String jpql = buildQuery(keyword, locationCD, industryCD, sorting, search);
//		return executeQuery(jpql, keyword, locationCD, industryCD);
	}

//	@Override
//	public List<JobRequest> searchJobRequest(String keyword, int locationCD, Long industryCD) {
//		String jpql = buildQuery(keyword, locationCD, industryCD);
//		return executeQuery(jpql, keyword, locationCD, industryCD);
//	}

//	private List<JobRequest> executeQuery(String jpql, String keyword, int locationCD, Long industryCD) {
//		TypedQuery<JobRequest> query = entityManager.createQuery(jpql, JobRequest.class);
//		if (keyword != null && !keyword.isEmpty()) {
//			query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
//		}
//		if (locationCD > 0) {
//			query.setParameter("locationCD", locationCD);
//		}
//		if (industryCD > 0) {
//			query.setParameter("industryCD", industryCD);
//		}
//
//		return query.getResultList();
//	}
//
//	private String buildQuery(String keyword, int locationCD, Long industryCD, Sort sort, boolean search) {
//		StringBuilder queryBuilder = new StringBuilder("SELECT jr FROM JobRequest jr");
//		List<String> conditions = new ArrayList<>();
//
//		if (keyword != null && !keyword.isEmpty() && search) {
//			// tim theo ten job voi search la true
//			conditions.add("LOWER(jr.title) LIKE LOWER(:keyword)");
//		}
//		else{
//			// tim theo ten cong ty voi search là false
//			conditions.add("LOWER(jr.title) LIKE LOWER(:keyword)");
//		}
//		if (locationCD > 0) {
//			conditions.add("jr.location.id = :locationCD");
//		}
//		if (industryCD > 0) {
//			conditions.add("jr.jobRole.id = :industryCD");
//		}
//
//		if (!conditions.isEmpty()) {
//			queryBuilder.append(" WHERE ");
//			queryBuilder.append(String.join(" AND ", conditions));
//		}
//
//		return queryBuilder.toString();
//	}

	private Sort getSortOrder(String sort) {
		switch (sort) {
			case "new":
				return Sort.by(Sort.Direction.DESC, "deadlineApplication");
			case "up_top":
				return Sort.by(Sort.Direction.DESC, "deadlineApplication");
			case "high_salary":
				return Sort.by(Sort.Direction.DESC, "salary");
			case "experientia":
				return Sort.by(Sort.Direction.ASC, "experience");
			default:
				return Sort.unsorted();
		}
	}
}