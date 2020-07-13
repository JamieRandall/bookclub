package ru.club.repositories;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface CloudRepository {
    Optional<MultipartFile> findOneByValue(Long value);

    Optional<MultipartFile> findOneByUserId(Long userId);

    String save(MultipartFile file);
}
