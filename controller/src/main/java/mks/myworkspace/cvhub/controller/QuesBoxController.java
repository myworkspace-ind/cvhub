/**
 * 
 */
package mks.myworkspace.cvhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.entity.UsuallyQuestion;
import mks.myworkspace.cvhub.service.QuesBoxService;
import mks.myworkspace.cvhub.service.UserService;
/**
 * 
 */
@Controller
public class QuesBoxController {
    @Autowired
    private QuesBoxService quesBoxService;
    
    @Autowired
    private UserService userService;
    
//    @RequestMapping(value = { "quesBox" }, method = RequestMethod.GET)
//	public ModelAndView returnquesBox() {
//		ModelAndView mav = new ModelAndView("quesBox/quesBox");
//		// Lấy danh sách câu hỏi từ dịch vụ
//        List<UsuallyQuestion> questions = quesBoxService.getAllQuestions_SortTimeDesc();
//        // Thêm danh sách câu hỏi vào model
//        mav.addObject("questions", questions);
//		return mav;
//	}
    
    @RequestMapping(value = { "quesBox" }, method = RequestMethod.GET)
    public ModelAndView returnquesBox(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "keyword", required = false) String keyword) {
        ModelAndView mav = new ModelAndView("quesBox/quesBox");

        // Lấy danh sách câu hỏi (phân trang và tìm kiếm)
        Page<UsuallyQuestion> questionPage;
        if (keyword != null && !keyword.isEmpty()) {
            questionPage = quesBoxService.searchQuestions(keyword, page);
            mav.addObject("keyword", keyword);
        } else {
            questionPage = quesBoxService.getAllQuestionsPaged(page);
        }

        // Thêm dữ liệu vào model
        mav.addObject("questions", questionPage.getContent());
        mav.addObject("pageNumber", questionPage.getNumber());
        mav.addObject("totalPages", questionPage.getTotalPages());
        return mav;
    }


    @PostMapping("/quesBox/postQuestion")
    public String postQuestion(@RequestParam String title, 
                               @RequestParam String content, 
                               Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());

        try {
            quesBoxService.createQuestion(title, content, currentUser);
            model.addAttribute("successMessage", "Câu hỏi đã được đăng thành công!");
        } catch (SecurityException e) {
            // Thêm thông báo lỗi vào model
            model.addAttribute("errorMessage", e.getMessage());
        }

        // Redirect về trang đầu tiên của danh sách câu hỏi
        return "redirect:/quesBox";
    }

}



//@GetMapping("/quesBox/detail/{id}")
//public ModelAndView getQuestionDetail(@PathVariable Long id) {
//  ModelAndView mav = new ModelAndView("quesBox/comment");
//  Optional<Question> question = quesBoxService.getQuestionById(id);
//
//  if (question.isPresent()) {
//      mav.addObject("question", question.get());
//      List<Comment> comments = commentRepository.findByQuestionIdOrderByCreatedAtAsc(question.get().getId());
//      mav.addObject("comments", comments);
//      
//  } else {
//      mav.setViewName("error");
//      mav.addObject("message", "Không tìm thấy câu hỏi với ID: " + id);
//  }
//
//  return mav;
//}
//
//@PostMapping("/quesBox/detail/{id}/postComment")
//public String postComment(@PathVariable Long id, @RequestParam String content) {
//  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//  User currentUser = userService.findUserByEmail(auth.getName());
//  quesBoxService.createComment(id, content, currentUser);
//  return "redirect:/quesBox/detail/" + id;
//}
//
//@PostMapping("/quesBox/detail/{questionId}/deleteComment/{commentId}")
//public String deleteComment(@PathVariable Long questionId, @PathVariable Long commentId) throws AccessDeniedException {
//  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//  User currentUser = userService.findUserByEmail(auth.getName());
//  quesBoxService.deleteComment(commentId, currentUser);
//  return "redirect:/quesBox/detail/" + questionId;
//}
//
//@PostMapping("/quesBox/detail/{questionId}/editComment/{commentId}")
//public String editComment(@PathVariable Long questionId, @PathVariable Long commentId, @RequestParam String content) throws AccessDeniedException {
//  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//  User currentUser = userService.findUserByEmail(auth.getName());
//  quesBoxService.editComment(commentId, content, currentUser);
//  return "redirect:/quesBox/detail/" + questionId;
//}
