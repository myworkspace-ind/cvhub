package mks.myworkspace.cvhub.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.repository.OrganizationRepository;
import mks.myworkspace.cvhub.service.OrganizationExportExcelService;
import mks.myworkspace.cvhub.service.OrganizationService;

@Controller
public class ExportEmployerController extends BaseController {
	@Autowired
    OrganizationRepository organizationRepository;
	
    @Autowired
    OrganizationService organizationService;
    
    @Autowired
    OrganizationExportExcelService exportService;

    
    @GetMapping("/export-employers")
    public void exportEmployers(HttpServletResponse response, HttpSession httpSession, @RequestParam("keyword") String keyword) {
        try {
            List<Organization> organizations;
            
            if (keyword != null && !keyword.isEmpty()) {
                organizations = organizationService.findByTitleContaining(keyword);
            } else {
                organizations = organizationService.getRepo().findAll();
            }

            if (organizations == null || organizations.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().write("No companies found for the given search.");
                return;
            }

            ByteArrayOutputStream excelFile = exportService.createExcelFile(organizations);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=organizations.xlsx");
            response.getOutputStream().write(excelFile.toByteArray());
            response.getOutputStream().flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("Error while generating the Excel file.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
