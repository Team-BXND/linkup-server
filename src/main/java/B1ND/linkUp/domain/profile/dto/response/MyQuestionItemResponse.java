package B1ND.linkUp.domain.profile.dto.response;

import B1ND.linkUp.domain.post.entity.Category;
import B1ND.linkUp.domain.post.entity.Posts;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public record MyQuestionItemResponse(
        Long id,
        String title,
        String preview,
        Category category,
        int commentCount
) {
    public static MyQuestionItemResponse of(Posts posts, int commentCount) {
        return new MyQuestionItemResponse(
                posts.getId(),
                posts.getTitle(),
                posts.getContent().substring(0, Math.min(20, posts.getContent().length())),
                posts.getCategory(),
                commentCount
        );
    }

    public static List<MyQuestionItemResponse> fromPage(Page<Posts> page, java.util.function.ToIntFunction<Long> commentCountFn) {
        return page.getContent().stream()
                .map(p -> MyQuestionItemResponse.of(p, commentCountFn.applyAsInt(p.getId())))
                .collect(Collectors.toList());
    }
}