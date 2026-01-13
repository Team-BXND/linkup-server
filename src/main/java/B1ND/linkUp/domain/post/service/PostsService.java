package B1ND.linkUp.domain.post.service;

import B1ND.linkUp.domain.post.dto.request.CreatePostsRequest;
import B1ND.linkUp.domain.post.dto.request.ReadPostsRequest;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    private final PostsLikeRepository postsLikeRepository;

    public APIResponse<PageResponse<List<ReadPostsResponse>>> ReadPosts(int page, ReadPostsRequest req) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, req.category().toString()));
        Page<Posts> postsPage = postsRepository.findAllByIsAcceptedTrue(pageable);

        return APIResponse.ok(PageResponse.of(ReadPostsResponse.fromPage(postsPage), postsPage));
    }

    public APIResponse<ViewPostsResponse> viewPosts(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new PostsException(PostsErrorCode.POST_NOT_FOUND));

        boolean isLike = postsLikeRepository.existsByPostsIdAndMemberId(posts.getId(),1L); //memberId같은 경우 머지된 후 추가할 예정

        return APIResponse.ok(ViewPostsResponse.of(posts, isLike));
    }

    public APIResponse<ResponseEntity<MessageResponse>> createPosts(CreatePostsRequest req) {
        postsRepository.save(
                        Posts.builder()
                        .title(req.title())
                        .content(req.content())
                        .author(req.author())
                        .category(req.category())
                        .build()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("게시글이 작성되었습니다."));
    }
}