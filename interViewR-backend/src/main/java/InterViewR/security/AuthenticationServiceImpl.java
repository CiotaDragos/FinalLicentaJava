package InterViewR.security;

import InterViewR.data.security.InputRepository;
import InterViewR.data.security.UserRepository;
import InterViewR.domain.personality.Input;
import InterViewR.domain.security.Role;
import InterViewR.domain.security.User;
import InterViewR.requests.AnalyzePersonalityRequest;
import InterViewR.requests.SignInRequest;
import InterViewR.requests.SignUpRequest;
import InterViewR.responses.JwtAuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    private final InputRepository inputRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String pythonServerUrl = "http://localhost:5000/predict";

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(Role.User)).build();
        User savedUser = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).userId(savedUser.getId()).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        User savedUser = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).userId(savedUser.getId()).build();
    }

    @Override
    public JwtAuthenticationResponse analyzePersonality(AnalyzePersonalityRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // Set up headers and the HTTP entity with the request body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        HttpEntity<AnalyzePersonalityRequest> requestEntity = new HttpEntity<>(request, headers);

        // Make the POST request to the Flask server
        ResponseEntity<String> response = restTemplate.exchange(
                pythonServerUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Get the predicted label from the response
        String predictedLabel = response.getBody();
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        Input input = Input.builder().user(user).inputText(request.getTextToAnalyze())
                .response(String.valueOf(predictedLabel)).build();
        Input savedInput = inputRepository.save(input);

        // Return the result wrapped in JwtAuthenticationResponse or any other custom response
        return JwtAuthenticationResponse.builder()
                .token(String.valueOf(predictedLabel))  // Just an example, adjust as needed
                .build();
    }

    @Override
    public List<String> getUserHistory(String email) {
        // Find the user by email
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Retrieve the inputs (history) associated with the user
        List<Input> inputs = user.getInputs();
        List<String> results = new ArrayList<>();
        for (Input input : inputs){
            results.add(input.getInputText());
        }

        // Convert Input entities to JwtAuthenticationResponse DTOs
        return results;
    }
}