package dev.joncollins.mpapi.auth;

import dev.joncollins.mpapi.Invite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseUserResponse {
    private String id;
    private String name;
    private boolean isPrivate;
    private LocalDateTime createdAt;
}
