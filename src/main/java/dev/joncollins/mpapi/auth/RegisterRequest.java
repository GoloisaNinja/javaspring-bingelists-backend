package dev.joncollins.mpapi.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String email;
    private String password;
    private String isPrivate;

    public Boolean getIsPrivateAsBoolean() {
        return Boolean.parseBoolean(this.isPrivate);
    }
}
