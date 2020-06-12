package ru.club.transfer;

import lombok.Data;
import ru.club.models.Token;

@Data
public class TokenDto {
    private String value;

    public TokenDto(Token token) {
        this.value = token.getValue();
    }
}
