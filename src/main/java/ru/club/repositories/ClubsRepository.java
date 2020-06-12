package ru.club.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.club.models.Club;

import java.util.Optional;

public interface ClubsRepository extends JpaRepository<Club, Long> {
    Optional<Club> findOneByTitle(String title);
}
