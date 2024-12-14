package mks.myworkspace.cvhub.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import mks.myworkspace.cvhub.entity.FollowedOrganization;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface FollowedOrganizationService {
	void AddFollowedOrganization(User user, Organization organization);

	List<FollowedOrganization> getFollowedOrganizationByUser(User currentUser);
	boolean hasFollowedOrganization(User user, Long organization);
	List<FollowedOrganization> getFollowedOrganizationByOrganization(Organization organization);
	List<FollowedOrganization> findAll();
	Page<FollowedOrganization> findAll(PageRequest pageRequest);
	Page<FollowedOrganization> findByCreatedDateBetween(Date start, Date end, PageRequest pageRequest);
	List<FollowedOrganization> findByCreatedDateBetween(Date start, Date end);
	void deleteFollowedOrganizationById(Long id);
	FollowedOrganization getFollowedOrganizationByFollowedOrganizationId(Long id);
}
