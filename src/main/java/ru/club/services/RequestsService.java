package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.club.decorators.PageMutable;
import ru.club.exception.club.ClubNotFoundException;
import ru.club.exception.common.ForbiddenException;
import ru.club.exception.request.RequestNotFoundException;
import ru.club.models.*;
import ru.club.repositories.ClubsRepository;
import ru.club.repositories.RequestsRepository;
import ru.club.repositories.TokensRepository;
import ru.club.repositories.UsersRepository;
import ru.club.transfer.RequestDto;

import java.util.Optional;

@Service
public class RequestsService {
    @Autowired
    private RequestsRepository requestsRepository;
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private ClubsRepository clubsRepository;
    @Autowired
    private UsersRepository usersRepository;

    /**
     * Shows all NEW requests. Uses pagination
     * @param clubId - id of the club
     * @param page - number of page (for pagination)
     * @param size - size of elements on single page (for pagination)
     * @param token - user identifier
     * @return list of NEW requests as dto
     * @throws RequestNotFoundException - if there are no requests on this page (or at all)
     */
    public PageMutable<RequestDto> getJoinRequests(Long clubId, Integer page, Integer size, String token) {
        this.accessCheck(clubId, token);

        Pageable certainPage = new PageRequest(page, size);
        Page<Request> requests = requestsRepository.findAllByClub_IdAndStatus(clubId, RequestStatus.NEW , certainPage);

        if (requests.getContent().isEmpty())
            throw new RequestNotFoundException();

        return RequestDto.createMutableDtoPage(requests);

    }

    /**
     * Applies request and makes user to be a member of the club.
     * Changes status of request from NEW to APPLIED
     * Manages access check to verify authorities
     * @param requestId - id of the request
     * @param token - user identifier
     * @return id of user whom request method applies
     * @throws RequestNotFoundException - if request with this id does not exist
     * @throws ForbiddenException - if someone tries to apply not NEW request (DECLINED OR ACCEPTED)
     */
    public Long applyRequest(Long requestId, String token) {
        Optional<Request> requestCandidate = requestsRepository.findOneById(requestId);

        if (!requestCandidate.isPresent())
            throw new RequestNotFoundException();
        if (!requestCandidate.get().getStatus().equals(RequestStatus.NEW))
            throw new ForbiddenException();

        Request request = requestCandidate.get();
        Club club = requestCandidate.get().getClub();
        User user = requestCandidate.get().getUser();

        this.accessCheck(request.getClub().getId(), token);

        user.getClubs().add(club);
        usersRepository.save(user);

        request.setStatus(RequestStatus.ACCEPTED);
        requestsRepository.save(request);

        return user.getId();
    }

    /**
     * Declines join request and set it status to DECLINED
     * Manages access check to verify authorities
     * @param requestId - id of the request
     * @param token - user identifier
     * @throws RequestNotFoundException - if request with this id does not exist
     * @throws ForbiddenException - if someone tries to decline not NEW request (DECLINED OR ACCEPTED)
     */
    public void declineRequest(Long requestId, String token) {


        Optional<Request> requestCandidate = requestsRepository.findOneById(requestId);

        if (!requestCandidate.isPresent())
            throw new RequestNotFoundException();
        if (!requestCandidate.get().getStatus().equals(RequestStatus.NEW))
            throw new ForbiddenException();

        this.accessCheck(requestCandidate.get().getClub().getId(), token);

        Request request = requestCandidate.get();

        request.setStatus(RequestStatus.DECLINED);

        requestsRepository.save(request);

    }

    /**
     * Verify authorities of user who wants to manage request
     * @param clubId - id of club requests are connected to
     * @param token - user identifier
     * @throws ForbiddenException - if token is not valid
     * @throws ClubNotFoundException if club does not exist
     * @throws ForbiddenException - if user is now owner of the club
     */
    private void accessCheck(Long clubId, String token) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!tokenCandidate.isPresent())
            throw new ForbiddenException();
        if (!clubCandidate.isPresent())
            throw new ClubNotFoundException();
        if (!tokenCandidate.get().getOwner().getOwnedClubs().contains(clubCandidate.get()))
            throw new ForbiddenException();
    }
}
