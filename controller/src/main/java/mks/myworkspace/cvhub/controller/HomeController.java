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

package mks.myworkspace.cvhub.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles requests for the application home page.
 */
@Controller
@Slf4j
public class HomeController extends BaseController {
    
    /**
     * This method is called when binding the HTTP parameter to bean (or model).
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // Sample init of Custom Editor
//        Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//        PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//        binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);
        log.debug("Initializing binder for custom property editor");
    }

    /**
     * Simply selects the home view to render by returning its name.
     * @return ModelAndView
     */
    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public ModelAndView displayHome(HttpServletRequest request, HttpSession httpSession) {
        ModelAndView mav = new ModelAndView("home");

        // Initialize session attributes
        initSession(request, httpSession);

        // Add current user details to the model
        mav.addObject("currentSiteId", getCurrentSiteId());
        mav.addObject("userDisplayName", getCurrentUserDisplayName());

        logUserDetails();  // Log current user details for tracing purposes

        return mav;
    }

    /**
     * Logs user details like display name, site ID, and email.
     */
    private void logUserDetails() {
        log.info("User Details: ");
        log.info("User Display Name: {}", getCurrentUserDisplayName());
        log.info("Current Site ID: {}", getCurrentSiteId());
        log.info("User Email: {}", getCurrentUserEmail());
    }

    /**
     * Updates a session attribute with new user information, e.g., after updating profile.
     * @param session the current session
     * @param key the session attribute key
     * @param value the new value for the session attribute
     */
    public void updateSessionAttribute(HttpSession session, String key, String value) {
        if (key != null && value != null) {
            session.setAttribute(key, value);
            log.info("Updated session attribute: {} = {}", key, value);
        } else {
            log.error("Attempted to update session with null key or value");
        }
    }

    /**
     * Checks if a user has a specific role or permission.
     * @param role the role to check (e.g., "admin")
     * @return true if the user has the role, otherwise false
     */
    public boolean hasRole(String role) {
        // Placeholder logic for role check, replace with actual role check logic
        String currentRole = (String) getSessionAttribute("userRole");
        return currentRole != null && currentRole.equals(role);
    }

    /**
     * Helper method to retrieve session attributes.
     * @param key the session attribute key
     * @return the value of the session attribute, or null if not found
     */
    private Object getSessionAttribute(String key) {
        HttpSession session = getSession();
        return session != null ? session.getAttribute(key) : null;
    }

    /**
     * Returns the current session or creates a new one if not present.
     * @return HttpSession
     */
    private HttpSession getSession() {
        // Get current session if available, or create a new one if necessary
        return request.getSession(true);
    }

    /**
     * Example method for handling user logout.
     * @param request the HTTP request object
     * @param response the HTTP response object
     * @throws Exception
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void handleLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            log.info("User logged out successfully.");
        }
        response.sendRedirect("/home");
    }
}
