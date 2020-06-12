package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.club.forms.UserForm;
import ru.club.models.User;
import ru.club.repositories.UsersRepository;
import ru.club.transfer.user.UserPageDto;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return usersRepository.findAll();
    }

    public void signUp(UserForm userForm) {
        User user = User.builder()
                .login(userForm.getLogin())
                .hashPassword(passwordEncoder.encode(userForm.getPassword()))
                .clubs(userForm.getClubs())
                .build();

        usersRepository.save(user);
    }

    public Page<User> paginationFindAll(Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        return usersRepository.findAll(certainPage);
    }

    public Optional<UserPageDto> findUserByLogin(String login) {
        Optional<User> userCandidate = usersRepository.findOneByLogin(login);
        Optional<UserPageDto> dto;
        if (userCandidate.isPresent()) {
            dto = Optional.of(UserPageDto.fromUser(userCandidate.get()));
        } else {
            dto = Optional.empty();
        }
        return dto;
    }
}
