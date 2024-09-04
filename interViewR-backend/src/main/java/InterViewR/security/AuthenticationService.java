package InterViewR.security;

import InterViewR.requests.AnalyzePersonalityRequest;
import InterViewR.requests.SignInRequest;
import InterViewR.requests.SignUpRequest;
import InterViewR.responses.JwtAuthenticationResponse;

import java.util.List;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);

    JwtAuthenticationResponse analyzePersonality(AnalyzePersonalityRequest request);

    List<String> getUserHistory(String email);
}