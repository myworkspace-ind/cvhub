
package mks.myworkspace.cvhub.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mks.myworkspace.cvhub.entity.FollowedOrganization;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.FollowedOrganizationRepository;
import mks.myworkspace.cvhub.repository.OrganizationRepository;
import mks.myworkspace.cvhub.repository.OrganizationReviewRepository;
import mks.myworkspace.cvhub.service.FollowedOrganizationService;

@Service
@RequiredArgsConstructor
public class FollowedOrganizationImpl implements FollowedOrganizationService{

	private final FollowedOrganizationRepository repo;
	@Override
	public void AddFollowedOrganization(User user, Organization organization) {
		FollowedOrganization follow = new FollowedOrganization();
		follow.setOrganization(organization);

		follow.setUser(user);
        repo.save(follow);
	}
	@Override
	public List<FollowedOrganization> getFollowedOrganizationByUser(User currentUser) {
		// TODO Auto-generated method stub
		return repo.findByUser(currentUser);
	}
	@Override
	public boolean hasFollowedOrganization(User user, Long organization) {
		List<FollowedOrganization> follow = getFollowedOrganizationByUser(user);
	    return follow.stream().anyMatch(app -> app.getOrganization().getId().equals(organization));
	}
	@Override
	public List<FollowedOrganization> getFollowedOrganizationByOrganization(Organization organization) {
		return repo.findByUserAndOrganization(organization);
	}
	@Override
	public List<FollowedOrganization> findAll() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	@Override
	public Page<FollowedOrganization> findAll(PageRequest pageRequest) {
		return repo.findAll(pageRequest);
	}

	@Override
	public Page<FollowedOrganization> findByCreatedDateBetween(Date start, Date end, PageRequest pageRequest) {
		return repo.findByCreatedDateBetween(start, end, pageRequest);
	}

	@Override
	public List<FollowedOrganization> findByCreatedDateBetween(Date start, Date end) {
		return repo.findByCreatedDateBetween(start, end);
	}

	@Override
	public void deleteFollowedOrganizationById(Long id) {
		repo.deleteById(id);
		
	}
	@Override
	public FollowedOrganization getFollowedOrganizationByFollowedOrganizationId(Long id) {
		// TODO Auto-generated method stub
		return repo.findByFollowedOrganizationId(id).get();
	}
}

