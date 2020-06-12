package ru.club.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.club.models.User;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByLogin(String login);

    Optional<User> findOneById(Long id);

//    Page<User> findAllByClubs_title(String title, Pageable pageable);

    List<User> findAllByClubs_title(String title);
 }
