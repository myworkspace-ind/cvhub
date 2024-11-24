package mks.myworkspace.cvhub.service;

import java.util.List;

import mks.myworkspace.cvhub.entity.JobRequest;

public interface SearchJobService {
 List<JobRequest> searchJobRequest(String keyword,int locationCD,Long industryCD);
}
