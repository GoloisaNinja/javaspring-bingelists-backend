package dev.joncollins.mpapi.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
   private final AuthService service;
    @PostMapping("/new/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(service.register(req));
    }
    @PostMapping("/new/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(service.authenticate(req));
    }
    @GetMapping("/user")
    public ResponseEntity<UserResponse> fetchUserByToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return ResponseEntity.ok(service.getUserByToken(auth));
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> fetchUsersByPrivacy() {
        return ResponseEntity.ok(service.getUsersByPrivacy());
    }
}
