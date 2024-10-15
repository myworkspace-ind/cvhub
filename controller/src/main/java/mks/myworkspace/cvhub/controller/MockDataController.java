package mks.myworkspace.cvhub.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.entity.JobRole;
import mks.myworkspace.cvhub.entity.Location;
import mks.myworkspace.cvhub.entity.Organization;
import mks.myworkspace.cvhub.service.JobRequestService;
import mks.myworkspace.cvhub.service.JobRoleService;
import mks.myworkspace.cvhub.service.LocationService;
import mks.myworkspace.cvhub.service.OrganizationService;

@Controller
public class MockDataController extends BaseController {
	@Autowired
	OrganizationService organizationService;
	@Autowired
	JobRoleService jobRoleService;
	@Autowired
	LocationService locationService;
	@Autowired
	JobRequestService jobRequestService;
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// Sample init of Custom Editor

//        Class<List<ItemKine>> collectionType = (Class<List<ItemKine>>)(Class<?>)List.class;
//        PropertyEditor orderNoteEditor = new MotionRuleEditor(collectionType);
//        binder.registerCustomEditor((Class<List<ItemKine>>)(Class<?>)List.class, orderNoteEditor);

	}
//	@RequestMapping(value = { "/add" }, method = RequestMethod.GET)
//	public ModelAndView addJobRoles() {
//		ModelAndView mav = new ModelAndView("searchResult");
//		List<Location> locations = locationService.getRepo().findAll();
//		List<JobRole> jobRole= jobRoleService.getRepo().findAll();
//		List<Organization> organization = organizationService.getRepo().findAll();
//		List<JobRequest> jobRequests = Arrays.asList(
//				new JobRequest(1L, "Java Developer", locations.get(0), jobRole.get(0),1, 100,organization.get(0)),
//				new JobRequest(2L, "Marketing Specialist", locations.get(0), jobRole.get(1), 2, 200,organization.get(1)),
//				new JobRequest(3L, "Financial Analyst", locations.get(0), jobRole.get(2), 3, 300,organization.get(2)),
//				new JobRequest(4L, "Registered Nurse", locations.get(0), jobRole.get(3), 4, 100,organization.get(3)),
//				new JobRequest(5L, "Civil Engineer", locations.get(0), jobRole.get(4), 5, 200,organization.get(4)),
//				new JobRequest(6L, "Software Engineer", locations.get(0), jobRole.get(0), 6, 300,organization.get(0)),
//				new JobRequest(7L, "Data Scientist", locations.get(0), jobRole.get(0),2, 100,organization.get(0)),
//				new JobRequest(8L, "Project Manager", locations.get(0), jobRole.get(4), 8, 200,organization.get(4)),
//				new JobRequest(9L, "Marketing Manager", locations.get(0), jobRole.get(1), 1, 300,organization.get(1)),
//				new JobRequest(10L, "Accountant", locations.get(0), jobRole.get(2), 2, 100,organization.get(2)),
//				new JobRequest(11L, "Teacher", locations.get(0), jobRole.get(5), 3, 200,organization.get(5)),
//				new JobRequest(12L, "Physician", locations.get(0), jobRole.get(5), 4, 300,organization.get(5)),
//				new JobRequest(13L, "Java Dev", locations.get(0), jobRole.get(0), 4, 100,organization.get(0)));
//		jobRequestService.getRepo().saveAll(jobRequests);
//		return mav;
//	}
	@RequestMapping(value = { "/addAll" }, method = RequestMethod.GET)
	public ModelAndView addLocation() {
		ModelAndView mav = new ModelAndView("searchResult");
		List<Location> locations = locationService.fetchLocationsFromAPI();

		locationService.getRepo().saveAll(locations);
		List<JobRole> jobRoles = Arrays.asList(
				new JobRole("IT", "Information Technology"),
				new JobRole("Marketing", "Marketing and Advertising"),
				new JobRole("Finance", "Financial Services"),
				new JobRole("Healthcare", "Health and Medicine"),
				new JobRole("Construction", "Construction and Engineering"),
				new JobRole("Education", "Education and Training"));
		jobRoleService.getRepo().saveAll(jobRoles);
		String image= "https://inkythuatso.com/uploads/images/2021/12/logo-hcmute-inkythuatso-17-13-52-06.jpg";
		List<Organization> organizations = Arrays.asList(
	            createOrganization("Tech Innovators Inc.",image,
	                    "https://techinnovators.com",
	                    "Leading the way in cutting-edge technology solutions",
	                    "Tech Innovators Inc. is a forward-thinking company specializing in AI, machine learning, and blockchain technologies. Founded in 2010, we've been at the forefront of technological advancements, serving clients across various industries.",
	                    "Silicon Valley, CA"),
	            createOrganization( "Global Marketing Solutions",image,
	                    "https://globalmarketingsolutions.com",
	                    "Your partner in comprehensive marketing strategies",
	                    "Global Marketing Solutions offers end-to-end marketing services, from brand development to digital campaigns. With a team of creative professionals and data analysts, we deliver results-driven marketing solutions for businesses of all sizes.",
	                    "New York City, NY"),
	            createOrganization( "Secure Financial Corp",image,
	                    "https://securefinancial.com",
	                    "Trusted financial services for a secure future",
	                    "Secure Financial Corp provides a wide range of financial services including investment management, retirement planning, and risk assessment. Our team of experienced professionals is committed to helping clients achieve their financial goals.",
	                    "Chicago, IL"),
	            createOrganization("Wellness Health Services",image,
	                    "https://wellnesshealthservices.com",
	                    "Comprehensive healthcare for mind and body",
	                    "Wellness Health Services is a holistic healthcare provider offering a range of medical and wellness services. From primary care to specialized treatments, we focus on improving the overall health and well-being of our patients.",
	                    "Los Angeles, CA"),
	            createOrganization("BuildRight Construction LLC",image,
	                    "https://buildright-construction.com",
	                    "Building dreams, one project at a time",
	                    "BuildRight Construction LLC is a full-service construction company specializing in residential and commercial projects. With a focus on quality craftsmanship and customer satisfaction, we bring our clients' visions to life.",
	                    "Denver, CO"),
	            createOrganization( "HCMUTE Education",image,
	                    "https://hcmute.edu.vn",
	                    "Shaping the future through quality education",
	                    "HCMUTE Education is a leading educational institution committed to providing high-quality education and fostering innovation. We offer a wide range of programs designed to prepare students for successful careers in various fields.",
	                    "Ho Chi Minh City, Vietnam")
	        );
	        organizationService.getRepo().saveAll(organizations);
		return mav;
	}
	public byte[] downloadImage(String imageUrl) throws IOException {
        try (InputStream in = new URL(imageUrl).openStream()) {
            return in.readAllBytes();
        }
    }

    private Organization createOrganization(String title, String logoUrl,String website, String summary, String detail,
			String location) {
        try {
            byte[] logo = downloadImage(logoUrl);
            UUID logoID = UUID.randomUUID(); // Tạo một UUID ngẫu nhiên
            return new  Organization( title,logoID,logo,website,summary,detail,location);
        } catch (IOException e) {
            e.printStackTrace();
            return new Organization(title, null, null, null, null, null, null); // Trả về tổ chức mà không có logo nếu xảy ra lỗi
        }
    }
}
