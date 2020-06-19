package ru.club.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.club.models.Request;
import ru.club.models.RequestStatus;

import java.util.Optional;

@Repository
public interface RequestsRepository extends JpaRepository<Request, Long> {
    Optional<Request> findOneById(Long requestId);

    Page<Request> findAllByClub_IdAndStatus(Long clubId, RequestStatus status, Pageable pageable);
}
