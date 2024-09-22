package mks.myworkspace.cvhub.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.service.SearchJobService;

@Service
public class SearchJobImpl implements SearchJobService {

	@Autowired
	private EntityManager entityManager;

	@Override
	public List<JobRequest> searchJobRequest(String keyword, int locationCD, Long industryCD) {
		String jpql = buildQuery(keyword, locationCD, industryCD);
		return executeQuery(jpql, keyword, locationCD, industryCD);
	}

	private List<JobRequest> executeQuery(String jpql, String keyword, int locationCD, Long industryCD) {
		TypedQuery<JobRequest> query = entityManager.createQuery(jpql, JobRequest.class);
		if (keyword != null && !keyword.isEmpty()) {
			query.setParameter("keyword", "%" + keyword.toLowerCase() + "%");
		}
		if (locationCD > 0) {
			query.setParameter("locationCD", locationCD);
		}
		if (industryCD > 0) {
			query.setParameter("industryCD", industryCD);
		}

		return query.getResultList();
	}

	private String buildQuery(String keyword, int locationCD, Long industryCD) {
		StringBuilder queryBuilder = new StringBuilder("SELECT jr FROM JobRequest jr");
		List<String> conditions = new ArrayList<>();

		if (keyword != null && !keyword.isEmpty()) {
			conditions.add("LOWER(jr.title) LIKE LOWER(:keyword)");
		}
		if (locationCD > 0) {
			conditions.add("jr.location.id = :locationCD");
		}
		if (industryCD > 0) {
			conditions.add("jr.jobRole.id = :industryCD");
		}

		if (!conditions.isEmpty()) {
			queryBuilder.append(" WHERE ");
			queryBuilder.append(String.join(" AND ", conditions));
		}

		return queryBuilder.toString();
	}
}