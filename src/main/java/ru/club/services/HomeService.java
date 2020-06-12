package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.club.models.Token;
import ru.club.models.User;
import ru.club.repositories.TokensRepository;
import ru.club.repositories.UsersRepository;
import ru.club.transfer.user.UserProfileDto;

import java.util.Optional;

@Service
public class HomeService {
    @Autowired
    private TokensRepository tokensRepository;

    public Optional<UserProfileDto> getProfileInfo(String tokenValue) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(tokenValue);
        Optional<UserProfileDto> dto;
        //Return user connected to token, if it exists
        //In another way, return empty optional
        if (tokenCandidate.isPresent()) {
            dto = Optional.of(UserProfileDto.fromUser(tokenCandidate.get().getOwner()));
        } else {
            dto = Optional.empty();
        }
        return dto;
    }

}
