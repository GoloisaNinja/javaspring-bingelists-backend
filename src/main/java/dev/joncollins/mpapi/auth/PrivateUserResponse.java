package dev.joncollins.mpapi.auth;

import dev.joncollins.mpapi.Invite;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PrivateUserResponse extends BaseUserResponse {
    private String email;
    private String token;
    private List<Invite> invites;

}
