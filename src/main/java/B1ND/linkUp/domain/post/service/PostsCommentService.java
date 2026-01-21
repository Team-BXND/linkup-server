package B1ND.linkUp.domain.post.service;

import B1ND.linkUp.domain.auth.entity.User;
import B1ND.linkUp.domain.post.dto.request.PostsCommentRequest;
import B1ND.linkUp.domain.post.dto.response.MessageResponse;
import B1ND.linkUp.domain.post.entity.Posts;
import B1ND.linkUp.domain.post.entity.PostsComment;
import B1ND.linkUp.domain.post.exception.PostsErrorCode;
import B1ND.linkUp.domain.post.exception.PostsException;
import B1ND.linkUp.domain.post.repository.PostsCommentRepository;
import B1ND.linkUp.domain.post.repository.PostsRepository;
import B1ND.linkUp.global.common.APIResponse;
import B1ND.linkUp.global.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostsCommentService {
    private final PostsRepository postsRepository;
    private final PostsCommentRepository postsCommentRepository;
    private final SecurityUtil securityUtil;


    @Transactional
    public APIResponse<MessageResponse> createComment(Long id, PostsCommentRequest request) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new PostsException(PostsErrorCode.POST_NOT_FOUND));

        User user = securityUtil.getUser();

        postsCommentRepository.save(PostsComment.builder()
                .content(request.content())
                .posts(posts)
                .user(user)
                .build());
        return APIResponse.ok(MessageResponse.of("답변이 등록되었습니다."));
    }

    @Transactional
    public APIResponse<MessageResponse> deleteComment(Long id) {
        PostsComment comment = postsCommentRepository.findById(id)
                .orElseThrow(() -> new PostsException(PostsErrorCode.ANSWER_NOT_FOUND));

        User user = securityUtil.getUser();

        if(!user.equals(comment.getUser())) {
            throw new PostsException(PostsErrorCode.NOT_POST_AUTHOR);
        }
        postsCommentRepository.delete(comment);

        return APIResponse.ok(MessageResponse.of("답변이 삭제되었습니다."));
    }

    @Transactional
    public APIResponse<MessageResponse> AnswerAcceptanceService(Long id, Long answerId) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new PostsException(PostsErrorCode.POST_NOT_FOUND));
        User user = securityUtil.getUser();

        if (posts.isAccepted()) {
            throw new PostsException(PostsErrorCode.ALREADY_ACCEPTED_ANSWER);
        }
        if (!user.equals(posts.getUser())) {
            throw new PostsException(PostsErrorCode.NOT_POST_AUTHOR);
        }

        PostsComment comment = postsCommentRepository.findByIdAndPosts(answerId, posts)
                .orElseThrow(() -> new PostsException(PostsErrorCode.ANSWER_NOT_FOUND));

        posts.setAccepted(comment);
        postsRepository.save(posts);

        return APIResponse.ok(MessageResponse.of("답변이 채택되었습니다."));
    }
}
