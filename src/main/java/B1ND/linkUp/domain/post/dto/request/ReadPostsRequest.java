package B1ND.linkUp.domain.post.dto.request;

import B1ND.linkUp.domain.post.entity.Category;
import jakarta.validation.constraints.NotBlank;

public record ReadPostsRequest(
        @NotBlank(message = "카테고리를 선택해주세요.")
        Category category
) {
}
