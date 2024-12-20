package mks.myworkspace.cvhub.repository;

import org.springframework.stereotype.Repository;
import mks.myworkspace.cvhub.entity.UsuallyQuestion;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UsuallyQuestionRepository extends JpaRepository<UsuallyQuestion, Long> {
    List<UsuallyQuestion> findAllByOrderByCreatedAtDesc();
    List<UsuallyQuestion> findAll();
    Page<UsuallyQuestion> findByQuestionContainingIgnoreCase(String keyword, Pageable pageable);
}
