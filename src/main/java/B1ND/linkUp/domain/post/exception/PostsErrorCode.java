package B1ND.linkUp.domain.post.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostsErrorCode {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_NOT_FOUND", "게시글을 찾을 수 없습니다."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "ANSWER_NOT_FOUND", "존재하지 않는 답변입니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
