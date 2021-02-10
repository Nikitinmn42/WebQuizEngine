package engine.dataobject;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * Class represents user on a server, used for authentication.
 */
@Entity
@Component
public class User implements UserDetails {

    /**
     * Regex pattern for validating email addresses.
     */
    @Transient
    private static final String emailPattern = "[-a-z0-9!#$%&'*+/=?^_`{|}~]+" +
            "(?:\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*" +
            "@(?:[a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(?:aero|arpa|asia|biz|cat|com|coop|edu|gov|" +
            "info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])";
    /**
     * Entity ID, primary key of the table, generated automatically.
     */
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;
    /**
     * Email address of user, also used for authentication, same as username.
     */
    @Column
    @Email(regexp = emailPattern)
    private String email;
    /**
     * User password for authentication.
     */
    @Column
    @NotNull
    @Size(min = 5, max = 255)
    private String password;
    /**
     * Set of all roles of this user, such as USER or ADMIN.
     */
    @JsonIgnore
    @Column
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Role> roles;
    /**
     * Name for authentication, same as email.
     */
    @JsonIgnore
    private String username;
    /**
     * List of quizzes that was created by user.
     */
    @JsonIgnore
    @Column
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Quiz> quizzes;

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        setUsername(email);
    }

    public void deleteQuiz(Quiz quiz) {
        quizzes.remove(quiz);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(roles, user.roles) &&
                Objects.equals(username, user.username) &&
                Objects.equals(quizzes, user.quizzes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, roles, username, quizzes);
    }
}
