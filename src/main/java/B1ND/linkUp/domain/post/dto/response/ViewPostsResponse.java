package B1ND.linkUp.domain.post.dto.response;

import B1ND.linkUp.domain.post.entity.Category;
import B1ND.linkUp.domain.post.entity.Posts;

import java.time.LocalDate;

public record ViewPostsResponse(
        String title,
        String author,
        Category category,
        String content,
        int like,
        LocalDate createAt,
        boolean isAccepted,
        boolean isLike
) {
    public static ViewPostsResponse of(Posts posts, boolean isLike) {
        return new ViewPostsResponse(
                posts.getTitle(),
                posts.getAuthor(),
                posts.getCategory(),
                posts.getContent(),
                posts.likeCount(),
                posts.getCreateAt(),
                posts.isAccepted(),
                isLike
        );
    }
}
