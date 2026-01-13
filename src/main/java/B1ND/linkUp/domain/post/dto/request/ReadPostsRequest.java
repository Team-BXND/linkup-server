package B1ND.linkUp.domain.post.dto.request;

import B1ND.linkUp.domain.post.entity.Category;
import jakarta.validation.constraints.NotBlank;

public record ReadPostsRequest(
        Category category
) {
}
