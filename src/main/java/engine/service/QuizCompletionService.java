package engine.service;

import engine.dataobject.Quiz;
import engine.dataobject.QuizCompletion;
import engine.dataobject.User;
import engine.repository.QuizCompletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


/**
 * Service class for providing methods for interaction with QuizCompletion objects.
 */
@Service
public class QuizCompletionService {

    /**
     * Providing CRUD methods for QuizCompletion objects.
     */
    private QuizCompletionRepository quizCompletionRepository;
    /**
     * providing methods for interaction with User objects
     */
    private UserService userService;

    /**
     * Removes QuizCompletions from database for specified quiz.
     *
     * @param quiz Quiz which records about completion should be removed.
     */
    public void deleteQuizCompletions(Quiz quiz) {
        quizCompletionRepository.deleteByQuiz(quiz);
    }

    /**
     * Creates record about completion of specified quiz and stores it into DB along with user from request
     * and current date/time.
     *
     * @param quiz               Solved quiz.
     * @param httpServletRequest HTTP request with which quiz was solved.
     */
    public void addQuizCompletion(Quiz quiz, HttpServletRequest httpServletRequest) {
        User user = userService.loadUserByUsername(httpServletRequest.getRemoteUser());
        quizCompletionRepository.save(new QuizCompletion(quiz, user, quiz.getId(), LocalDateTime.now()));
    }

    /**
     * Retrieves one page from all quiz completions stored in DB corresponding to page number.
     * Page size is 10 records.
     *
     * @param page               Page number for return.
     * @param httpServletRequest HTTP request with which quiz completions were requested.
     * @return One page from all quiz completions of current user.
     */
    public Page<QuizCompletion> getQuizCompletions(Integer page, HttpServletRequest httpServletRequest) {
        User user = userService.loadUserByUsername(httpServletRequest.getUserPrincipal().getName());
        return quizCompletionRepository.findAllByUserOrderByCompletedAtDesc(PageRequest.of(page, 10), user);
    }

    @Autowired
    public void setQuizCompletionRepository(QuizCompletionRepository quizCompletionRepository) {
        this.quizCompletionRepository = quizCompletionRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
