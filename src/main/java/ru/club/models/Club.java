package ru.club.models;

import lombok.*;

import javax.persistence.*;
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
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(mappedBy = "clubs")
    private List<User> members;

    @OneToMany(
            mappedBy = "club",
            cascade = CascadeType.ALL
    )
    private List<Request> requests;
}
