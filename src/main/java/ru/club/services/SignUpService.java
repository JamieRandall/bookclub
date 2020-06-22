package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.club.exception.user.UserAlreadyExistsException;
import ru.club.forms.SignUpForm;
import ru.club.models.Role;
import ru.club.models.User;
import ru.club.repositories.UsersRepository;

@Service
public class SignUpService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Saves new user to database
     * @param signUpForm - contains diverse parameters of user profile
     * @return id of new user
     * @throws UserAlreadyExistsException - if user with this login already exists
     */
    public Long signUp(SignUpForm signUpForm) {

        if (!usersRepository.findOneByLogin(signUpForm.getLogin()).isPresent()) {
            User user = User.builder()
                    .login(signUpForm.getLogin().toLowerCase())
                    .firstName(signUpForm.getFirstName())
                    .lastName(signUpForm.getLastName())
                    .hashPassword(passwordEncoder.encode(signUpForm.getPassword()))
                    .role(Role.USER)
                    .build();
            usersRepository.save(user);

            return usersRepository.findOneByLogin(signUpForm.getLogin()).get().getId();
        }

        throw new UserAlreadyExistsException();
    }
}
