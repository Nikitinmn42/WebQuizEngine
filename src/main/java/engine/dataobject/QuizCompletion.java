package engine.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * Class represents record of successful solved quiz by specific user.
 */
@Entity
@Component
public class QuizCompletion {

    /**
     * Entity ID, primary key of the table, generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    @JsonIgnore
    private Integer id;
    /**
     * The quiz that was solved.
     */
    @JoinColumn
    @ManyToOne
    @JsonIgnore
    private Quiz quiz;
    /**
     * User that solved the quiz.
     */
    @JoinColumn
    @ManyToOne
    @JsonIgnore
    private User user;
    /**
     * ID of the quiz that was solved - need for JSON representation in response.
     */
    @Column
    @JsonProperty("id")
    private Integer quizID;
    /**
     * Date and time when quiz was solved.
     */
    @Column
    private LocalDateTime completedAt;


    public QuizCompletion() {
    }

    public QuizCompletion(Quiz quiz, User user, Integer quizID, LocalDateTime completedAt) {
        this.quiz = quiz;
        this.user = user;
        this.quizID = quizID;
        this.completedAt = completedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Integer getQuizID() {
        return quizID;
    }

    public void setQuizID(Integer quizId) {
        this.quizID = quizId;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuizCompletion)) return false;
        QuizCompletion that = (QuizCompletion) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(quiz, that.quiz) &&
                Objects.equals(user, that.user) &&
                Objects.equals(quizID, that.quizID) &&
                Objects.equals(completedAt, that.completedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quiz, user, quizID, completedAt);
    }
}
