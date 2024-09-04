package InterViewR.controller;

import InterViewR.domain.personality.Input;
import InterViewR.requests.AnalyzePersonalityRequest;
import InterViewR.requests.SignInRequest;
import InterViewR.requests.SignUpRequest;
import InterViewR.responses.AnalyzePersonalityResponse;
import InterViewR.responses.JwtAuthenticationResponse;
import InterViewR.security.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @PostMapping("/analyze")
    public ResponseEntity<JwtAuthenticationResponse> analizepersonality(@RequestBody AnalyzePersonalityRequest request){
        return ResponseEntity.ok(authenticationService.analyzePersonality(request));
    }

    @GetMapping("/history")
    public ResponseEntity<List<String>> getUserHistory(@RequestParam String email) {
        List<String> history = authenticationService.getUserHistory(email);
        return ResponseEntity.ok(history);
    }
}