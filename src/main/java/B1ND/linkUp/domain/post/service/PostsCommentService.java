package B1ND.linkUp.domain.post.service;

import B1ND.linkUp.domain.post.dto.request.PostsCommentRequest;
import B1ND.linkUp.domain.post.dto.response.MessageResponse;
import B1ND.linkUp.domain.post.entity.Posts;
import B1ND.linkUp.domain.post.entity.PostsComment;
import B1ND.linkUp.domain.post.exception.PostsErrorCode;
import B1ND.linkUp.domain.post.exception.PostsException;
import B1ND.linkUp.domain.post.repository.PostsCommentRepository;
import B1ND.linkUp.domain.post.repository.PostsRepository;
import B1ND.linkUp.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostsCommentService {
    private final PostsRepository postsRepository;
    private final PostsCommentRepository postsCommentRepository;

    public APIResponse<MessageResponse> createComment(Long id, PostsCommentRequest request) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new PostsException(PostsErrorCode.POST_NOT_FOUND));

        postsCommentRepository.save(PostsComment.builder()
                .content(request.content())
                .posts(posts)
                .build());
        return APIResponse.ok(MessageResponse.of("답변이 등록되었습니다."));
    }
}
