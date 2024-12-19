/**
 * 
 */
package mks.myworkspace.cvhub.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mks.myworkspace.cvhub.entity.Question;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.service.ChatBoxService;
import mks.myworkspace.cvhub.service.UserService;

/**
 * 
 */
@Controller
public class ChatBoxController {
    @Autowired
    private ChatBoxService chatBoxService;

    @Autowired
    private UserService userService;
    
    @RequestMapping(value = { "chatBox" }, method = RequestMethod.GET)
	public ModelAndView returnChatBox() {
		ModelAndView mav = new ModelAndView("chatBox/chatBox");
		// Lấy danh sách câu hỏi từ dịch vụ
        List<Question> questions = chatBoxService.getAllQuestions_SortTimeDesc();
        // Thêm danh sách câu hỏi vào model
        mav.addObject("questions", questions);
        
        System.out.println("Tổng số câu hỏi: " + questions.size());
        for (Question question : questions) {
            System.out.println("Câu hỏi: " + question.getTitle());
        }
		return mav;
	}

    @PostMapping("/chatBox/postQuestion")
    public String postQuestion(@RequestParam String title, @RequestParam String content) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        chatBoxService.createQuestion(title, content, currentUser);
        return "redirect:/chatBox";
    }
    
    @GetMapping("/chatBox/detail/{id}")
    public ModelAndView getQuestionDetail(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView();
        Optional<Question> question = chatBoxService.getQuestionById(id);

        if (question.isPresent()) {
            mav.setViewName("chatBox/comment");
            mav.addObject("question", question.get());
        } else {
            mav.setViewName("error"); // Chuyển sang trang lỗi nếu không tìm thấy
            mav.addObject("message", "Không tìm thấy câu hỏi với ID: " + id);
        }

        return mav;
    }

    @PostMapping("/chatBox/postComment/{questionId}")
    public String postComment(@PathVariable Long questionId, @RequestParam String content) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        chatBoxService.createComment(questionId, content, currentUser);
        return "redirect:/chatBox";
    }

    @PostMapping("/chatBox/likeComment/{commentId}")
    public String likeComment(@PathVariable Long commentId) {
        chatBoxService.likeComment(commentId);
        return "redirect:/chatBox";
    }
}
