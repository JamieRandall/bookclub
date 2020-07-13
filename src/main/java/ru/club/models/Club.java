package ru.club.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "club")
@ToString(exclude = "owner")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private State state;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(
            mappedBy = "clubs",
            cascade = CascadeType.PERSIST)
    private List<User> members;

    @OneToMany(
            mappedBy = "club",
            cascade = CascadeType.ALL
    )
    private List<Request> requests;
}
