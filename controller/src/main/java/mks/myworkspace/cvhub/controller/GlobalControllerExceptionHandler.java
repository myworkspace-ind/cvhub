/**
 * 
 */
package mks.myworkspace.cvhub.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * @see https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
 */

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		ModelAndView mav = new ModelAndView("error");
		
		log.error("Unknown error", e);
	    mav.addObject("exception", e);
	    mav.addObject("url", req.getRequestURL());

	    return mav;
	}
}
