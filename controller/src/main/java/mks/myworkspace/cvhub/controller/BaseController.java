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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sakaiproject.component.cover.ComponentManager;
import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.cvhub.logic.ProjectLogic;
import mks.myworkspace.cvhub.logic.SakaiProxy;

/**
 * Handles requests for the application home page.
 */
@Slf4j
public class BaseController {

    @Setter
    @Getter
    SakaiProxy sakaiProxy = null;

    @Setter
    @Getter
    ProjectLogic projectLogic;

    @Value("${theme.root}")
    String themeRoot;

    private static final String MIME_TYPE = "application/octet-stream";
    private static final String HEADER_KEY = "Content-Disposition";
    private static final String DEFAULT_USER_EID = "ThachLN";
    private static final String DEFAULT_SITE_ID = "DefaultSite";
    private static final String DEFAULT_USER_NAME = "Le Ngoc Thach";

    public String TMP_DIR = System.getProperty("java.io.tmpdir");

    /**
     * Store common data into session.
     * @param request Client HTTP request
     * @param httpSession Client HTTP session
     */
    void initSession(HttpServletRequest request, HttpSession httpSession) {
        // Set the theme root in the session.
        httpSession.setAttribute("themeRoot", themeRoot);

        if (sakaiProxy == null) {
            sakaiProxy = ComponentManager.get(SakaiProxy.class);
            log.debug("ComponentManager.get(SakaiProxy.class)={}", sakaiProxy);
        }

        // Initialize session attributes from Sakai or use default values.
        if (sakaiProxy != null) {
            setSessionAttributesFromSakai(httpSession);
        } else {
            setSessionAttributesForDemo(httpSession);
        }
    }

    private void setSessionAttributesFromSakai(HttpSession session) {
        session.setAttribute("currentSiteId", sakaiProxy.getCurrentSiteId());
        session.setAttribute("userDisplayName", sakaiProxy.getCurrentUserDisplayName());
        session.setAttribute("userEid", sakaiProxy.getCurrentUserEid());
        session.setAttribute("userEmail", sakaiProxy.getCurrentUserEmail());
        session.setAttribute("userFirstName", sakaiProxy.getCurrentUserFirstName());
        session.setAttribute("userLastName", sakaiProxy.getCurrentUserLastName());
    }

    private void setSessionAttributesForDemo(HttpSession session) {
        session.setAttribute("currentSiteId", DEFAULT_SITE_ID);
        session.setAttribute("userDisplayName", DEFAULT_USER_NAME);
        session.setAttribute("userEid", DEFAULT_USER_EID);
        session.setAttribute("userEmail", "LNThach@gmail.com");
        session.setAttribute("userFirstName", "Thach");
        session.setAttribute("userLastName", "Le Ngoc Thach");
    }

    public String getCurrentUserEid() {
        return sakaiProxy != null ? sakaiProxy.getCurrentUserEid() : DEFAULT_USER_EID;
    }

    public String getCurrentSiteId() {
        return sakaiProxy != null ? sakaiProxy.getCurrentSiteId() : DEFAULT_SITE_ID;
    }

    public String getCurrentUserEmail() {
        return sakaiProxy != null ? sakaiProxy.getCurrentUserEmail() : "lnthach@gmail.com";
    }

    public String getCurrentUserDisplayName() {
        return sakaiProxy != null ? sakaiProxy.getCurrentUserDisplayName() : DEFAULT_USER_NAME;
    }

    /**
     * Write the content of the file to HttpServletResponse with the given file name.
     * @param file The file to download.
     * @param response The HttpServletResponse object.
     * @param fileName The name of the file to be downloaded.
     * @throws IOException If an I/O error occurs.
     */
    protected void writeDownloadContent(File file, HttpServletResponse response, String fileName)
            throws IOException {
        try (InputStream fis = new FileInputStream(file)) {
            setResponseHeaders(response, fileName, file.length());
            writeDownloadContent(fis, response);
        }
    }

    /**
     * Write the content from the InputStream to HttpServletResponse.
     * @param is The InputStream containing the file content.
     * @param response The HttpServletResponse object.
     * @throws IOException If an I/O error occurs.
     */
    protected void writeDownloadContent(InputStream is, HttpServletResponse response)
            throws IOException {
        try (ServletOutputStream outStream = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int length;

            while ((length = is.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }
        } catch (IOException ex) {
            log.error("Error while writing content to the output stream.", ex);
            throw ex;
        }
    }

    /**
     * Set the response headers for file download.
     * @param response The HttpServletResponse object.
     * @param fileName The name of the file.
     * @param fileSize The size of the file.
     */
    private void setResponseHeaders(HttpServletResponse response, String fileName, long fileSize) {
        response.setContentType(MIME_TYPE);
        response.setContentLength((int) fileSize);
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.setHeader(HEADER_KEY, headerValue);
    }
}
