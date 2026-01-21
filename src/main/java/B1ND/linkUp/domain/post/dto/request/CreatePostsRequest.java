package B1ND.linkUp.domain.post.dto.request;

import B1ND.linkUp.domain.post.entity.Category;
import jakarta.validation.constraints.NotBlank;

public record CreatePostsRequest(
        Category category,
        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        String title,
        @NotBlank(message = "내용은 필수 입력 항목입니다.")
        String content,
        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
        String author
) {
}
