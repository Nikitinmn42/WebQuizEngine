package engine.service;

import engine.dataobject.*;
import engine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


/**
 * Service class for providing methods for interaction with Quiz objects.
 */
@Service
public class QuizService {

    /**
     * Providing methods for interaction with QuizCompletion objects.
     */
    private QuizCompletionService quizCompletionService;
    /**
     * Providing methods for interaction with User objects
     */
    private UserService userService;
    /**
     * Result for the correct solved quiz.
     */
    private QuizResultCorrect resultCorrect;
    /**
     * Result for the wrong solved quiz.
     */
    private QuizResultWrong resultWrong;
    /**
     * Interface for providing CRUD methods for Quiz objects.
     */
    private QuizRepository quizRepository;

    /**
     * Saves quiz submitted by user into DB.
     *
     * @param quiz               Submitted quiz.
     * @param httpServletRequest HTTP request with which quiz was submitted.
     * @return Same quiz that was submitted but wit generated ID and without answers.
     */
    public Quiz addQuiz(Quiz quiz, HttpServletRequest httpServletRequest) {
        quizRepository.save(quiz);
        userService.addQuizToUser(quiz, httpServletRequest.getRemoteUser());
        return quiz;
    }

    /**
     * Removes quiz from DB. Quiz deletion allowed only by user who created this quiz. Also removes all records about
     * solving this quiz.
     *
     * @param id                 ID of the quiz that should be deleted.
     * @param httpServletRequest HTTP request with which quiz deletion was requested.
     * @return Response "204 No Content" if successfully deleted, "403 Forbidden" if quiz belongs to  another user
     * or "404 Not Found" if quiz cannot be found.
     */
    @Transactional
    public ResponseEntity<String> deleteQuiz(int id, HttpServletRequest httpServletRequest) {
        Quiz quiz = getQuizByIdOrThrow(id);
        if (userService.userHasQuiz(quiz, httpServletRequest)) {
            quizCompletionService.deleteQuizCompletions(quiz);
            userService.deleteQuizFromUser(quiz, httpServletRequest);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Retrieves quiz by its ID from DB.
     *
     * @param id ID of the quiz that should be returned.
     * @return Quiz from DB if present or response "404 Not Found" if not.
     */
    public Quiz getQuizByIdOrThrow(int id) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);
        if (optionalQuiz.isPresent()) {
            return optionalQuiz.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found.");
    }

    /**
     * Retrieves one page from all quizzes stored in DB corresponding to page number.
     * Page size is 10 records.
     *
     * @param page Page number for return.
     * @return One page from all quizzes stored in DB.
     */
    public Page<Quiz> getAllQuizzes(int page) {
        return quizRepository.findAll(PageRequest.of(page, 10));
    }

    /**
     * Accepts answer for quiz from user.
     *
     * @param answer             Answer that was submitted.
     * @param id                 ID of the quiz to which submitted answer.
     * @param httpServletRequest HTTP request with which answer was submitted.
     * @return If answer is correct QuizResultCorrect object and QuizResultWrong otherwise.
     * Returns "404 Not Found" if there's no quiz with provided ID.
     */
    public QuizResult answerQuiz(Answer answer, int id, HttpServletRequest httpServletRequest) {
        QuizResult quizResult;
        Quiz quiz = getQuizByIdOrThrow(id);
        List<Integer> savedAnswer = quiz.getAnswer();
        if (savedAnswer == null || answer.getAnswer() == null) {
            quizResult = savedAnswer == answer.getAnswer() ? resultCorrect : resultWrong;
        } else {
            quizResult = answer.getAnswer().equals(savedAnswer) ? resultCorrect : resultWrong;
        }
        if (quizResult.isSuccess()) {
            quizCompletionService.addQuizCompletion(quiz, httpServletRequest);
        }
        return quizResult;
    }

    @Autowired
    public void setQuizRepository(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setQuizCompletionService(QuizCompletionService quizCompletionService) {
        this.quizCompletionService = quizCompletionService;
    }

    @Autowired
    public void setResultCorrect(QuizResultCorrect resultCorrect) {
        this.resultCorrect = resultCorrect;
    }

    @Autowired
    public void setResultWrong(QuizResultWrong resultWrong) {
        this.resultWrong = resultWrong;
    }
}
