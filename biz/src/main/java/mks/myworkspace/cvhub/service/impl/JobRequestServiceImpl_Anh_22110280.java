package mks.myworkspace.cvhub.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.repository.JobRequestRepository_Anh_22110280;
import mks.myworkspace.cvhub.service.JobRequestService_Anh_22110280_themQLDSTinTuyenDungCuaMoiTaiKhoang;

public class JobRequestServiceImpl_Anh_22110280 implements JobRequestService_Anh_22110280_themQLDSTinTuyenDungCuaMoiTaiKhoang {
	private final JobRequestRepository_Anh_22110280 repo;

    public JobRequestServiceImpl_Anh_22110280(JobRequestRepository_Anh_22110280 repo) {
        this.repo = repo;
    }
    
//	@Override
//	public Page<JobRequest> findJobsByCriteria(Long organizationId, String searchTerm, String locationCode,
//			String sortBy, int page, int size) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	@Override
    public Page<JobRequest> findJobsByCriteria(Long organizationId, String searchTerm, String locationCode, String sortBy, int page, int size) {
        // Chuẩn bị Pageable
        Sort sort = Sort.unsorted();
        if ("date".equals(sortBy)) {
            sort = Sort.by("createdDate").descending();
        } else if ("title".equals(sortBy)) {
            sort = Sort.by("title").ascending();
        }
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // Xử lý searchTerm và locationCode
        // Giả sử: nếu searchTerm != null, tìm kiếm theo title;
        // Nếu locationCode != null, lọc theo locationCode;
        // Trường hợp phức tạp, bạn có thể dùng Specification hoặc @Query tùy chỉnh.

        // Ví dụ đơn giản (cần tùy chỉnh theo entity của bạn):
        // Giả sử JobRequest có trường 'location' là một entity liên kết, 
        // Bạn cần query dựa trên locationCode. 
        // Nếu locationCode là một trường mã location bên trong JobRequest, 
        // bạn cần thêm phương thức trong repository.

        // Giả sử bạn có query method như:
        // Page<JobRequest> findByOrganizationIdAndTitleContainingIgnoreCaseAndLocation_Code(
        //       Long orgId, String title, String locationCode, Pageable pageable);

        // Kiểm tra null
        String titleFilter = (searchTerm == null || searchTerm.isEmpty()) ? "" : searchTerm;

        if (locationCode != null && !locationCode.isEmpty()) {
            // Cần một phương thức repository hỗ trợ cả locationCode 
            // (Giả sử bạn có phương thức: findByOrganizationIdAndTitleContainingIgnoreCaseAndLocation_Code)
            return repo.findByOrganizationIdAndTitleContainingIgnoreCaseAndLocation_Code(
                    organizationId, titleFilter, locationCode, pageable
            );
        } else {
            // Nếu không lọc locationCode, dùng phương thức chỉ lọc title
            return repo.findByOrganizationIdAndTitleContainingIgnoreCase(
                    organizationId, titleFilter, pageable
            );
        }
    }
	


    @Override
    public JobRequestRepository_Anh_22110280 getRepo() {
        return repo;
    }

    @Override
    public void deleteJobRequest(JobRequest jobRequest) {
        repo.delete(jobRequest);
    }

    @Override
    public List<JobRequest> findAllByOrganizationId(Long id) {
        //return repo.findByOrganizationId(id);
    	return null;
    }

    

}
