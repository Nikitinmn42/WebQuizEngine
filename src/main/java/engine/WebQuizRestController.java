package engine;

import engine.dataobject.*;
import engine.service.QuizCompletionService;
import engine.service.QuizService;
import engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * Controller class for processing REST requests.
 */
@RestController
public class WebQuizRestController {

    /**
     * Providing methods for interaction with User objects
     */
    private UserService userService;
    /**
     * Providing methods for interaction with Quiz objects
     */
    private QuizService quizService;
    /**
     * providing methods for interaction with QuizCompletion objects.
     */
    private QuizCompletionService quizCompletionService;


    /**
     * Handles HTTP POST request to /api/quizzes. Adds quiz submitted by user.
     *
     * @param quiz               Submitted quiz.
     * @param httpServletRequest HTTP request with which quiz was submitted.
     * @return Same quiz that was submitted but wit generated ID and without answers.
     */
    @PostMapping(path = "/api/quizzes")
    public Quiz addQuiz(@Valid @RequestBody Quiz quiz, HttpServletRequest httpServletRequest) {
        return quizService.addQuiz(quiz, httpServletRequest);
    }

    /**
     * Handles HTTP GET request to /api/quizzes/{id}. Retrieves quiz by its ID.
     *
     * @param id ID of the quiz that requested.
     * @return Quiz from DB if present or response "404 Not Found" if not.
     */
    @GetMapping(path = "/api/quizzes/{id}")
    public Quiz getQuizById(@PathVariable int id) {
        return quizService.getQuizByIdOrThrow(id);
    }

    /**
     * Handles HTTP GET request to /api/quizzes.
     * Retrieves one page from all quizzes stored in DB corresponding to page number.
     *
     * @param page Page number for return.
     * @return One page from all quizzes stored in DB.
     */
    @GetMapping(path = "/api/quizzes")
    public Page<Quiz> getAllQuizzes(@RequestParam int page) {
        return quizService.getAllQuizzes(page);
    }

    /**
     * Handles HTTP POST request to /api/quizzes/{id}/solve. Accepts answer for quiz from user.
     *
     * @param answer             Answer that was submitted.
     * @param id                 ID of the quiz to which submitted answer.
     * @param httpServletRequest HTTP request with which answer was submitted.
     * @return If answer is correct QuizResultCorrect object and QuizResultWrong otherwise.
     * Returns "404 Not Found" if there's no quiz with provided ID.
     */
    @PostMapping(path = "/api/quizzes/{id}/solve")
    public QuizResult answerQuiz(@Valid @RequestBody Answer answer, @PathVariable int id,
                                 HttpServletRequest httpServletRequest) {
        return quizService.answerQuiz(answer, id, httpServletRequest);
    }

    /**
     * Handles HTTP DELETE request to /api/quizzes/{id}. Removes quiz from DB. Quiz deletion allowed only by user
     * who created this quiz. Also removes all records about solving this quiz.
     *
     * @param id                 ID of the quiz that should be deleted.
     * @param httpServletRequest HTTP request with which quiz deletion was requested.
     * @return Response "204 No Content" if successfully deleted, "403 Forbidden" if quiz belongs to  another user
     * or "404 Not Found" if quiz cannot be found.
     */
    @DeleteMapping(path = "/api/quizzes/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable int id, HttpServletRequest httpServletRequest) {
        return quizService.deleteQuiz(id, httpServletRequest);
    }

    /**
     * Handles HTTP GET request to /api/quizzes/completed. Retrieves one page from all quiz completions stored
     * in DB corresponding to page number.
     *
     * @param page               Page number for return.
     * @param httpServletRequest HTTP request with which quiz completions were requested.
     * @return One page from all quiz completions of current user.
     */
    @GetMapping(path = "/api/quizzes/completed")
    public Page<QuizCompletion> getQuizCompletions(@RequestParam Integer page, HttpServletRequest httpServletRequest) {
        return quizCompletionService.getQuizCompletions(page, httpServletRequest);
    }

    /**
     * Handles HTTP POST request to /api/register. Saves new user. If such user already exist
     * throws ResponseStatusException.
     *
     * @param newUser User provided for adding.
     */
    @PostMapping(path = "/api/register")
    public void registerNewUser(@Valid @RequestBody User newUser) {
        if (!userService.addUser(newUser)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("User %s is already registered.", newUser.getUsername()));
        }
    }

    @Autowired
    public void setQuizCompletionService(QuizCompletionService quizCompletionService) {
        this.quizCompletionService = quizCompletionService;
    }

    @Autowired
    public void setQuizService(QuizService quizService) {
        this.quizService = quizService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
