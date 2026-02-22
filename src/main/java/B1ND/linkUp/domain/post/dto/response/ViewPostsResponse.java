package B1ND.linkUp.domain.post.dto.response;

import B1ND.linkUp.domain.post.entity.Category;
import B1ND.linkUp.domain.post.entity.Posts;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record ViewPostsResponse(
        String title,
        String author,
        Category category,
        String content,
        int like,
        LocalDate createdAt,
        boolean isAccepted,
        boolean isLike,
        boolean isAuthor,
        List<ViewCommentsResponse> comment
) {
    public static ViewPostsResponse of(Posts posts, boolean isLike, boolean isAuthor) {
        return new ViewPostsResponse(
                posts.getTitle(),
                posts.getAuthor(),
                posts.getCategory(),
                posts.getContent(),
                posts.likeCount(),
                posts.getCreateAt(),
                posts.isAccepted(),
                isLike,
                isAuthor,
                ViewCommentsResponse.fromList(posts.getComments())
        );
    }
}
