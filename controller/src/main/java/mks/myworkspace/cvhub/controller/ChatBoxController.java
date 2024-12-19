/**
 * 
 */
package mks.myworkspace.cvhub.controller;

import java.nio.file.AccessDeniedException;
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

import mks.myworkspace.cvhub.entity.Comment;
import mks.myworkspace.cvhub.entity.Question;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.CommentRepository;
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
    
    @Autowired
    private CommentRepository commentRepository;
    
    @RequestMapping(value = { "chatBox" }, method = RequestMethod.GET)
	public ModelAndView returnChatBox() {
		ModelAndView mav = new ModelAndView("chatBox/chatBox");
		// Lấy danh sách câu hỏi từ dịch vụ
        List<Question> questions = chatBoxService.getAllQuestions_SortTimeDesc();
        // Thêm danh sách câu hỏi vào model
        mav.addObject("questions", questions);
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
        ModelAndView mav = new ModelAndView("chatBox/comment");
        Optional<Question> question = chatBoxService.getQuestionById(id);

        if (question.isPresent()) {
            mav.addObject("question", question.get());
            List<Comment> comments = commentRepository.findByQuestionIdOrderByCreatedAtAsc(question.get().getId());
            mav.addObject("comments", comments);
            
        } else {
            mav.setViewName("error");
            mav.addObject("message", "Không tìm thấy câu hỏi với ID: " + id);
        }

        return mav;
    }
   
    @PostMapping("/chatBox/detail/{id}/postComment")
    public String postComment(@PathVariable Long id, @RequestParam String content) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        chatBoxService.createComment(id, content, currentUser);
        return "redirect:/chatBox/detail/" + id;
    }

    @PostMapping("/chatBox/detail/{questionId}/deleteComment/{commentId}")
    public String deleteComment(@PathVariable Long questionId, @PathVariable Long commentId) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        chatBoxService.deleteComment(commentId, currentUser);
        return "redirect:/chatBox/detail/" + questionId;
    }

    @PostMapping("/chatBox/detail/{questionId}/editComment/{commentId}")
    public String editComment(@PathVariable Long questionId, @PathVariable Long commentId, @RequestParam String content) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByEmail(auth.getName());
        chatBoxService.editComment(commentId, content, currentUser);
        return "redirect:/chatBox/detail/" + questionId;
    }

//    @PostMapping("/chatBox/postComment/{questionId}")
//    public String postComment(@PathVariable Long questionId, @RequestParam String content) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = userService.findUserByEmail(auth.getName());
//        chatBoxService.createComment(questionId, content, currentUser);
//        return "redirect:/chatBox";
//    }

    @PostMapping("/chatBox/likeComment/{commentId}")
    public String likeComment(@PathVariable Long commentId) {
        chatBoxService.likeComment(commentId);
        return "redirect:/chatBox";
    }
}
