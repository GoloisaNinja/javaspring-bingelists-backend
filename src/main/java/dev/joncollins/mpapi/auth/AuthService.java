package dev.joncollins.mpapi.auth;

import dev.joncollins.mpapi.Role;
import dev.joncollins.mpapi.Token;
import dev.joncollins.mpapi.TokenType;
import dev.joncollins.mpapi.User;
import dev.joncollins.mpapi.configuration.JwtService;
import dev.joncollins.mpapi.repositories.TokenRepository;
import dev.joncollins.mpapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repo;
    private final TokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest req) {
        var user = User.builder()
                .firstName(req.getFirstname())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .isPrivate(req.getIsPrivateAsBoolean())
                .tokens(new ArrayList<>())
                .invites(new ArrayList<>())
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        var jwtToken = jwtService.generateToken(user);
        var savedUser = repo.save(user);
        saveUserToken(savedUser, jwtToken);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()
                )
                                          );
        var user = repo.findUserByEmail(req.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user.getId())
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepo.save(token);
        user.addTokenToTokens(token);
        repo.save(user);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepo.findTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepo.saveAll(validUserTokens);
        List<Token> userTokensToRevoke = user.getValidTokens();
        if (!userTokensToRevoke.isEmpty()) {
            userTokensToRevoke.forEach(t -> {
                t.setExpired(true);
                t.setRevoked(true);
            });
            repo.save(user);
        }

    }
    public User returnUserDetailsByToken(String authHeader) {
        String token = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(token);
        User user = repo.findUserByEmail(userEmail).orElse(null);
        //User user = repo.findUsersByEmailExcludePasswordAndTokens(userEmail).orElse(null);
        return user;
    }

    public UserResponse getUserByToken(String auth) {
        User user = returnUserDetailsByToken(auth);
        if (user == null) {
            throw new NoSuchElementException();
        }
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getFirstName())
                .isPrivate(user.getIsPrivate())
                .invites(user.getInvites())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public List<UserResponse> getUsersByPrivacy() {
        List<User> users = repo.findUsersByIsPrivate().orElse(null);
        if (users == null) {
            throw new NoSuchElementException();
        }
        List<UserResponse> userList = new ArrayList<>();
        for (User user : users) {
            var resp = UserResponse.builder()
                    .id(user.getId())
                    .name(user.getFirstName())
                    .isPrivate(user.getIsPrivate())
                    .createdAt(user.getCreatedAt())
                    .build();
            userList.add(resp);
        }
        return userList;
    }

}
