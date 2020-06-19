package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.club.exception.UserAlreadyExistsException;
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


    public Long signUp(SignUpForm signUpForm) {

        if (!usersRepository.findOneByLogin(signUpForm.getLogin()).isPresent()) {
            User user = User.builder()
                    .login(signUpForm.getLogin())
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
