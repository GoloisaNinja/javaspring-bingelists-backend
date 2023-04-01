package dev.joncollins.mpapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    public String id;
    public String token;
    public TokenType tokenType = TokenType.BEARER;
    public boolean revoked;
    public boolean expired;
    public String user;

    @Override
    public String toString() {
        return "token: " + this.token + "\n" +
               "revoked: " + this.revoked + "\n" +
               "expired: " + this.expired + "\n" +
               "user: " + this.user;
    }

}
