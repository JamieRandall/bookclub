package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.club.decorators.PageMutable;
import ru.club.exception.EntityNotFoundException;
import ru.club.exception.ForbiddenException;
import ru.club.forms.UserChangeForm;
import ru.club.models.Club;
import ru.club.models.Role;
import ru.club.models.Token;
import ru.club.models.User;
import ru.club.repositories.ClubsRepository;
import ru.club.repositories.TokensRepository;
import ru.club.repositories.UsersRepository;
import ru.club.transfer.UserDto;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ClubsRepository clubsRepository;
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public PageMutable<UserDto> paginationFindAll(Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);

        return UserDto.createMutableDtoPage(usersRepository.findAll(certainPage));
    }

    public UserDto findUserByLogin(String login) {
        Optional<User> userCandidate = usersRepository.findOneByLogin(login);
        if (userCandidate.isPresent()) {
            return UserDto.getUserDto(userCandidate.get());
        }

        throw new EntityNotFoundException("User not found");
    }

    /**
     * Returns info of certain user
     * @param id - user id
     * @return user info as dto
     * @throws EntityNotFoundException - if user with this id is not present
     */
    public UserDto findUserById(Long id) {
        Optional<User> userCandidate = usersRepository.findOneById(id);

        if (userCandidate.isPresent()) {
            return UserDto.getUserDto(userCandidate.get());
        }

        throw new EntityNotFoundException("User not found");
    }

    /**
     * Returns extended info of user connected with current token
     * @param tokenValue - user identifier
     * @return extended info of user as dto
     * @throws EntityNotFoundException - if token is not valid
     */
    public UserDto getMyPage(String tokenValue) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(tokenValue);

        //Return user connected to token, if it exists
        //In another way, throws exception
        if (tokenCandidate.isPresent()) {
            return UserDto.getUserDto(tokenCandidate.get().getOwner());
        }

        throw new EntityNotFoundException("User not found");
    }

    /**
     * Returns list of users who are members of certain club. Uses pagination
     * @param clubId - club id
     * @param page - number of page (for pagination)
     * @param size - size of elements on single page (for pagination)
     * @return list of users as dto
     * @throws EntityNotFoundException - if club with this id is not present
     */
    public PageMutable<UserDto> getMembersOfClub(Long clubId, Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!clubCandidate.isPresent())
            throw new EntityNotFoundException("Clubs not found");

        return UserDto.createMutableDtoPage(usersRepository.findAllByClubs_id(clubId, certainPage));
    }

    /**
     * Changes user profile info
     * @param userChangeForm - variety of new parameters for user profile
     * @param userId - user id
     * @param tokenValue - user identifier
     * @throws EntityNotFoundException - if user with this id is not present
     * @throws ForbiddenException - if token belongs to another user (but changes can be made by ADMIN)
     */
    public void changeUser(UserChangeForm userChangeForm, Long userId, String tokenValue) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(tokenValue);
        Role roleFromForm = Role.getRoleFromString(userChangeForm.getRole());

        //If user wants to change another profile, and he is not ADMIN
        //Throw exception
        if (!usersRepository.findOneById(userId).isPresent())
            throw new EntityNotFoundException("User not found");
        if ((tokenCandidate.get().getOwner().getId() != userId) && (!tokenCandidate.get().getOwner().getRole().equals(Role.ADMIN)))
            throw new ForbiddenException("Forbidden");


        User user = usersRepository.findOneById(userId).get();

        String firstName = (userChangeForm.getFirstName() == null) ? user.getFirstName() : userChangeForm.getFirstName();
        String lastName = (userChangeForm.getLastName() == null) ? user.getLastName() : userChangeForm.getLastName();
        String login = (userChangeForm.getLogin() == null) ? user.getLogin() : userChangeForm.getLogin();
        String hashPassword = (userChangeForm.getPassword() == null) ? user.getHashPassword() : passwordEncoder.encode(userChangeForm.getPassword());
        Role role = (roleFromForm == null) ? user.getRole() : roleFromForm;

        usersRepository.setUserInfoById(userId, firstName, lastName, login, hashPassword, role.toString());
    }

    public void uploadPhoto(MultipartFile file, Long userId, String token) {
        String fileName = file.getOriginalFilename();
        try {
            file.transferTo(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
