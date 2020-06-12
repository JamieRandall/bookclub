package ru.club.security.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.club.models.Token;
import ru.club.repositories.TokensRepository;
import ru.club.security.details.UserDetailsServiceImpl;
import ru.club.security.token.TokenAuthentification;

import java.util.Optional;

@Component
public class TokenAuthentificationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private TokensRepository tokensRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TokenAuthentification tokenAuthentification = (TokenAuthentification) authentication;

        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(tokenAuthentification.getName());
        if (tokenCandidate.isPresent()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(tokenCandidate.get().getOwner().getLogin());

            tokenAuthentification.setUserDetails(userDetails);
            tokenAuthentification.setAuthenticated(true);

            return tokenAuthentification;
        } else throw new IllegalArgumentException("Bad Token");
    }

    @Override
    public boolean supports(Class<?> authentification) {
        return TokenAuthentification.class.equals(authentification);
    }
}
