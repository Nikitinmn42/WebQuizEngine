package engine.dataobject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Class represents quiz entity accepted by addQuiz method of WebQuizRestController and stored in DB using JPA.
 */
@Entity(name = "quiz")
@Component
public class Quiz {

    /**
     * Entity ID, primary key of the table, generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    @JsonProperty(access = Access.READ_ONLY)
    private int id;
    /**
     * Title of the quiz shortly describes what this quiz about.
     */
    @Column
    @NotBlank
    private String title;
    /**
     * Full description of the quiz - a question that the user must answer.
     */
    @Column
    @NotBlank
    private String text;
    /**
     * Ordered list of possible answers to quiz.
     */
    @Column
    @ElementCollection
    @NotNull
    @Size(min = 2)
    private List<String> options;
    /**
     * List of indexes of correct answers corresponding list of options.
     */
    @Column
    @ElementCollection
    @JsonProperty(access = Access.WRITE_ONLY)
    @Nullable
    private List<Integer> answer;


    public Quiz() {
    }

    public Quiz(String title, String text, ArrayList<String> options, ArrayList<Integer> answer) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quiz)) return false;
        Quiz quiz = (Quiz) o;
        return id == quiz.id &&
                Objects.equals(title, quiz.title) &&
                Objects.equals(text, quiz.text) &&
                Objects.equals(options, quiz.options) &&
                Objects.equals(answer, quiz.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text, options, answer);
    }
}
