package InterViewR.domain.personality;

import InterViewR.domain.security.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Data
@Builder
@Table(name = "Input")
public class Input {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter
    private String inputText;

    @Getter
    @Setter
    private String response;

    public String getInputText() {
        return inputText;
    }
}
