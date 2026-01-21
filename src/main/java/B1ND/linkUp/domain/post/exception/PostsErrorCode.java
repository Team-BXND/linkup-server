package B1ND.linkUp.domain.post.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostsErrorCode {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_NOT_FOUND", "게시글을 찾을 수 없습니다."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "ANSWER_NOT_FOUND", "존재하지 않는 답변입니다."),
    ALREADY_ACCEPTED_ANSWER(HttpStatus.CONFLICT, "ALREADY_ACCEPTED_ANSWER", "이미 채택된 답변이 존재합니다."),
    NOT_POST_AUTHOR(HttpStatus.FORBIDDEN, "NOT_POST_AUTHOR", "해당 게시글에 대한 권한이 없습니다."),
    NOT_ANSWER_AUTHOR(HttpStatus.FORBIDDEN, "NOT_ANSWER_AUTHOR", "해당 답변에 대한 권한이 없습니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
