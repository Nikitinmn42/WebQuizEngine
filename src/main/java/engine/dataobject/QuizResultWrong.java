package engine.dataobject;

import org.springframework.stereotype.Component;


/**
 * Result for the wrong solved quiz
 */
@Component
public class QuizResultWrong extends QuizResult {

    public QuizResultWrong() {
        super(false, "Sorry, your answer is wrong - try again!");
    }
}
