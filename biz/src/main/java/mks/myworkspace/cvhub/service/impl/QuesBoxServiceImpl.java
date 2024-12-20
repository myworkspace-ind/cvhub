package mks.myworkspace.cvhub.service.impl;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import mks.myworkspace.cvhub.entity.User;
import mks.myworkspace.cvhub.entity.UsuallyQuestion;
import mks.myworkspace.cvhub.repository.UsuallyQuestionRepository;
import mks.myworkspace.cvhub.service.QuesBoxService;

@Service
public class QuesBoxServiceImpl implements QuesBoxService  {
	@Autowired
	private UsuallyQuestionRepository UquestionRepository;
	
	@Override
	public UsuallyQuestion createQuestion(String ques, String ans, User author) {
	    // Kiểm tra role của người dùng
	    if (!"ROLE_ADMIN".equals(author.getRole())) {
	        throw new SecurityException("Bạn không có quyền đăng câu hỏi.");
	    }

	    // Tạo câu hỏi mới nếu có quyền
	    UsuallyQuestion uquestion = new UsuallyQuestion();
	    uquestion.setQuestion(ques);
	    uquestion.setAnswer(ans);
	    uquestion.setCreatedAt(LocalDateTime.now());

	    return UquestionRepository.save(uquestion);
	}

	@Override
	public List<UsuallyQuestion> getAllQuestions() {
		List<UsuallyQuestion> questions = UquestionRepository.findAll();
        return questions;
	}

	@Override
	public List<UsuallyQuestion> getAllQuestions_SortTimeDesc() {
		List<UsuallyQuestion> questions = UquestionRepository.findAllByOrderByCreatedAtDesc(); 
        return questions;
	}

	@Override
	public Optional<UsuallyQuestion> getQuestionById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
	
	@Override
	public Page<UsuallyQuestion> getAllQuestionsPaged(int page) {
	    Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
	    return UquestionRepository.findAll(pageable);
	}

	@Override
	public Page<UsuallyQuestion> searchQuestions(String keyword, int page) {
	    Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
	    return UquestionRepository.findByQuestionContainingIgnoreCase(keyword, pageable);
	}
}
