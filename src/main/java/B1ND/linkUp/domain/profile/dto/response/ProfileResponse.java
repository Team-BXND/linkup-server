package B1ND.linkUp.domain.profile.dto.response;

import B1ND.linkUp.domain.auth.entity.User;

public record ProfileResponse(
        String username,
        String email
) {
    public static ProfileResponse of(User user) {
        return new ProfileResponse(user.getUsername(), user.getEmail());
    }
}