package mks.myworkspace.cvhub.service;

import java.util.List;

import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.entity.JobRequest;

@Service
public interface SearchJobService {
	 List<JobRequest> searchJobRequest(String keyword,int locationCD,Long industryCD);
}
