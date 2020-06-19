package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.club.decorators.PageMutable;
import ru.club.exception.AlreadyMemberException;
import ru.club.exception.ClubAlreadyExistsException;
import ru.club.exception.ClubNotFoundException;
import ru.club.exception.RequestNotFoundException;
import ru.club.forms.ClubForm;
import ru.club.models.*;
import ru.club.repositories.ClubsRepository;
import ru.club.repositories.RequestsRepository;
import ru.club.repositories.TokensRepository;
import ru.club.repositories.UsersRepository;
import ru.club.transfer.RequestDto;
import ru.club.transfer.club.ClubDto;

import java.util.List;
import java.util.Optional;

@Service
public class ClubsService {
    @Autowired
    private ClubsRepository clubsRepository;
    @Autowired
    private RequestsRepository requestsRepository;
    @Autowired
    private TokensRepository tokensRepository;

    public PageMutable<ClubDto> paginationFindAll(Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        Page<Club> clubs = clubsRepository.findAll(certainPage);

        if (clubs.getContent().isEmpty())
            throw new ClubNotFoundException();

        return ClubDto.createMutableDtoPage(clubs);
    }

    public ClubDto findClubById(Long id) {
        Optional<Club> clubCandidate = clubsRepository.findOneById(id);
        if (clubCandidate.isPresent()) {
            return ClubDto.fromClub(clubCandidate.get());
        }
        throw new ClubNotFoundException();
    }

    public PageMutable<ClubDto> getClubsOwnedByUser(Long userId, Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        Page<Club> clubs = clubsRepository.findAllByOwner_id(userId, certainPage);

        if (clubs.getContent().isEmpty())
            throw new ClubNotFoundException();

        return ClubDto.createMutableDtoPage(clubs);
    }

    public PageMutable<ClubDto> getClubsUserParticipate(Long userId, Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        Page<Club> clubs = clubsRepository.findAllByMembers_id(userId, certainPage);

        if (clubs.getContent().isEmpty())
            throw new ClubNotFoundException();

        return ClubDto.createMutableDtoPage(clubs);
    }

    public Long createClub(ClubForm clubForm, String tokenValue) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(tokenValue);

        if (clubsRepository.findOneByTitle(clubForm.getTitle()).isPresent())
            throw new ClubAlreadyExistsException();

        Club club = Club.builder()
                .title(clubForm.getTitle())
                .description(clubForm.getDescription())
                .owner(tokenCandidate.get().getOwner())
                .build();

        clubsRepository.save(club);

        return clubsRepository.findOneByTitle(clubForm.getTitle()).get().getId();
    }

    public void joinClub(Long clubId, String token) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!clubCandidate.isPresent())
            throw new ClubNotFoundException();

        User user = tokenCandidate.get().getOwner();
        Club club = clubCandidate.get();

        if (club.getMembers().contains(user))
            throw new AlreadyMemberException();


        requestsRepository.save(Request.builder()
                .club(club)
                .user(user)
                .status(RequestStatus.NEW)
                .build());


    }
}
