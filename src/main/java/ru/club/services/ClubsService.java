package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.club.decorators.PageMutable;
import ru.club.exception.club.AlreadyMemberException;
import ru.club.exception.club.ClubAlreadyExistsException;
import ru.club.exception.club.ClubNotFoundException;
import ru.club.exception.club.NotMemberException;
import ru.club.exception.common.ForbiddenException;
import ru.club.exception.request.RequestAlreadyExistsException;
import ru.club.forms.ClubForm;
import ru.club.forms.RequestForm;
import ru.club.models.*;
import ru.club.repositories.ClubsRepository;
import ru.club.repositories.RequestsRepository;
import ru.club.repositories.TokensRepository;
import ru.club.transfer.club.ClubDto;

import java.util.Optional;

@Service
public class ClubsService {
    @Autowired
    private ClubsRepository clubsRepository;
    @Autowired
    private RequestsRepository requestsRepository;
    @Autowired
    private TokensRepository tokensRepository;

    /**
     * Returns list of all clubs. Uses pagination
     * @param page - number of page (for pagination)
     * @param size - size of elements on single page (for pagination)
     * @return list of clubs as dto
     * @throws ClubNotFoundException - id there are no clubs on this page (or at all)
     */
    public PageMutable<ClubDto> paginationFindAll(Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        Page<Club> clubs = clubsRepository.findAll(certainPage);

        if (clubs.getContent().isEmpty())
            throw new ClubNotFoundException();

        return ClubDto.createMutableDtoPage(clubs);
    }

    /**
     * Returns info of certain club
     * @param id - club id
     * @return club info as dto
     * @throws ClubNotFoundException - if club with this id does not exist
     */
    public ClubDto findClubById(Long id) {
        Optional<Club> clubCandidate = clubsRepository.findOneById(id);
        if (clubCandidate.isPresent()) {
            return ClubDto.fromClub(clubCandidate.get());
        }
        throw new ClubNotFoundException();
    }

    /**
     * Returns list of clubs that user owns. Uses pagination
     * @param userId - id of user
     * @param page - number of page (for pagination)
     * @param size - size of elements on single page (for pagination)
     * @return list of clubs as dto
     * @throws ClubNotFoundException - if user does not participate in any club
     */
    public PageMutable<ClubDto> getClubsOwnedByUser(Long userId, Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        Page<Club> clubs = clubsRepository.findAllByOwner_id(userId, certainPage);

        if (clubs.getContent().isEmpty())
            throw new ClubNotFoundException();

        return ClubDto.createMutableDtoPage(clubs);
    }

    /**
     * Returns list of clubs that user participates in. Uses pagination
     * @param userId - id of user
     * @param page - number of page (for pagination)
     * @param size - size of elements on single page (for pagination)
     * @return list of clubs as dto
     * @throws ClubNotFoundException - if user does not participate in any club
     */
    public PageMutable<ClubDto> getClubsUserParticipate(Long userId, Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        Page<Club> clubs = clubsRepository.findAllByMembers_id(userId, certainPage);

        if (clubs.getContent().isEmpty())
            throw new ClubNotFoundException();

        return ClubDto.createMutableDtoPage(clubs);
    }

    /**
     * Creates new club owned by user
     * @param clubForm - contains title and description of new club
     * @param tokenValue - user identifier
     * @return id of new club
     * @throws ClubAlreadyExistsException - if club with such title already exists
     */
    public Long createClub(ClubForm clubForm, String tokenValue) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(tokenValue);

        if (clubsRepository.findOneByTitle(clubForm.getTitle()).isPresent())
            throw new ClubAlreadyExistsException();

        Club club = Club.builder()
                .title(clubForm.getTitle())
                .description(clubForm.getDescription())
                .owner(tokenCandidate.get().getOwner())
                .state(State.ACTIVE)
                .build();

        clubsRepository.save(club);

        return clubsRepository.findOneByTitle(clubForm.getTitle()).get().getId();
    }

    /**
     * Creates join request from user by his token
     * @param clubId - club id user wants to join
     * @param token - user identifier
     * @param requestForm - contains cover letter
     * @throws ClubNotFoundException - if club user want to join does not exist
     * @throws AlreadyMemberException - if club already contains this user as a member
     * @throws RequestAlreadyExistsException - if join request to club from this user already exists
     */
    public void joinClub(Long clubId, String token, RequestForm requestForm) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!clubCandidate.isPresent())
            throw new ClubNotFoundException();

        User user = tokenCandidate.get().getOwner();
        Club club = clubCandidate.get();

        if (club.getMembers().contains(user))
            throw new AlreadyMemberException();

        if (requestsRepository.findOneByClubAndUser(club, user).isPresent())
            throw new RequestAlreadyExistsException();


        requestsRepository.save(Request.builder()
                .club(club)
                .user(user)
                .status(RequestStatus.NEW)
                .coverLetter(requestForm.getCoverLetter())
                .build());


    }

    /**
     * Deletes user from list of club members
     * Deletes request connected to this club
     * @param clubId - club id user wants to leave
     * @param token - user identifier
     * @throws ClubNotFoundException - if club with this id is not present
     * @throws ForbiddenException - if token is not valid
     * @throws NotMemberException - if user wants to leave club and he is not a member of it
     */
    public void leaveClub(Long clubId, String token) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!clubCandidate.isPresent())
            throw new ClubNotFoundException();
        if (!tokenCandidate.isPresent())
            throw new ForbiddenException();

        User user = tokenCandidate.get().getOwner();
        Club club = clubCandidate.get();

        if (!club.getMembers().remove(user))
            throw new NotMemberException();

        requestsRepository.deleteRequestByUserIdAndClubId(user.getId(), club.getId());
        clubsRepository.deleteMemberByIdAndClubId(user.getId(), club.getId());
    }

    /**
     * Deletes club. Actually, sets it state to DELETED
     * @param clubId - club id user wants to leave
     * @param token - user identifier
     * @throws ClubNotFoundException - if club with this id is not present
     * @throws ForbiddenException - if token is not valid
     * @throws ForbiddenException - if user is not an owner of the club
     */
    public void deleteClub(Long clubId, String token) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!clubCandidate.isPresent())
            throw new ClubNotFoundException();
        if (!tokenCandidate.isPresent())
            throw new ForbiddenException();

        User user = tokenCandidate.get().getOwner();
        Club club = clubCandidate.get();

        if (user.getId() != club.getOwner().getId())
            throw new ForbiddenException();

        club.setState(State.DELETED);
        clubsRepository.save(club);
    }

    /**
     * Recovers club if it was deleted
     * @param clubId - id of club to recover
     * @param token - user identifier
     * @throws ClubNotFoundException - if club with this id does not exists
     * @throws ForbiddenException - if token is not valid
     * @throws ClubAlreadyExistsException - if club status is not DELETED
     */
    public void recoverClub(Long clubId, String token) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!clubCandidate.isPresent())
            throw new ClubNotFoundException();
        if (!tokenCandidate.isPresent())
            throw new ForbiddenException();
        if (!clubCandidate.get().getState().equals(State.DELETED))
            throw new ClubAlreadyExistsException();

        Club club = clubCandidate.get();

        club.setState(State.ACTIVE);

        clubsRepository.save(club);
    }
}
