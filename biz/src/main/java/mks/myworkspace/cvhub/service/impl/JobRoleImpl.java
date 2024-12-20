package mks.myworkspace.cvhub.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.Getter;
import mks.myworkspace.cvhub.dao.JobRoleDao;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.model.JobRoleJDBC;
import mks.myworkspace.cvhub.repository.JobRoleRepository;
import mks.myworkspace.cvhub.service.JobRoleService;

@Service
public class JobRoleImpl implements JobRoleService {

	@Getter
	@Autowired
	JobRoleRepository repo;
	@Autowired
    private JobRoleDao jobRoleDao;
	
	@Override
	public JobRole createJobRole(String title, String description) {

		// kiem tra cac tham so dau vao
		if (title == null) {
			throw new IllegalArgumentException("Title must not be null or empty.");
		}
		JobRole jobRole = new JobRole(title, description);

		return jobRole;
	}
	
	@Override
	public JobRoleJDBC createJobRoleJdbc(JobRoleJDBC jobRoleJDBC) {
	    // Kiểm tra các tham số đầu vào
	    if (jobRoleJDBC.getTitle() == null) {
	        throw new IllegalArgumentException("Title must not be null or empty.");
	    }
	    
	    // Thiết lập các giá trị mặc định khác nếu cần
	    if (jobRoleJDBC.getDescription() == null) {
	        jobRoleJDBC.setDescription("");
	    }
	    if (jobRoleJDBC.getCreatedDate() == null) {
	        jobRoleJDBC.setCreatedDate(new Date());
	    }
	    if (jobRoleJDBC.getModifiedDate() == null) {
	        jobRoleJDBC.setModifiedDate(new Date());
	    }
	    
	    return jobRoleDao.save(jobRoleJDBC);
	}

	@Override
	public JobRole updateJobRole(JobRole job, String title, String description) {
		job.setTitle(title);
		job.setDescription(description);

		return getRepo().save(job);
	}
	
	@Override
	public void updateJobRoleJdbc(JobRoleJDBC jobRoleJDBC) {	    
	    jobRoleJDBC.setModifiedDate(new Date());
	    
	    jobRoleDao.update(jobRoleJDBC);
	}
	
	@Override
	public JobRoleJDBC getJobRoleById(Long id) {
	    return jobRoleDao.findById(id);
	}

	@Override
	public void deleteJobRole(JobRole job) {
		if (job == null) {
			throw new IllegalArgumentException("JobRole must not be null.");
		}
		getRepo().delete(job);
	}
	
	@Override
	public void deleteJobRoleJdbc(JobRoleJDBC jobRoleJdbc) {
	    jobRoleDao.delete(jobRoleJdbc.getId());
	}

	@Override
	public Page<JobRole> getAllJobRole(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public boolean existsByTitle(String title) {
		return getRepo().existsByTitle(title);
	}

	@Override
	public boolean canEditByTitle(String title) {
		return getRepo().existsExactlyOneByTitle(title);
	}

	
}
