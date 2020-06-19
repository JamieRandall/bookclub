package ru.club.models;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String login;
    @Column(name = "hash_password")
    private String hashPassword;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "usrs_clubs",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "club_id")}
    )
    private List<Club> clubs;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL
    )
    private List<Club> ownedClubs;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "owner"
    )
    private List<Token> tokens;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<Request> requests;
}
