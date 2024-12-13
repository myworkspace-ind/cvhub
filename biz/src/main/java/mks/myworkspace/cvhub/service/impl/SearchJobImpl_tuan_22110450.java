package mks.myworkspace.cvhub.service.impl;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.repository.JobRequestRepository_tuan_22110450;
import mks.myworkspace.cvhub.service.SearchJobService;
import mks.myworkspace.cvhub.service.SearchJobService_tuan_22110450;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
	public Page<JobRequest> searchJobRequest(String keyword, int locationCD, List<Long> selectedIndustries, String sort, boolean search, int page, int size) {
		Sort sorting = getSortOrder(sort); // Xác định thứ tự sắp xếp
		Pageable pageable = PageRequest.of(page, size, sorting);

		// Sử dụng phương thức tìm kiếm tùy chỉnh trong repository
		try {

//			return jobRepository.findBySearchCriteria(locationCD, selectedIndustries, keyword, keyword, search, pageable);
			String jpql = buildSearchQuery(locationCD, selectedIndustries, keyword, keyword, search);
		    return executeSearchQuery(jpql, locationCD, selectedIndustries, keyword, keyword, search, pageable);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private Sort getSortOrder(String sort) {
		switch (sort) {
			case "new":
				return Sort.by(Sort.Direction.DESC, "createdDate");
			case "up_top":
				return Sort.by(Sort.Direction.DESC, "modified");
			case "high_salary":
				return Sort.by(Sort.Direction.DESC, "salary");
			case "experientia":
				return Sort.by(Sort.Direction.ASC, "experience");
			default:
				return Sort.unsorted();
		}
	}
	
	private Page<JobRequest> executeSearchQuery(String jpql, Integer locationCD, List<Long> selectedIndustries, String company, String jobName, boolean search, Pageable pageable) {
	    // Truy vấn dữ liệu chính
	    String sortedJpql = jpql + getSortOrderString(pageable); // Áp dụng sắp xếp từ pageable
	    TypedQuery<JobRequest> query = entityManager.createQuery(sortedJpql, JobRequest.class);

	    // Thiết lập các tham số
	    if (locationCD != null && locationCD > 0) {
	        query.setParameter("locationCD", locationCD);
	    }

	    if (selectedIndustries != null && !selectedIndustries.isEmpty()) {
	        query.setParameter("industryCD", selectedIndustries);
	    }

	    query.setParameter("company", company);
	    query.setParameter("jobName", jobName);
	    query.setParameter("bool", search);

	    // Áp dụng phân trang
	    query.setFirstResult((int) pageable.getOffset());
	    query.setMaxResults(pageable.getPageSize());

	    // Lấy kết quả từ truy vấn
	    List<JobRequest> results = query.getResultList();

	    // Truy vấn đếm tổng số phần tử phù hợp với tiêu chí tìm kiếm
	    String countJpql = "SELECT COUNT(j) FROM JobRequest j WHERE " + jpql.substring(jpql.indexOf("WHERE") + 5);  // Giữ lại phần WHERE từ jpql ban đầu
	    TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
	    if (locationCD != null && locationCD > 0) {
	        countQuery.setParameter("locationCD", locationCD);
	    }
	    if (selectedIndustries != null && !selectedIndustries.isEmpty()) {
	        countQuery.setParameter("industryCD", selectedIndustries);
	    }
	    countQuery.setParameter("company", company);
	    countQuery.setParameter("jobName", jobName);
	    countQuery.setParameter("bool", search);

	    // Lấy tổng số phần tử
	    Long total = countQuery.getSingleResult();

	    // Trả về kết quả phân trang
	    return new PageImpl<>(results, pageable, total);
	}

	private String getSortOrderString(Pageable pageable) {
	    Sort sort = pageable.getSort();
	    if (sort.isEmpty()) {
	        return "";
	    }

	    StringBuilder sortOrderString = new StringBuilder(" ORDER BY ");
	    for (Sort.Order order : sort) {
	        sortOrderString.append("j.")
	                       .append(order.getProperty())
	                       .append(" ")
	                       .append(order.getDirection().name())
	                       .append(", ");
	    }

	    // Loại bỏ dấu phẩy cuối cùng
	    return sortOrderString.substring(0, sortOrderString.length() - 2);
	}


	private String buildSearchQuery(Integer locationCD, List<Long> selectedIndustries, String company, String jobName, boolean search) {
	    StringBuilder queryBuilder = new StringBuilder("SELECT j FROM JobRequest j WHERE ");
	    List<String> conditions = new ArrayList<>();

	    // Location condition
	    if (locationCD != null && locationCD > 0) {
	        conditions.add("j.location.code = :locationCD");
	    }

	    // Industry condition
	    if (selectedIndustries != null && !selectedIndustries.isEmpty()) {
	        conditions.add("j.jobRole.id IN :industryCD");
	    }

	        conditions.add("(:bool = false OR j.organization.title LIKE CONCAT('%', :company, '%'))");

	        conditions.add("(:bool = true OR j.title LIKE CONCAT('%', :jobName, '%'))");

	    // Bool flag condition
//	    conditions.add("(:bool = true AND j.title LIKE CONCAT('%', :jobName, '%')) OR (:bool = false)");

	    queryBuilder.append(String.join(" AND ", conditions));
	    
	    return queryBuilder.toString();
	}
}