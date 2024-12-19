package mks.myworkspace.cvhub.service;

import java.util.List;
import java.util.Optional;

import mks.myworkspace.cvhub.entity.Comment;
import mks.myworkspace.cvhub.entity.Question;
import mks.myworkspace.cvhub.entity.User;

public interface ChatBoxService {

	void likeComment(Long commentId);

	Comment createComment(Long questionId, String content, User author);

	Question createQuestion(String title, String content, User author);

	List<Question> getAllQuestions();
	List<Question> getAllQuestions_SortTimeDesc();

	Optional<Question> getQuestionById(Long id);

}
