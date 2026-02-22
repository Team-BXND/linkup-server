package B1ND.linkUp.domain.post.service;

import B1ND.linkUp.domain.auth.entity.User;
import B1ND.linkUp.domain.post.entity.Posts;
import B1ND.linkUp.domain.post.entity.PostsLike;
import B1ND.linkUp.domain.post.exception.PostsErrorCode;
import B1ND.linkUp.domain.post.exception.PostsException;
import B1ND.linkUp.domain.post.repository.PostsLikeRepository;
import B1ND.linkUp.domain.post.repository.PostsRepository;
import B1ND.linkUp.global.common.APIResponse;
import B1ND.linkUp.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostsLikeService {
    private final SecurityUtil securityUtil;
    private final PostsRepository postsRepository;
    private final PostsLikeRepository postsLikeRepository;

    @Transactional
    public APIResponse<String> toggleLike(Long id) {
        User user = securityUtil.getUser();

        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new PostsException(PostsErrorCode.POST_NOT_FOUND));
        if (user.equals(posts.getUser())) {
            throw new PostsException(PostsErrorCode.CANNOT_LIKE_OWN_POST);
        }

        return postsLikeRepository.findByPosts_IdAndUser(id, user)
                .map(existingLike -> {
                    postsLikeRepository.delete(existingLike);
                    return APIResponse.ok("좋아요를 취소했습니다.");
                })
                .orElseGet(() -> {
                    PostsLike newLike = PostsLike.builder()
                            .posts(posts)
                            .user(user)
                            .build();
                    postsLikeRepository.save(newLike);
                    return APIResponse.ok("좋아요를 눌렀습니다.");
                });
    }
}
