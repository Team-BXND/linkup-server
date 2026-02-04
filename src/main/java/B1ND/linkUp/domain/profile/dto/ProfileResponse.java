package B1ND.linkUp.domain.profile.dto;

import B1ND.linkUp.domain.profile.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private Long id;
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProfileResponse from(Profile profile, String profileImageUrl) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .nickname(profile.getNickname())
                .profileImageUrl(profileImageUrl)
                .bio(profile.getBio())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}