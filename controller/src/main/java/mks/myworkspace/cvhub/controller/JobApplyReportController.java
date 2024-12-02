package mks.myworkspace.cvhub.controller;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.cvhub.entity.JobApplication;
import mks.myworkspace.cvhub.entity.JobRequest;
import mks.myworkspace.cvhub.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/jobapply")
@Slf4j
public class JobApplyReportController extends BaseController{
        @Autowired
        JobApplicationService jobApplication;

        @GetMapping("")
        public ModelAndView getAllJobApplication(@RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "limit", defaultValue = "10") int limit){
                ModelAndView mav = new ModelAndView("jobApplycationReport");
                PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdDate").descending());
                Page<JobApplication> jobApplicationPage = jobApplication.findAll(pageRequest);
                int totalPages = jobApplicationPage.getTotalPages();
                mav.addObject("jobAppList", jobApplicationPage);
                mav.addObject("totalPages", totalPages);
                mav.addObject("currentPage", page);
                return mav;
        }

        @GetMapping("/search")
        public ModelAndView getAllJobApplicationByFilter(@RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "limit", defaultValue = "1") int limit,
                                                         @RequestParam(value = "filter", defaultValue = "1") String filter){
                Date endDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDate);

                ModelAndView mav = new ModelAndView("jobApplycationReport");

                if (filter.equals("1")){
                        calendar.add(Calendar.DATE, -1);
                }
                else if (filter.equals("2")){
                        calendar.add(Calendar.WEEK_OF_YEAR, -1);
                }
                else{
                        calendar.add(Calendar.MONTH, -1);
                }

                Date startDate;
                startDate = calendar.getTime();

                PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdDate").descending());
                Page<JobApplication> jobApplicationPage = jobApplication.findByCreatedDateBetween(startDate, endDate, pageRequest);

                int totalPages = jobApplicationPage.getTotalPages();
                mav.addObject("jobAppList", jobApplicationPage);
                mav.addObject("totalPages", totalPages);
                mav.addObject("currentPage", page);
                mav.addObject("selectedFilter", filter);
                return mav;
        }
}
