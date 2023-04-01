package dev.joncollins.mpapi.auth;

import dev.joncollins.mpapi.Invite;
import dev.joncollins.mpapi.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String name;
    private Boolean isPrivate;
    private List<Invite> invites;
    private LocalDateTime createdAt;
}
