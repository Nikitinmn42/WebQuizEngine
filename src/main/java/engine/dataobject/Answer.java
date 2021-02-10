package engine.dataobject;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Class represents the answer for quiz which accepts by answerQuiz method of WebQuizRestController.
 */
@Component
public class Answer {

    /**
     * List of indexes of correct answers in list of options for quiz.
     */
    private List<Integer> answer;

    public Answer() {
    }

    public Answer(ArrayList<Integer> answer) {
        this.answer = answer;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;
        Answer answer1 = (Answer) o;
        return Objects.equals(answer, answer1.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer);
    }
}
