package dev.joncollins.mpapi.auth;


import dev.joncollins.mpapi.configuration.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {
   private final AuthService service;
   private final LogoutService logoutService;
    @PostMapping("/new/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(service.register(req));
    }
    @PostMapping("/new/authenticate")
    public ResponseEntity<PrivateUserResponse> authenticate(@RequestBody AuthRequest req) {
        return ResponseEntity.ok(service.authenticate(req));
    }
    @GetMapping("/user")
    public ResponseEntity<PublicUserResponse> fetchUserByToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        return ResponseEntity.ok(service.getUserByToken(auth));
    }
    @GetMapping("/users")
    public ResponseEntity<List<PublicUserResponse>> fetchUsersByPrivacy() {
        return ResponseEntity.ok(service.getUsersByPrivacy());
    }

}
