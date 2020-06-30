package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.club.exception.EntityAlreadyExistsException;
import ru.club.exception.EntityNotFoundException;
import ru.club.forms.SignUpForm;
import ru.club.models.Role;
import ru.club.models.State;
import ru.club.models.User;
import ru.club.repositories.UsersRepository;
import ru.club.transfer.UserDto;

import java.util.Optional;
import java.util.UUID;

@Service
public class SignUpService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailSender mailSender;

    private static final boolean EMAIL_SIGNUP_ENABLED = true;


    /**
     * Saves new user to database
     * @param signUpForm - contains diverse parameters of user profile
     * @return id of new user
     * @throws EntityAlreadyExistsException - if user with this login already exists
     */
    public Long signUp(SignUpForm signUpForm) {
        if (EMAIL_SIGNUP_ENABLED) {
            return this.emailSignUp(signUpForm);
        } else {
            return this.regularSignUp(signUpForm);
        }
    }

    private Long regularSignUp(SignUpForm signUpForm) {

        if (!usersRepository.findOneByLogin(signUpForm.getLogin().toLowerCase()).isPresent() ||
                !usersRepository.findOneByEmail(signUpForm.getEmail().toLowerCase()).isPresent()) {
            User user = User.builder()
                    .login(signUpForm.getLogin().toLowerCase())
                    .firstName(signUpForm.getFirstName())
                    .lastName(signUpForm.getLastName())
                    .hashPassword(passwordEncoder.encode(signUpForm.getPassword()))
                    .role(Role.USER)
                    .email(signUpForm.getEmail().toLowerCase())
                    .state(State.ACTIVE)
                    .build();
            usersRepository.save(user);

            return usersRepository.findOneByLogin(signUpForm.getLogin().toLowerCase()).get().getId();
        }

        throw new EntityAlreadyExistsException("User with this login already exists");
    }

    private Long emailSignUp(SignUpForm signUpForm) {
        if (!usersRepository.findOneByLogin(signUpForm.getLogin().toLowerCase()).isPresent() ||
                !usersRepository.findOneByEmail(signUpForm.getEmail().toLowerCase()).isPresent()) {
            User user = User.builder()
                    .login(signUpForm.getLogin().toLowerCase())
                    .firstName(signUpForm.getFirstName())
                    .lastName(signUpForm.getLastName())
                    .hashPassword(passwordEncoder.encode(signUpForm.getPassword()))
                    .role(Role.USER)
                    .email(signUpForm.getEmail().toLowerCase())
                    .state(State.INACTIVE)
                    .activationCode(UUID.randomUUID().toString())
                    .build();
            usersRepository.save(user);

            String emailMessage = String.format("Hello, %s!\n" +
            "You have to follow the next link to activate your account at Book Club!\n" +
            "http://localhost:8081/signup/confirm/%s", user.getLogin(), user.getActivationCode());

            mailSender.sendMail(user.getEmail(), "Activate your account!", emailMessage);

            return usersRepository.findOneByLogin(signUpForm.getLogin().toLowerCase()).get().getId();
        }

        throw new EntityAlreadyExistsException("User with this login already exists");
    }

    public UserDto confirmSignUp(String code) {
        Optional<User> userCandidate = usersRepository.findOneByActivationCode(code);

        if (userCandidate.isPresent()) {
            User user = userCandidate.get();
            if (user.getState().equals(State.INACTIVE)) {
                user.setState(State.ACTIVE);
                user.setActivationCode(null);
                usersRepository.save(user);

                return UserDto.getUserDto(user);
            }
        }

        throw new EntityNotFoundException("Wrong activation code");
    }
}