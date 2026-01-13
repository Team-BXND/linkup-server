package B1ND.linkUp.domain.post.dto.request;

import B1ND.linkUp.domain.post.entity.Category;

public record UpdatePostsRequest(
        Category category,
        String title,
        String content,
        String author
) {
}
