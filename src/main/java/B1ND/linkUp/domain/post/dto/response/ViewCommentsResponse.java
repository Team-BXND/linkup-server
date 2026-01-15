package B1ND.linkUp.domain.post.dto.response;

import B1ND.linkUp.domain.post.entity.PostsComment;

import java.util.List;
import java.util.stream.Collectors;

public record ViewCommentsResponse(
        Long id,
        String author,
        String content
) {
    public static ViewCommentsResponse of(PostsComment comment) {
        return new ViewCommentsResponse(
                comment.getId(),
                comment.getUser().getUsername(),
                comment.getContent()
        );
    }

    public static List<ViewCommentsResponse> fromList(List<PostsComment> commentList) {
        return commentList
                .stream()
                .map(comment -> ViewCommentsResponse.of(comment))
                .collect(Collectors.toList());
    }
}
