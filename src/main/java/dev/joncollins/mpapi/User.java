package dev.joncollins.mpapi;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
@Data
@Builder
@Document
public class User implements UserDetails {
    @Id
    private String id;
    private String firstName;
    @Indexed(unique = true)
    private String email;
    private String password;
    private Boolean isPrivate;
    private List<Token> tokens;
    private List<Invite> invites;
    private Role role;
    private LocalDateTime createdAt;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setIsPrivate(Boolean designation) {
        if (this.isPrivate == designation) {
            return;
        }
        this.isPrivate = designation;
    }
    public void addTokenToTokens(Token token) {
        this.tokens.add(token);
    }

    public List<Token> getValidTokens() {
        if (this.tokens == null) {
            return null;
        }
        return this.tokens.stream().filter((t) -> !t.expired && !t.revoked).toList();
    }

    public boolean addInvite(Invite i) {
        if (this.invites.contains(i)) {
            return false;
        }
        this.invites.add(i);
        return true;
    }

    public boolean deleteInvite(Invite i) {
        if (!this.invites.contains(i)) {
            return false;
        }
        this.invites.remove(i);
        return true;
    }

}
