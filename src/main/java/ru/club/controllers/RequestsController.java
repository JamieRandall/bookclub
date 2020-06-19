package ru.club.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.club.decorators.PageMutable;
import ru.club.services.RequestsService;
import ru.club.transfer.RequestDto;

import java.net.URI;

@RestController
@RequestMapping("/requests")
public class RequestsController {
    @Autowired
    private RequestsService requestsService;

    @GetMapping("/{club-id}")
    @ApiOperation(value = "Show all join requests of certain club", authorizations = { @Authorization(value="token") })
    public ResponseEntity<PageMutable<RequestDto>> getJoinRequests(
            @RequestParam(name = "token") String token,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @PathVariable(value = "club-id") Long clubId) {


        return ResponseEntity.ok(requestsService.getJoinRequests(clubId, page, size, token));
    }

    @PostMapping("/{request-id}")
    @ApiOperation(value = "Apply certain request of club, returns applied user", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> applyRequest(
            @RequestParam(name = "token") String token,
            @PathVariable(value = "request-id") Long requestId) {

        Long userId = requestsService.applyRequest(requestId, token);
        URI uri = URI.create("/users/" + userId);

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{request-id}")
    @ApiOperation(value = "Decline certain request of club", authorizations = { @Authorization(value="token") })
    public ResponseEntity<Object> declineRequest(
            @RequestParam(name = "token") String token,
            @PathVariable(value = "request-id") Long requestId) {

        requestsService.declineRequest(requestId, token);

        return ResponseEntity.ok().build();
    }
}
