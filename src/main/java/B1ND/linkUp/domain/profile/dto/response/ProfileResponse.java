package B1ND.linkUp.domain.profile.dto.response;

public record ProfileResponse(
        String username,
        String email,
        int point,
        long ranking
) {
}