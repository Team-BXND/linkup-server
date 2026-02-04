package B1ND.linkUp.domain.profile.exception;

import lombok.Getter;

@Getter
public class ProfileException extends RuntimeException {
    private ProfileErrorCode profileErrorCode;

    public ProfileException(ProfileErrorCode profileErrorCode) {
        super(profileErrorCode.getMessage());
        this.profileErrorCode = profileErrorCode;
    }

    public ProfileException(ProfileErrorCode profileErrorCode, String message) {
        super(message);
        this.profileErrorCode = profileErrorCode;
    }
}