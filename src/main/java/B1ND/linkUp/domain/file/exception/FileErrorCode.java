package B1ND.linkUp.domain.file.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FileErrorCode {
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE_NOT_FOUND", "파일을 찾을 수 없습니다."),
    FILE_EMPTY(HttpStatus.BAD_REQUEST, "FILE_EMPTY", "파일이 없습니다."),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "INVALID_FILE_NAME", "파일명이 유효하지 않습니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "INVALID_FILE_EXTENSION", "허용되지 않는 파일입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_UPLOAD_FAILED", "업로드 실패");

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
