/**
 * Licensed to MKS Group under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * MKS Group licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package mks.myworkspace.cvhub.logic;

import java.util.List;

import org.sakaiproject.authz.api.AuthzGroup;
import org.sakaiproject.authz.api.AuthzGroupService;
import org.sakaiproject.authz.api.GroupNotDefinedException;
import org.sakaiproject.authz.api.Member;
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.event.api.EventTrackingService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.site.util.SiteParticipantHelper;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.ToolManager;
import org.sakaiproject.user.api.UserDirectoryService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link SakaiProxy}
 * 
 * @author Steve Swinsburg (steve.swinsburg@anu.edu.au), Thach Ngoc Le (ThachLN@mks.com.vn)
 *
 */
@Slf4j
public class SakaiProxyImpl implements SakaiProxy {

    @Getter @Setter
    private ToolManager toolManager;
    
    @Getter @Setter
    private SessionManager sessionManager;
    
    @Getter @Setter
    private UserDirectoryService userDirectoryService;
    
    @Getter @Setter
    private SecurityService securityService;
    
    @Getter @Setter
    private EventTrackingService eventTrackingService;
    
    @Getter @Setter
    private ServerConfigurationService serverConfigurationService;
    
    @Getter @Setter
    private SiteService siteService;

    @Getter @Setter
    private AuthzGroupService authzGroupService = null;

    // Retrieve the current site ID
    @Override
    public String getCurrentSiteId() {
        return toolManager.getCurrentPlacement().getContext();
    }
    
    // Retrieve the current user's ID
    @Override
    public String getCurrentUserId() {
        return sessionManager.getCurrentSessionUserId();
    }
    
    // Retrieve the current user's display name
    @Override
    public String getCurrentUserDisplayName() {
        return userDirectoryService.getCurrentUser().getDisplayName();
    }

    // Retrieve the current user's EID
    @Override
    public String getCurrentUserEid() {
        return userDirectoryService.getCurrentUser().getEid();
    }

    // Retrieve the current user's email
    @Override
    public String getCurrentUserEmail() {
        return userDirectoryService.getCurrentUser().getEmail();
    }

    // Retrieve the current user's first name
    @Override
    public String getCurrentUserFirstName() {
        return userDirectoryService.getCurrentUser().getFirstName();
    }

    // Retrieve the current user's last name
    @Override
    public String getCurrentUserLastName() {
        return userDirectoryService.getCurrentUser().getLastName();
    }

    // Check if the current user is a superuser
    @Override
    public boolean isSuperUser() {
        return securityService.isSuperUser();
    }
    
    // Post an event with tracking information
    @Override
    public void postEvent(String event, String reference, boolean modify) {
        eventTrackingService.post(eventTrackingService.newEvent(event, reference, modify));
    }
    
    // Get skin repository property from the server configuration
    @Override
    public String getSkinRepoProperty() {
        return serverConfigurationService.getString("skin.repo");
    }
    
    // Get the tool CSS path for the current skin
    @Override
    public String getToolSkinCSS(String skinRepo) {
        String skin = siteService.findTool(sessionManager.getCurrentToolSession().getPlacementId()).getSkin();          
        if (skin == null) {
            skin = serverConfigurationService.getString("skin.default");
        }
        return skinRepo + "/" + skin + "/tool.css";
    }
    
    // Perform initialization tasks when the bean starts up
    @Override
    public void init() {
        log.info("SakaiProxy bean initialized.");
    }
    
    /**
     * Check if the current user role is swapped.
     * 
     * @return true if the role is swapped, otherwise false
     */
    @Override
    public boolean isUserRoleSwapped() {
        try {
            return securityService.isUserRoleSwapped();
        } catch (IdUnusedException e) {
            log.error("Could not call method isUserRoleSwapped()", e);
        }
        return false;
    }

    /**
     * Get the role of the current user in the current site.
     * 
     * @return the role of the current user, or null if not found
     */
    @Override
    public String getUserRole() {
        String userId = userDirectoryService.getCurrentUser().getId();
        try {
            String siteId = getCurrentSiteId();
            AuthzGroup realm = authzGroupService.getAuthzGroup(siteService.siteReference(siteId));
            Member member = realm.getMember(userId);
            return member.getRole() != null ? member.getRole().getId() : null;
        } catch (GroupNotDefinedException e) {
            log.error("Could not get user role for user: {} in site: {}", userId, getCurrentSiteId(), e);
        } catch (IdUnusedException e) {
            log.error("Invalid site ID: {}", getCurrentSiteId(), e);
        }
        return null;
    }

    /**
     * Get a list of roles for the current site.
     * 
     * @return a list of roles available in the current site, or empty if not available
     */
    @Override
    public List<Role> getRoles() {
        List<Role> roles = null;
        try {
            String siteId = getCurrentSiteId();
            Site site = siteService.getSite(siteId);
            AuthzGroup realm = authzGroupService.getAuthzGroup(siteService.siteReference(siteId));
            roles = SiteParticipantHelper.getAllowedRoles(site.getType(), realm.getRoles());
        } catch (IdUnusedException | GroupNotDefinedException e) {
            log.error("Error retrieving roles for site: {}", getCurrentSiteId(), e);
        }
        
        if (roles != null) {
            log.debug("Found {} roles in site: {}", roles.size(), getCurrentSiteId());
            for (Role role : roles) {
                log.debug("Role ID: {}", role.getId());
            }
        }
        return roles != null ? roles : List.of();
    }
}
