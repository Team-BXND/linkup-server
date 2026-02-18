package B1ND.linkUp.domain.profile.dto.response;

import B1ND.linkUp.domain.post.entity.Posts;

import java.util.Locale;

public record MyQuestionItemResponse(
        Long id,
        String title,
        String preview,
        String category,
        int like,
        int commentCount,
        boolean isAccepted,
        int page
) {
    public static MyQuestionItemResponse of(Posts posts,
                                            int like,
                                            int commentCount,
                                            boolean isAccepted,
                                            int page) {
        String preview = posts.getContent().substring(0, Math.min(20, posts.getContent().length()));
        String category = posts.getCategory().name().toLowerCase(Locale.ROOT);

        return new MyQuestionItemResponse(
                posts.getId(),
                posts.getTitle(),
                preview,
                category,
                like,
                commentCount,
                isAccepted,
                page
        );
    }
}