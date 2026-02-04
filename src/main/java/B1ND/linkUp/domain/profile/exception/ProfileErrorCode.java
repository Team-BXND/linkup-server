package B1ND.linkUp.domain.profile.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProfileErrorCode {
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "PROFILE_NOT_FOUND", "프로필을 찾을 수 없습니다."),
    PROFILE_ALREADY_EXISTS(HttpStatus.CONFLICT, "PROFILE_ALREADY_EXISTS", "이미 프로필이 존재합니다."),
    PROFILE_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PROFILE_CREATE_FAILED", "프로필 생성에 실패했습니다."),
    PROFILE_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PROFILE_UPDATE_FAILED", "프로필 수정에 실패했습니다."),
    PROFILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PROFILE_DELETE_FAILED", "프로필 삭제에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}