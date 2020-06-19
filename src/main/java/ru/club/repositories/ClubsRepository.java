package ru.club.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.club.models.Club;
import java.util.Optional;

public interface ClubsRepository extends JpaRepository<Club, Long> {
    Optional<Club> findOneByTitle(String title);

    Optional<Club> findOneById(Long id);

    Page<Club> findAllByMembers_id(Long id, Pageable pageable);

    Page<Club> findAllByOwner_id(Long id, Pageable pageable);

}
