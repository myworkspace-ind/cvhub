package mks.myworkspace.cvhub.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import lombok.Getter;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.JobRequestRepository;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.OrganizationService;

@Service
public class JobRequestImpl implements JobRequestService {

	@Getter
	@Autowired
	JobRequestRepository repo;
	@Autowired
	LocationService locationService;
	@Autowired
	JobRoleService jobRoleService;
	@Autowired
	OrganizationService organizationService;
	public final Logger logger = LoggerFactory.getLogger(this.getClass());;

	@Override
	public JobRequest createJobRequest(String title, int locationCode, Long jobRoleId, Integer experience,
			Integer salary, Long organizationId, String jobDescription, String requirementsCandidate,
			String benefitCandidate, LocalDate deadlineApplication) {
		// Kiểm tra tính hợp lệ của các tham số đầu vào
		if (title == null || title.isEmpty() || organizationId == null) {
			throw new IllegalArgumentException("Title and Organization ID must not be null or empty.");
		}

		// Tìm kiếm đối tượng Location và JobRole từ mã tương ứng
		Location location = locationService.getRepo().findById(locationCode)
				.orElseThrow(() -> new IllegalArgumentException("Invalid location code"));

		JobRole jobRole = jobRoleService.getRepo().findById(jobRoleId).orElse(null); // Có thể là null nếu không tìm
																						// thấy

		// Tìm kiếm đối tượng Organization từ organizationId
		Organization organization = organizationService.getRepo().findById(organizationId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid organization ID"));

		// Tạo một đối tượng JobRequest mới
		JobRequest jobRequest = new JobRequest();
		jobRequest.setTitle(title);
		jobRequest.setLocation(location);
		jobRequest.setJobRole(jobRole);
		jobRequest.setExperience(experience);
		jobRequest.setSalary(salary);
		jobRequest.setOrganization(organization);
		jobRequest.setDetailsJob(jobDescription);
		jobRequest.setRequirementsCandidate(requirementsCandidate);
		jobRequest.setBenefitCandidate(benefitCandidate);
		jobRequest.setDeadlineApplication(deadlineApplication);

		// Trả về đối tượng JobRequest đã tạo
		return jobRequest;
	}

	@Override
	public JobRequest updateJobRequest( JobRequest jobRequest, String title, int locationCode, Long jobRoleId,
			Integer experience, Integer salary, String jobDescription, String requirementsCandidate,
			String benefitCandidate, LocalDate deadlineApplication, String courseLink) {


// Validate input parameters
		if (title == null || title.isEmpty()) {
			throw new IllegalArgumentException("Title must not be null or empty.");
		}

// Find Location and JobRole objects from their respective codes
		Location location = locationService.getRepo().findById(locationCode)
				.orElseThrow(() -> new IllegalArgumentException("Invalid location code"));

		JobRole jobRole = jobRoleService.getRepo().findById(jobRoleId).orElse(null); // Can be null if not found

// Update the existing JobRequest object
		jobRequest.setTitle(title);
		jobRequest.setLocation(location);
		jobRequest.setJobRole(jobRole);
		jobRequest.setExperience(experience);
		jobRequest.setSalary(salary);
		jobRequest.setDetailsJob(jobDescription);
		jobRequest.setRequirementsCandidate(requirementsCandidate);
		jobRequest.setBenefitCandidate(benefitCandidate);
		jobRequest.setDeadlineApplication(deadlineApplication);
		jobRequest.setCourseLink(courseLink);

// Save and return the updated JobRequest
		return getRepo().save(jobRequest);
	}

	@Override
	public void deleteJobRequest(JobRequest jobRequest) {
		if (jobRequest == null) {
	        throw new IllegalArgumentException("JobRequest must not be null.");
	    }
		getRepo().delete(jobRequest);
		
	}

	@Override
	public List<JobRequest> findAllByOrganizationId(Long id) {
		// TODO Auto-generated method stub
		return repo.findByOrganizationId(id);
	}
}