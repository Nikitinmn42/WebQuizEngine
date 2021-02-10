package engine.repository;

import engine.dataobject.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Interface for providing CRUD methods for Quiz objects.
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    /**
     * @param pageable Specifies page number and size of the page that should be returned.
     * @return All quizzes.
     */
    Page<Quiz> findAll(Pageable pageable);
}
