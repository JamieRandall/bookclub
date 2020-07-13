package ru.club.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.club.models.Role;
import ru.club.models.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByLogin(String login);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneById(Long id);

    Optional<User> findOneByActivationCode(String code);

//    Page<User> findAllByClubs_title(String title, Pageable pageable);

    List<User> findAllByClubs_title(String title);

    Page<User> findAllByClubs_id(Long id, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE usr SET first_name = ?2, last_name = ?3, login = ?4, hash_password = ?5, role = ?6 WHERE id = ?1", nativeQuery = true)
    void setUserInfoById(Long id, String firstName, String lastName, String login, String hashPassword, String role);
 }
