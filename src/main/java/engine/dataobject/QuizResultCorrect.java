package engine.dataobject;

import org.springframework.stereotype.Component;


/**
 * Result for the correct solved quiz.
 */
@Component
public class QuizResultCorrect extends QuizResult {

    public QuizResultCorrect() {
        super(true, "Congratulations, you solved the quiz correctly!");
    }
}
