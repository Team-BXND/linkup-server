package B1ND.linkUp.domain.post.dto.response;

import B1ND.linkUp.domain.post.entity.PostsComment;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record ViewCommentsResponse(
        Long commentId,
        String author,
        String content,
        boolean isAccepted,
        LocalDate createdAt
) {
    public static ViewCommentsResponse of(PostsComment comment) {
        return new ViewCommentsResponse(
                comment.getId(),
                comment.getUser().getUsername(),
                comment.getContent(),
                comment.isAccepted(),
                comment.getCreateAt()
        );
    }

    public static List<ViewCommentsResponse> fromList(List<PostsComment> commentList) {
        return commentList
                .stream()
                .map(comment -> ViewCommentsResponse.of(comment))
                .collect(Collectors.toList());
    }
}
