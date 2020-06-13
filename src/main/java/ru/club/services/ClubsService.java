package ru.club.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.club.adapters.PageAsList;
import ru.club.models.Club;
import ru.club.models.User;
import ru.club.repositories.ClubsRepository;
import ru.club.repositories.UsersRepository;
import ru.club.transfer.club.ClubDto;
import ru.club.transfer.club.ClubMemberDto;

import java.util.List;
import java.util.Optional;

@Service
public class ClubsService {
    @Autowired
    private ClubsRepository clubsRepository;
    @Autowired
    private UsersRepository usersRepository;


    public List<Club> findAll() {
        return  clubsRepository.findAll();
    }

    public PageAsList<ClubDto> paginationFindAll(Integer page, Integer size) {
        Pageable certainPage = new PageRequest(page, size);
        return ClubDto.getDtosListFromPage(clubsRepository.findAll(certainPage));
    }

//    public PageAsList<ClubMemberDto> paginationFindAllMembersOfClub(Integer page, Integer size, String title) {
//
//    }

    public List<User> getUsersByClubTitle(String title) {
        return usersRepository.findAllByClubs_title(title);
    }

    public Optional<Club> findClubByTitle(String title) {
        Optional<Club> clubCandidate = clubsRepository.findOneByTitle(title);
        if (clubCandidate.isPresent()) {
            return clubCandidate;
        } else {
            return Optional.empty();
        }
    }
}
