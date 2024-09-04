package InterViewR.services.security;

import InterViewR.data.security.InputRepository;
import InterViewR.data.security.UserRepository;
import InterViewR.domain.personality.Input;
import InterViewR.domain.security.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InputServiceImpl implements InputService{
    private final InputRepository inputRepository;
    private final UserRepository userRepository;

    public Input saveInputForUser(UUID userId, String text, String result) {
        // Find the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Create a new input
        Input input = new Input();
        input.setInputText(text);
        input.setResponse(result);
        input.setUser(user);

        // Add the input to the user's inputs list
        user.getInputs().add(input);

        // Save the input (cascading should handle saving the user’s inputs)
        return inputRepository.save(input);
    }
}
