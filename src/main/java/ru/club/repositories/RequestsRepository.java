package ru.club.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.club.models.Club;
import ru.club.models.Request;
import ru.club.models.RequestStatus;
import ru.club.models.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface RequestsRepository extends JpaRepository<Request, Long> {
    Optional<Request> findOneById(Long requestId);

    Optional<Request> findOneByClubAndUser(Club club, User user);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM request WHERE user_id = ?1 AND club_id = ?2", nativeQuery = true)
    void deleteRequestByUserIdAndClubId(Long userId, Long clubId);

    Page<Request> findAllByClub_IdAndStatus(Long clubId, RequestStatus status, Pageable pageable);
}
