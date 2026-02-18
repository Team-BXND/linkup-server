package B1ND.linkUp.domain.profile.dto.response;
import B1ND.linkUp.domain.post.entity.Posts;
import B1ND.linkUp.domain.post.entity.PostsComment;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Locale;

public record MyAnswerItemResponse(
        Long id,
        String title,
        String preview,
        String category,
        String answer
) {
    public static MyAnswerItemResponse of(PostsComment comment) {
        Posts p = comment.getPosts();

        String preview = p.getContent().substring(0, Math.min(20, p.getContent().length()));
        String category = p.getCategory().name().toLowerCase(Locale.ROOT);

        return new MyAnswerItemResponse(
                p.getId(),
                p.getTitle(),
                preview,
                category,
                comment.getContent()
        );
    }

    public static List<MyAnswerItemResponse> fromPage(Page<PostsComment> page) {
        return page.getContent().stream()
                .map(MyAnswerItemResponse::of)
                .toList();
    }
}