package mks.myworkspace.cvhub.repository;

import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRequestRepository_tuan_22110450 extends JpaRepository<JobRequest, Long> {
//    @Query("SELECT j FROM JobRequest j "
//            + "JOIN j.organization c WHERE "
//            + "( :locationCD IS NULL OR j.location.code = :locationCD or 1=1) AND "
//            + "( :industryCD IS NULL OR j.jobRole.id = :industryCD) AND "
//            + "( :company IS NULL OR c.title LIKE %:company%) AND "
//            + "( :jobName IS NULL OR j.title LIKE %:jobName%)")
//    List<JobRequest> findBySearchCriteria(
//            @Param("locationCD") int locationCD,
//            @Param("industryCD") Long industryCD,
//            @Param("company") String company,
//            @Param("jobName") String jobName,
//            Sort sort);

//    @Query("SELECT j FROM JobRequest j "
//            + " WHERE "
//            + "( :locationCD IS NULL OR :locationCD <= 0 OR j.location.code = :locationCD) AND "
//            + "( :industryCD IS NULL OR :industryCD <= 0 OR j.jobRole.id = :industryCD) AND "
//            + "( :company IS NULL OR j.organization.title LIKE %:company% OR :bool is false) AND "
//            + "( :jobName IS NULL OR j.title LIKE %:jobName%) OR :bool is true")
//    List<JobRequest> findBySearchCriteria(
//            @Param("locationCD") int locationCD,
//            @Param("industryCD") Long industryCD,
//            @Param("company") String company,
//            @Param("jobName") String jobName);

    @Query("SELECT j FROM JobRequest j "
            + " WHERE "
            + "( :locationCD IS NULL OR :locationCD <= 0 OR j.location.code = :locationCD) AND "
            + "( :industryCD IS NULL OR :industryCD <= 0L OR j.jobRole.id = :industryCD) AND "
            + "( :company IS NULL OR j.organization.title LIKE %:company% OR :bool is false) AND "
            + "( :jobName IS NULL OR j.title LIKE %:jobName% OR :bool is true)")
    List<JobRequest> findBySearchCriteria(
            @Param("locationCD") int locationCD,
            @Param("industryCD") Long industryCD,
            @Param("company") String company,
            @Param("jobName") String jobName,
            @Param("bool") boolean search,
            Sort sort);
}
