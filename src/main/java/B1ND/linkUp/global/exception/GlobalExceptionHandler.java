package B1ND.linkUp.global.exception;

import B1ND.linkUp.domain.post.exception.PostsException;
import B1ND.linkUp.global.common.APIResponse;
import B1ND.linkUp.global.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(PostsException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> handlePostsException(PostsException e) {
        ErrorResponse errorResponse = ErrorResponse.of(
                e.getPostsErrorCode().getCode(),
                e.getPostsErrorCode().getMessage()
        );
        return ResponseEntity
                .status(e.getPostsErrorCode().getHttpStatus())
                .body(APIResponse.error(e.getPostsErrorCode().getHttpStatus(), errorResponse));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("NoResourceFoundException: {}", e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                "NOT_FOUND",
                "존재하지 않는 엔드포인트입니다."
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(APIResponse.error(HttpStatus.NOT_FOUND, errorResponse));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("NoHandlerFoundException: {}", e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                "NOT_FOUND",
                "요청한 리소스를 찾을 수 없습니다."
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(APIResponse.error(HttpStatus.NOT_FOUND, errorResponse));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: {}", e.getMessage());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.error(HttpStatus.BAD_REQUEST, errors));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> handleDataAccessException(DataAccessException e) {
        log.error("Database error occurred: ", e);

        ErrorResponse errorResponse = ErrorResponse.of(
                "DATABASE_ERROR",
                "데이터베이스 오류가 발생했습니다."
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<ErrorResponse>> handleException(Exception e) {
        log.error("Unexpected error occurred: ", e);

        ErrorResponse errorResponse = ErrorResponse.of(
                "INTERNAL_SERVER_ERROR",
                "서버 내부 오류가 발생했습니다."
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<APIResponse<ErrorResponse>> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        ErrorResponse errorResponse = ErrorResponse.of(
                errorCode.name(),
                errorCode.getMessage()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(APIResponse.error(errorCode.getStatus(),
                        errorResponse));
    }
}