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
	    
	    // Lưu jobRoleJDBC vào cơ sở dữ liệu thông qua JobRoleDao
	    return jobRoleDao.save(jobRoleJDBC);
	}	@Override
    public JobRoleJDBC createJobRoleJdbc(String title, String description) {
        if (title == null) {
            throw new IllegalArgumentException("Title must not be null or empty.");
        }
        JobRoleJDBC jobRole = new JobRoleJDBC(title, description);
        return jobRoleDao.save(jobRole);
    }

	@Override
	public JobRole updateJobRole(JobRole job, String title, String description) {
		job.setTitle(title);
		job.setDescription(description);

		return getRepo().save(job);
	}
	
	@Override
    public JobRoleJDBC updateJobRoleJdbc(JobRoleJDBC jobRole, String title, String description) {

        jobRole.setTitle(title);
        jobRole.setDescription(description);
        return jobRoleDao.save(jobRole);
    }

	@Override
	public void deleteJobRole(JobRole job) {
		if (job == null) {
			throw new IllegalArgumentException("JobRole must not be null.");
		}
		getRepo().delete(job);
	}
	
	@Override
    public void deleteJobRoleJdbc(Long id) {
        jobRoleDao.delete(id);
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
