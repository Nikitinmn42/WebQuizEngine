package engine.repository;

import engine.dataobject.Quiz;
import engine.dataobject.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Interface for providing CRUD methods for User objects.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * @param username Username by which user is searched.
     * @return User from repository by its username.
     */
    User findByUsername(String username);

    /**
     * @param quiz Quiz  by which user is searched.
     * @return User from the repository by the quiz which he added.
     */
    User findByQuizzesContains(Quiz quiz);
}
