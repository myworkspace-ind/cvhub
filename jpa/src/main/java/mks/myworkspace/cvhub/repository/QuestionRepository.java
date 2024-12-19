package mks.myworkspace.cvhub.repository;

import org.springframework.stereotype.Repository;
import mks.myworkspace.cvhub.entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByOrderByCreatedAtDesc();
    List<Question> findAll();
}
