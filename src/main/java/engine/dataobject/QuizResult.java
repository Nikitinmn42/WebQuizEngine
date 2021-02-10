package engine.dataobject;

import java.util.Objects;


/**
 * Class provides abstract representation of result of solving quiz and common functionality for subclasses.
 */
public abstract class QuizResult {

    /**
     * Correctness of solving - true if correct and false otherwise.
     */
    private boolean success;
    /**
     * Text feedback to the user depending on what was answer correct or not.
     */
    private String feedback;

    public QuizResult() {
    }

    public QuizResult(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuizResult)) return false;
        QuizResult that = (QuizResult) o;
        return success == that.success &&
                Objects.equals(feedback, that.feedback);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, feedback);
    }
}
