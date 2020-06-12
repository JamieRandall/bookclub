package ru.club.app;

import ru.club.models.User;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .firstName("Jamie")
                .lastName("Randall")
                .build());

        users.add(User.builder()
                .firstName("Mary")
                .lastName("Alt")
                .build());
        System.out.println(users);
    }
}
