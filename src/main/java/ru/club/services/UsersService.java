package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.club.decorators.PageMutable;
import ru.club.exception.club.ClubNotFoundException;
import ru.club.exception.common.ForbiddenException;
import ru.club.exception.user.UserNotFoundException;
import ru.club.forms.UserChangeForm;
import ru.club.models.Club;
import ru.club.models.Role;
import ru.club.models.Token;
import ru.club.models.User;
import ru.club.repositories.ClubsRepository;
import ru.club.repositories.TokensRepository;
import ru.club.repositories.UsersRepository;
import ru.club.transfer.user.UserPageDto;
import ru.club.transfer.user.UserProfileDto;

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

    public PageMutable<UserPageDto> paginationFindAll(Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);

        return UserPageDto.createMutableDtoPage(usersRepository.findAll(certainPage));
    }

    public UserPageDto findUserByLogin(String login) {
        Optional<User> userCandidate = usersRepository.findOneByLogin(login);
        if (userCandidate.isPresent()) {
            return UserPageDto.fromUser(userCandidate.get());
        }

        throw new UserNotFoundException();
    }

    /**
     * Returns info of certain user
     * @param id - user id
     * @return user info as dto
     * @throws UserNotFoundException - if user with this id is not present
     */
    public UserPageDto findUserById(Long id) {
        Optional<User> userCandidate = usersRepository.findOneById(id);

        if (userCandidate.isPresent()) {
            return UserPageDto.fromUser(userCandidate.get());
        }

        throw new UserNotFoundException();
    }

    /**
     * Returns extended info of user connected with current token
     * @param tokenValue - user identifier
     * @return extended info of user as dto
     * @throws UserNotFoundException - if token is not valid
     */
    public UserProfileDto getMyPage(String tokenValue) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(tokenValue);

        //Return user connected to token, if it exists
        //In another way, throws exception
        if (tokenCandidate.isPresent()) {
            return UserProfileDto.fromUser(tokenCandidate.get().getOwner());
        }

        throw new UserNotFoundException();
    }

    /**
     * Returns list of users who are members of certain club. Uses pagination
     * @param clubId - club id
     * @param page - number of page (for pagination)
     * @param size - size of elements on single page (for pagination)
     * @return list of users as dto
     * @throws ClubNotFoundException - if club with this id is not present
     */
    public PageMutable<UserPageDto> getMembersOfClub(Long clubId, Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        Optional<Club> clubCandidate = clubsRepository.findOneById(clubId);

        if (!clubCandidate.isPresent())
            throw new ClubNotFoundException();

        return UserPageDto.createMutableDtoPage(usersRepository.findAllByClubs_id(clubId, certainPage));
    }

    /**
     * Changes user profile info
     * @param userChangeForm - variety of new parameters for user profile
     * @param userId - user id
     * @param tokenValue - user identifier
     * @throws UserNotFoundException - if user with this id is not present
     * @throws ForbiddenException - if token belongs to another user (but changes can be made by ADMIN)
     */
    public void changeUser(UserChangeForm userChangeForm, Long userId, String tokenValue) {
        Optional<Token> tokenCandidate = tokensRepository.findOneByValue(tokenValue);
        Role roleFromForm = Role.getRoleFromString(userChangeForm.getRole());

        //If user wants to change another profile, and he is not ADMIN
        //Throw exception
        if (!usersRepository.findOneById(userId).isPresent())
            throw new UserNotFoundException();
        if ((tokenCandidate.get().getOwner().getId() != userId) && (!tokenCandidate.get().getOwner().getRole().equals(Role.ADMIN)))
            throw new ForbiddenException();


        User user = usersRepository.findOneById(userId).get();

        String firstName = (userChangeForm.getFirstName() == null) ? user.getFirstName() : userChangeForm.getFirstName();
        String lastName = (userChangeForm.getLastName() == null) ? user.getLastName() : userChangeForm.getLastName();
        String login = (userChangeForm.getLogin() == null) ? user.getLogin() : userChangeForm.getLogin();
        String hashPassword = (userChangeForm.getPassword() == null) ? user.getHashPassword() : passwordEncoder.encode(userChangeForm.getPassword());
        Role role = (roleFromForm == null) ? user.getRole() : roleFromForm;

        usersRepository.setUserInfoById(userId, firstName, lastName, login, hashPassword, role.toString());
    }

    public void test() {
        User user = usersRepository.findOneById((long) 2).get();
        Club club = clubsRepository.findOneById((long) 2).get();
        user.getClubs().add(club);

        usersRepository.save(user);
    }

}
