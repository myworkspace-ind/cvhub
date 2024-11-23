package mks.myworkspace.cvhub.service;

import mks.myworkspace.cvhub.entity.JobRequest;

import java.util.List;

public interface SearchJobService_tuan_22110450 {
 List<JobRequest> searchJobRequest(String keyword,int locationCD,Long industryCD, String sort, boolean search);
}
