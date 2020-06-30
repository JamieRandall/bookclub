package ru.club.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public static Token getJWT(User user, String secret) {
        String header = String.format(
                "{" +
                        "\"alg\":\"%s\"," +
                        "\"type\":\"JWT\"" +
                        "}",
                "HS256");

        String payload = String.format(
                "{" +
                        "\"id\":%s," +
                        "\"login\":\"%s\"," +
                        "\"email\":\"%s\"" +
                        "}",
                user.getId(), user.getLogin(), user.getEmail());

        header = Base64.getEncoder().encodeToString(header.getBytes());
        payload = Base64.getEncoder().encodeToString(payload.getBytes());
        secret = Base64.getEncoder().encodeToString(secret.getBytes());

        String tokenValue = header + "." + payload;// + "." + secret;

        return Token.builder()
                .owner(user)
                .value(Base64.getEncoder().encodeToString(tokenValue.getBytes()))
                .build();

    }
}
