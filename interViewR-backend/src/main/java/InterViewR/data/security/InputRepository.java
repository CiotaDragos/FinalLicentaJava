package InterViewR.data.security;

import InterViewR.domain.personality.Input;
import InterViewR.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface  InputRepository extends JpaRepository<Input, UUID> {
    List<Input> findByUser(User user);
}
