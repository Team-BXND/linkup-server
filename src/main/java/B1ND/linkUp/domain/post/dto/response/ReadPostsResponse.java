package B1ND.linkUp.domain.post.dto.response;

import B1ND.linkUp.domain.post.entity.Category;
import B1ND.linkUp.domain.post.entity.Posts;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record ReadPostsResponse(
        Long id,
        String title,
        String author,
        Category category,
        int like,
        String preview,
        boolean isAccepted,
        int CommentCount,
        LocalDate createAt
) {
    public static ReadPostsResponse from(Posts posts) {
        return new ReadPostsResponse(
                posts.getId(),
                posts.getTitle(),
                posts.getAuthor(),
                posts.getCategory(),
                0,
                posts.getContent().substring(0,20),
                posts.isAccepted(),
                10,
                posts.getCreateAt()
        );
    }

    public static List<ReadPostsResponse> fromPage(Page<Posts> page) {
        return page.getContent().stream().map(
                posts -> ReadPostsResponse.from(posts)
        ).collect(Collectors.toList());
    }
}
