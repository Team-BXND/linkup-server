package B1ND.linkUp.domain.profile.dto.response;

import B1ND.linkUp.domain.post.entity.Category;
import B1ND.linkUp.domain.post.entity.Posts;
import B1ND.linkUp.domain.post.entity.PostsComment;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public record MyAnswerItemResponse(
        Long id,
        String title,
        String preview,
        Category category,
        String answer
) {
    public static MyAnswerItemResponse of(PostsComment comment) {
        Posts p = comment.getPosts();
        return new MyAnswerItemResponse(
                p.getId(),
                p.getTitle(),
                p.getContent().substring(0, Math.min(20, p.getContent().length())),
                p.getCategory(),
                comment.getContent()
        );
    }

    public static List<MyAnswerItemResponse> fromPage(Page<PostsComment> page) {
        return page.getContent().stream()
                .map(MyAnswerItemResponse::of)
                .collect(Collectors.toList());
    }
}