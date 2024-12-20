package mks.myworkspace.cvhub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import mks.myworkspace.cvhub.entity.Question;
import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.entity.UsuallyQuestion;

public interface QuesBoxService {

	UsuallyQuestion createQuestion(String title, String content, User author);
	
	List<UsuallyQuestion> getAllQuestions();
	List<UsuallyQuestion> getAllQuestions_SortTimeDesc();

	Optional<UsuallyQuestion> getQuestionById(Long id);
	
	Page<UsuallyQuestion> getAllQuestionsPaged(int page);
	Page<UsuallyQuestion> searchQuestions(String keyword, int page);
}
