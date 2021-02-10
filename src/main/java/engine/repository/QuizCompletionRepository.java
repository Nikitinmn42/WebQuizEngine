package engine.repository;

import engine.dataobject.Quiz;
import engine.dataobject.QuizCompletion;
import engine.dataobject.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Interface for providing CRUD methods for QuizCompletion objects.
 */
@Repository
public interface QuizCompletionRepository extends JpaRepository<QuizCompletion, Integer> {

    /**
     * @param pageable Specifies page number and size of the page that should be returned.
     * @param user     User by which filtered quiz completions.
     * @return All quiz completions by specified user, sorted by time of completion starting from the most recent.
     */
    Page<QuizCompletion> findAllByUserOrderByCompletedAtDesc(Pageable pageable, User user);

    /**
     * @param quiz Removes from the repository all quiz completions of the specified quiz.
     */
    void deleteByQuiz(Quiz quiz);
}
