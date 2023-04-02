package dev.joncollins.mpapi.invite;

import dev.joncollins.mpapi.Invite;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invite")
@AllArgsConstructor
public class InviteController {
    private InviteService service;

    @GetMapping("/invites")
    public ResponseEntity<List<Invite>> fetchAllInvitesByOriginator(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(service.fetchAllInvitesByOriginatorId(auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }
    @PostMapping("/send")
    public ResponseEntity<String> sendBingelistInviteToUser(@RequestBody Invite invite,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(service.sendBingelistInviteToUser(invite, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }

    }
    @PostMapping("/rescind")
    public ResponseEntity<String> rescindBingeListInvite(@RequestParam String inviteId,
                                                         @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(service.rescindBingelistInvite(inviteId, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
    @PostMapping("/accepted")
    public ResponseEntity<String> invitedUserAcceptsInvite(@RequestParam String inviteId,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(service.invitedUserAcceptsInvite(inviteId, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
    @PostMapping("/declined")
    public ResponseEntity<String> invitedUserDeclinesInvite(@RequestParam String inviteId,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        try {
            return ResponseEntity.ok(service.invitedUserDeclinesInvite(inviteId, auth));
        } catch(HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }
}
