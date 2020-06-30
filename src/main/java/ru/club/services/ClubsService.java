package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.club.decorators.PageMutable;
import ru.club.exception.EntityAlreadyExistsException;
import ru.club.exception.EntityNotFoundException;
import ru.club.exception.ForbiddenException;
import ru.club.forms.ClubForm;
import ru.club.forms.RequestForm;
import ru.club.models.*;
import ru.club.repositories.ClubsRepository;
import ru.club.repositories.RequestsRepository;
import ru.club.repositories.TokensRepository;
import ru.club.repositories.UsersRepository;
import ru.club.transfer.ClubDto;

import java.util.Optional;

@Service
public class ClubsService {
    @Autowired
    private ClubsRepository clubsRepository;
    @Autowired
    private RequestsRepository requestsRepository;
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private UsersRepository usersRepository;

    /**
     * Returns list of all clubs. Uses pagination
     * @param page - number of page (for pagination)
     * @param size - size of elements on single page (for pagination)
     * @return list of clubs as dto
     * @throws EntityNotFoundException - if there are no clubs on this page (or at all)
     */
    public PageMutable<ClubDto> paginationFindAll(Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        Page<Club> clubs = clubsRepository.findAll(certainPage);

        if (clubs.getContent().isEmpty())
            throw new EntityNotFoundException("Clubs not found");

        return ClubDto.createMutableDtoPage(clubs);
    }

    /**
     * Returns info of certain club
     * @param id - club id
     * @return club info as dto
     * @throws EntityNotFoundException - if club with this id does not exist
     */
    public ClubDto findClubById(Long id, String token) {
        Optional<Club> clubCandidate = clubsRepository.findOneById(id);
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);


        if (clubCandidate.isPresent() && tokenCandidate.isPresent()) {
            Club club = clubCandidate.get();
            User user = tokenCandidate.get().getOwner();
            Optional<Request> requestCandidate = requestsRepository.findOneByClubAndUser(club, user);

            if (requestCandidate.isPresent()) {
                return ClubDto.getClubDto(club, requestCandidate.get().getStatus().name());
            } else {
                return ClubDto.getClubDto(club, "NULL");
            }

        }
        throw new EntityNotFoundException("Club not found");
    }

    /**
     * Returns list of clubs that user owns. Uses pagination
     * @param userId - id of user
     * @param page - number of page (for pagination)
     * @param size - size of elements on single page (for pagination)
     * @return list of clubs as dto
     * @throws EntityNotFoundException - if user does not own in any club
     */
    public PageMutable<ClubDto> getClubsOwnedByUser(Long userId, Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        Page<Club> clubs = clubsRepository.findAllByOwner_id(userId, certainPage);

        if (clubs.getContent().isEmpty())
            throw new EntityNotFoundException("User doesn't own any club");

        return ClubDto.createMutableDtoPage(clubs);
    }

    /**
     * Returns list of clubs that user participates in. Uses pagination
     * @param userId - id of user
     * @param page - number of page (for pagination)
     * @param size - size of elements on single page (for pagination)
     * @return list of clubs as dto
     * @throws EntityNotFoundException - if user does not participate in any club
     */
    public PageMutable<ClubDto> getClubsUserParticipate(Long userId, Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        Page<Club> clubs = clubsRepository.findAllByMembers_id(userId, certainPage);

        if (clubs.getContent().isEmpty())
            throw new EntityNotFoundException("User doesn't participate in any club");

        return ClubDto.createMutableDtoPage(clubs);
    }

    /**
     * Creates new club owned by user
     * @param clubForm - contains title and description of new club
     * @param tokenValue - user identifier
     * @return id of new club
     * @throws EntityAlreadyExistsException - if club with such title already exists
     */
    public ClubDto createClub(ClubForm clubForm, String tokenValue) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(tokenValue);

        if (clubsRepository.findOneByTitle(clubForm.getTitle()).isPresent())
            throw new EntityAlreadyExistsException("Club with this title already exists");

        Club club = Club.builder()
                .title(clubForm.getTitle())
                .description(clubForm.getDescription())
                .owner(tokenCandidate.get().getOwner())
                .state(State.ACTIVE)
                .build();

        clubsRepository.save(club);

        return ClubDto.getClubDto(clubsRepository.findOneByTitle(clubForm.getTitle()).get(), "NULL");
    }

    /**
     * Creates join request from user by his token
     * @param clubId - club id user wants to join
     * @param token - user identifier
     * @param requestForm - contains cover letter
     * @throws EntityNotFoundException - if club user want to join does not exist
     * @throws EntityAlreadyExistsException - if club already contains this user as a member
     * @throws EntityAlreadyExistsException - if join request to club from this user already exists
     */
    public void joinClub(Long clubId, String token, RequestForm requestForm) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!clubCandidate.isPresent())
            throw new EntityNotFoundException("Club not found");

        User user = tokenCandidate.get().getOwner();
        Club club = clubCandidate.get();

        if (club.getMembers().contains(user))
            throw new EntityAlreadyExistsException("User is already a member");

        if (requestsRepository.findOneByClubAndUser(club, user).isPresent())
            throw new EntityAlreadyExistsException("User already sent request");


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
     * @throws EntityNotFoundException - if club with this id is not present
     * @throws ForbiddenException - if token is not valid
     * @throws EntityNotFoundException - if user wants to leave club and he is not a member of it
     */
    public void leaveClub(Long clubId, String token) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!clubCandidate.isPresent())
            throw new EntityNotFoundException("Club not found");
        if (!tokenCandidate.isPresent())
            throw new ForbiddenException("Forbidden. Token is not valid");

        User user = tokenCandidate.get().getOwner();
        Club club = clubCandidate.get();

        if (!club.getMembers().remove(user))
            throw new EntityNotFoundException("User is not a member");

        requestsRepository.deleteRequestByUserIdAndClubId(user.getId(), club.getId());
        clubsRepository.deleteMemberByIdAndClubId(user.getId(), club.getId());
    }

    /**
     * Deletes club. Actually, sets it state to DELETED
     * @param clubId - club id user wants to leave
     * @param token - user identifier
     * @throws EntityNotFoundException - if club with this id is not present
     * @throws ForbiddenException - if token is not valid
     * @throws ForbiddenException - if user is not an owner of the club
     */
    public void deleteClub(Long clubId, String token) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!clubCandidate.isPresent())
            throw new EntityNotFoundException("Club not found");
        if (!tokenCandidate.isPresent())
            throw new ForbiddenException("Forbidden. Token is not valid");

        User user = tokenCandidate.get().getOwner();
        Club club = clubCandidate.get();

        if (user.getId() != club.getOwner().getId())
            throw new ForbiddenException("Forbidden. User is not owner");

        club.setState(State.DELETED);
        clubsRepository.save(club);
    }

    /**
     * Recovers club if it was deleted
     * @param clubId - id of club to recover
     * @param token - user identifier
     * @throws EntityNotFoundException - if club with this id does not exists
     * @throws ForbiddenException - if token is not valid
     * @throws EntityAlreadyExistsException - if club status is not DELETED
     */
    public void recoverClub(Long clubId, String token) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!clubCandidate.isPresent())
            throw new EntityNotFoundException("Club not found");
        if (!tokenCandidate.isPresent())
            throw new ForbiddenException("Forbidden. Token is not valid");
        if (!clubCandidate.get().getState().equals(State.DELETED))
            throw new EntityAlreadyExistsException("Club has no need to be recovered");

        Club club = clubCandidate.get();

        club.setState(State.ACTIVE);

        clubsRepository.save(club);
    }

    /**
     * Changes info of the club
     * @param clubForm - contains new title and description
     * @param clubId - id of the club
     * @param token - user identifier
     * @throws EntityNotFoundException - if club with this id does not exist/ if user with this token does not exist
     * @throws ForbiddenException - if token belongs to user who does not own the club (but changes can be made by ADMIN)
     */
    public void changeClub(ClubForm clubForm, Long clubId, String token) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!clubCandidate.isPresent())
            throw new EntityNotFoundException("Club not found");

        //If user wants to change another profile, and he is not ADMIN
        //Throw exception
        if (!usersRepository.findOneById(tokenCandidate.get().getOwner().getId()).isPresent())
            throw new EntityNotFoundException("User not found");
        if ((tokenCandidate.get().getOwner().getId() != clubCandidate.get().getOwner().getId())
                && (!tokenCandidate.get().getOwner().getRole().equals(Role.ADMIN)))
            throw new ForbiddenException("Forbidden");


        Club club = clubCandidate.get();

        String title = (clubForm.getTitle() == null) ? club.getTitle() : clubForm.getTitle();
        String description = (clubForm.getDescription() == null) ? club.getDescription() : clubForm.getDescription();

        clubsRepository.setClubInfoById(clubId, title, description);
    }
}
