package B1ND.linkUp.domain.post.exception;

import lombok.Getter;

@Getter
public class PostsException extends RuntimeException {
    private PostsErrorCode postsErrorCode;

    public PostsException(PostsErrorCode postsErrorCode) {
        super(postsErrorCode.getMessage());
        this.postsErrorCode = postsErrorCode;
    }

    public PostsException(PostsErrorCode postsErrorCode, String message) {
        super(message);
        this.postsErrorCode = postsErrorCode;
    }
}
