package mks.myworkspace.cvhub.repository;

import org.springframework.stereotype.Repository;
import mks.myworkspace.cvhub.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByQuestionIdOrderByCreatedAtAsc(Long questionId);
}
