package dev.joncollins.mpapi.auth;

import dev.joncollins.mpapi.Invite;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class PublicUserResponse extends BaseUserResponse {

}
