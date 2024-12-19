package mks.myworkspace.cvhub.service.impl;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mks.myworkspace.cvhub.entity.Comment;
import mks.myworkspace.cvhub.entity.Question;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.repository.CommentRepository;
import mks.myworkspace.cvhub.repository.QuestionRepository;
import mks.myworkspace.cvhub.service.ChatBoxService;

@Service
public class ChatBoxServiceImpl implements ChatBoxService {
	
	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private CommentRepository commentRepository;
	    
    @Override
	public List<Question> getAllQuestions() {
    	//List<Question> questions = questionRepository.findAllByOrderByCreatedAtDesc();
    	List<Question> questions = questionRepository.findAll();
        return questions;
        //return questionRepository.findAllByOrderByCreatedAtDesc();
    }
    
    @Override
	public List<Question> getAllQuestions_SortTimeDesc() {
    	List<Question> questions = questionRepository.findAllByOrderByCreatedAtDesc(); 
        return questions;
        //return questionRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
	public Question createQuestion(String title, String content, User author) {
        Question question = new Question();
        question.setTitle(title);
        question.setContent(content);
        question.setAuthor(author);
        question.setCreatedAt(LocalDateTime.now());
        
		/*
		 * // Ghi log System.out.println("Thêm câu hỏi mới: " + question.getTitle());
		 */
        
        return questionRepository.save(question);
    }
    

    @Override
	public Comment createComment(Long questionId, String content, User author) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new RuntimeException("Question not found"));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setQuestion(question);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
	public void likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setLikes(comment.getLikes() + 1);
        commentRepository.save(comment);
    }

	@Override
	@Transactional
	public Optional<Question> getQuestionById(Long id) {
	    return questionRepository.findById(id);
	}

	public void deleteComment(Long commentId, User currentUser) throws AccessDeniedException {
	    Comment comment = commentRepository.findById(commentId)
	        .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bình luận với ID: " + commentId));
	    if (!comment.getAuthor().getId().equals(currentUser.getId())) {
	        throw new AccessDeniedException("Không có quyền xóa bình luận này");
	    }
	    commentRepository.delete(comment);
	}

	public void editComment(Long commentId, String content, User currentUser) throws AccessDeniedException {
	    Comment comment = commentRepository.findById(commentId)
	        .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bình luận với ID: " + commentId));
	    if (!comment.getAuthor().getId().equals(currentUser.getId())) {
	        throw new AccessDeniedException("Không có quyền chỉnh sửa bình luận này");
	    }
	    comment.setContent(content);
	    commentRepository.save(comment);
	}

}
