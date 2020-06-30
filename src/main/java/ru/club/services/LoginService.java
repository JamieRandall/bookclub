package ru.club.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.club.exception.EntityNotFoundException;
import ru.club.forms.LoginForm;
import ru.club.forms.SignUpForm;
import ru.club.models.Token;
import ru.club.models.User;
import ru.club.repositories.TokensRepository;
import ru.club.repositories.UsersRepository;
import ru.club.transfer.TokenDto;

import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${secret.word}")
    private String secret;

    /**
     * Generates and returns token connected to user
     * @param loginForm - contains login and password
     * @return token value as dto
     * @throws IllegalArgumentException - if user with such login and password does not exist
     */
    public TokenDto login(LoginForm loginForm) {
        Optional<User> userCandidate = usersRepository.findOneByLogin(loginForm.getLogin());

        if (userCandidate.isPresent()) {
            User user = userCandidate.get();

            if (passwordEncoder.matches(loginForm.getPassword(), user.getHashPassword())) {
//                Token token = Token.builder()
//                        .value(RandomStringUtils.random(10, true, true))
//                        .owner(user)
//                        .build();
                Token token = Token.getJWT(user, secret);

                tokensRepository.save(token);

                return new TokenDto(token);
            }
        } throw new EntityNotFoundException("User not found");

    }
}
