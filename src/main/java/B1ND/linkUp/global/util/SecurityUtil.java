package B1ND.linkUp.global.util;

import B1ND.linkUp.domain.auth.entity.User;
import B1ND.linkUp.domain.auth.repository.UserRepository;
import B1ND.linkUp.global.exception.CustomException;
import B1ND.linkUp.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final UserRepository userRepository;

    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_EMAIL));
    }
}
