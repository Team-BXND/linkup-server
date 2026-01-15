package B1ND.linkUp.domain.post.service;

import B1ND.linkUp.domain.auth.entity.User;
import B1ND.linkUp.domain.post.dto.request.CreatePostsRequest;
import B1ND.linkUp.domain.post.dto.request.ReadPostsRequest;
import B1ND.linkUp.domain.post.dto.request.UpdatePostsRequest;
import B1ND.linkUp.domain.post.dto.response.MessageResponse;
import B1ND.linkUp.domain.post.dto.response.ReadPostsResponse;
import B1ND.linkUp.domain.post.dto.response.ViewPostsResponse;
import B1ND.linkUp.domain.post.entity.Posts;
import B1ND.linkUp.domain.post.exception.PostsErrorCode;
import B1ND.linkUp.domain.post.exception.PostsException;
import B1ND.linkUp.domain.post.repository.PostsLikeRepository;
import B1ND.linkUp.domain.post.repository.PostsRepository;
import B1ND.linkUp.global.common.APIResponse;
import B1ND.linkUp.global.common.PageResponse;
import B1ND.linkUp.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    private final PostsLikeRepository postsLikeRepository;
    private final SecurityUtil securityUtil;

    public APIResponse<PageResponse<List<ReadPostsResponse>>> ReadPosts(int page, ReadPostsRequest req) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Posts> postsPage;

        if (req.category() == null || req.category().toString().equalsIgnoreCase("all")) {
            postsPage = postsRepository.findAll(pageable);
        } else {
            postsPage = postsRepository.findByCategory(req.category(), pageable);
        }

        return APIResponse.ok(PageResponse.of(ReadPostsResponse.fromPage(postsPage), postsPage));
    }

    public APIResponse<ViewPostsResponse> viewPosts(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new PostsException(PostsErrorCode.POST_NOT_FOUND));

        User user = securityUtil.getUser();

        boolean isLike = postsLikeRepository.existsByPosts_IdAndUser(posts.getId(),user);

        return APIResponse.ok(ViewPostsResponse.of(posts, isLike));
    }

    public APIResponse<MessageResponse> createPosts(CreatePostsRequest req) {
        User user = securityUtil.getUser();
        postsRepository.save(
                Posts.builder()
                        .title(req.title())
                        .content(req.content())
                        .author(req.author())
                        .category(req.category())
                        .user(user)
                        .build()
        );
        return APIResponse.of(HttpStatus.CREATED, MessageResponse.of("게시글이 등록되었습니다."));
    }

    public APIResponse<MessageResponse> updatePosts(Long id, UpdatePostsRequest request) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new PostsException(PostsErrorCode.POST_NOT_FOUND));

        User user = securityUtil.getUser();
        if(!user.equals(posts.getUser())) {
            throw new PostsException(PostsErrorCode.NOT_POST_AUTHOR);
        }

        posts.updatePosts(request);
        postsRepository.save(posts);
        return APIResponse.ok(MessageResponse.of("게시글이 수정되었습니다."));
    }

    public APIResponse<MessageResponse> deletePosts(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new PostsException(PostsErrorCode.POST_NOT_FOUND));

        User user = securityUtil.getUser();
        if(!user.equals(posts.getUser())) {
            throw new PostsException(PostsErrorCode.NOT_POST_AUTHOR);
        }

        postsRepository.delete(posts);
        return APIResponse.ok(MessageResponse.of("게시글이 삭제되었습니다."));
    }
}