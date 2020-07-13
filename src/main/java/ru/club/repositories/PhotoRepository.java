package ru.club.repositories;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.club.exception.ForbiddenException;
import ru.club.models.User;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Repository
public class PhotoRepository implements CloudRepository {
    @Autowired
    private UsersRepository usersRepository;
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public Optional<MultipartFile> findOneByValue(Long id) {


        return Optional.empty();
    }

    @Override
    public Optional<MultipartFile> findOneByUserId(Long userId) {
        Optional<User> userCandidate = usersRepository.findOneById(userId);

        if (userCandidate.isPresent()) {
            User user = userCandidate.get();

        }
        return Optional.empty();
    }

    @Override
    public String save(MultipartFile file) {
        try {
            if (file != null) {
                File directory = new File(uploadPath);
                if (!directory.exists()) {
                    directory.mkdir();
                }

                String randomNose = RandomStringUtils.random(5, true, false);
                String entireFileName = uploadPath + "/" + randomNose + "-" + file.getOriginalFilename();

                file.transferTo(new File(entireFileName));

                return entireFileName;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new ForbiddenException("File is empty");
    }
}
