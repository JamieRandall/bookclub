package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.club.decorators.PageMutable;
import ru.club.exception.ClubNotFoundException;
import ru.club.exception.ForbiddenException;
import ru.club.exception.RequestNotFoundException;
import ru.club.models.*;
import ru.club.repositories.ClubsRepository;
import ru.club.repositories.RequestsRepository;
import ru.club.repositories.TokensRepository;
import ru.club.transfer.RequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestsService {
    @Autowired
    private RequestsRepository requestsRepository;
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private ClubsRepository clubsRepository;

    /**
     * Method shows all NEW requests
     * @param clubId
     * @param page
     * @param size
     * @param token
     * @return dto list of NEW requests
     */
    public PageMutable<RequestDto> getJoinRequests(Long clubId, Integer page, Integer size, String token) {
        this.checkAccess(clubId, token);

        Pageable certainPage = new PageRequest(page, size);
        Page<Request> requests = requestsRepository.findAllByClub_IdAndStatus(clubId, RequestStatus.NEW , certainPage);

        if (requests.getContent().isEmpty())
            throw new RequestNotFoundException();

        return RequestDto.createMutableDtoPage(requests);

    }

    public Long applyRequest(Long requestId, String token) {
        Optional<Request> requestCandidate = requestsRepository.findOneById(requestId);

        if (!requestCandidate.isPresent())
            throw new RequestNotFoundException();

        Request request = requestCandidate.get();
        Club club = requestCandidate.get().getClub();
        User user = requestCandidate.get().getUser();

        this.checkAccess(request.getClub().getId(), token);

        club.getMembers().add(user);
        request.setStatus(RequestStatus.ACCEPTED);

        clubsRepository.save(club);
        requestsRepository.save(request);

        return user.getId();
    }

    public void declineRequest(Long requestId, String token) {


        Optional<Request> requestCandidate = requestsRepository.findOneById(requestId);

        if (!requestCandidate.isPresent())
            throw new RequestNotFoundException();

        this.checkAccess(requestCandidate.get().getClub().getId(), token);

        Request request = requestCandidate.get();

        request.setStatus(RequestStatus.DECLINED);

        requestsRepository.save(request);

    }

    private void checkAccess(Long clubId, String token) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(token);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        /**
         * Check if entities are present
         */
        if (!tokenCandidate.isPresent())
            throw new ForbiddenException();
        if (!clubCandidate.isPresent())
            throw new ClubNotFoundException();

        /**
         * If user does not own club he wants to access
         * Throw forbidden access exception
         */
        if (!tokenCandidate.get().getOwner().getOwnedClubs().contains(clubCandidate.get()))
            throw new ForbiddenException();
    }
}
