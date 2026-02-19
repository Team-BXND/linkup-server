package B1ND.linkUp.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_001", "서버 에러가 발생했습니다."),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_001", "이메일 또는 비밀번호가 올바르지 않습니다."),
    EMAIL_ALREADY_USED(HttpStatus.CONFLICT, "AUTH_002", "이미 사용 중인 이메일입니다."),
    USERNAME_ALREADY_USED(HttpStatus.CONFLICT, "AUTH_003", "이미 사용 중인 닉네임입니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "AUTH_004", "비밀번호 형식이 올바르지 않습니다."),
    INVALID_EMAIL(HttpStatus.NOT_FOUND, "AUTH_005", "존재하지 않는 이메일입니다."),

    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "AUTH_006", "인증번호가 올바르지 않습니다."),
    EXPIRED_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "AUTH_007", "인증번호가 만료되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}