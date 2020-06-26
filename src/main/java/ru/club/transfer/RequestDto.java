package ru.club.transfer;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import ru.club.decorators.PageMutable;
import ru.club.models.Request;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class RequestDto {
    private Long requestId;
    private String userLogin;
    private Long userId;

    public static PageMutable<RequestDto> createMutableDtoPage(Page<Request> requests) {
        List<RequestDto> innerList = new ArrayList<>();
        for (Request request : requests) {
            innerList.add(RequestDto.builder()
                    .userId(request.getUser().getId())
                    .userLogin(request.getUser().getLogin())
                    .requestId(request.getId())
                    .build());
        }

        return new PageMutable(innerList, requests.getTotalPages(), requests.getTotalElements(), requests.getSort());
    }
}
