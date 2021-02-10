package engine.service;

import engine.dataobject.Quiz;
import engine.dataobject.Role;
import engine.dataobject.User;
import engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Collections;


/**
 * Service class for providing methods for interaction with User objects.
 */
@Service
public class UserService implements UserDetailsService {

    /**
     * Providing CRUD methods for User objects.
     */
    private UserRepository userRepository;
    /**
     * Encoder for storing users password securely.
     */
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Retrieves User from DB by its name.
     *
     * @throws UsernameNotFoundException If an userRepository cannot locate a user by its username.
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    /**
     * Stores into DB newly registered user with its password and role.
     *
     * @param user User to be stored into DB.
     * @return True if stored successfully and false if this user already exist.
     */
    public boolean addUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        user.setId(0);
        user.setRoles(Collections.singleton(new Role("ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    /**
     * Removes quiz quiz from users list of his own quizzes.
     *
     * @param quiz               Quiz that should be removed.
     * @param httpServletRequest HTTP request with which quiz deletion was requested.
     */
    @Transactional
    public void deleteQuizFromUser(Quiz quiz, HttpServletRequest httpServletRequest) {
        if (userHasQuiz(quiz, httpServletRequest)) {
            loadUserByUsername(httpServletRequest.getRemoteUser()).deleteQuiz(quiz);
            userRepository.flush();
        }
    }

    /**
     * Checks if user has provided quiz in his list.
     *
     * @param quiz               Quiz for checking.
     * @param httpServletRequest HTTP request with with information about user.
     * @return True if user has provided quiz and false otherwise.
     */
    public boolean userHasQuiz(Quiz quiz, HttpServletRequest httpServletRequest) {
        String currentUserName = httpServletRequest.getRemoteUser();
        User user = userRepository.findByQuizzesContains(quiz);
        return currentUserName.equals(user.getUsername());
    }

    /**
     * Adds provided quiz to users list.
     *
     * @param quiz     Quiz that should be added.
     * @param username Name of user to which quiz should be added.
     */
    @Transactional
    public void addQuizToUser(Quiz quiz, String username) {
        loadUserByUsername(username).addQuiz(quiz);
        userRepository.flush();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
}
