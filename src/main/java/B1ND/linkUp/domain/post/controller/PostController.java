package B1ND.linkUp.domain.post.controller;

import B1ND.linkUp.domain.post.dto.request.CreatePostsRequest;
import B1ND.linkUp.domain.post.dto.request.PostsCommentRequest;
import B1ND.linkUp.domain.post.dto.request.ReadPostsRequest;
import B1ND.linkUp.domain.post.dto.request.UpdatePostsRequest;
import B1ND.linkUp.domain.post.dto.response.MessageResponse;
import B1ND.linkUp.domain.post.dto.response.ReadPostsResponse;
import B1ND.linkUp.domain.post.dto.response.ViewPostsResponse;
import B1ND.linkUp.domain.post.entity.PostsComment;
import B1ND.linkUp.domain.post.service.PostsCommentService;
import B1ND.linkUp.domain.post.service.PostsLikeService;
import B1ND.linkUp.domain.post.service.PostsService;
import B1ND.linkUp.global.common.APIResponse;
import B1ND.linkUp.global.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostsService postsService;
    private final PostsCommentService postsCommentService;
    private final PostsLikeService postsLikeService;

    @GetMapping
    public APIResponse<PageResponse<ReadPostsResponse>> ReadAll(
            @RequestParam(defaultValue = "0") int page,
            @Valid @RequestBody ReadPostsRequest req
    ) {
        return postsService.ReadPosts(page, req);
    }

    @GetMapping("/{id}")
    public APIResponse<ViewPostsResponse> ViewPosts(
            @PathVariable Long id) {
        return postsService.viewPosts(id);
    }

    @PostMapping
    public APIResponse<MessageResponse> createPosts(
            @Valid @RequestBody CreatePostsRequest request) {
        return postsService.createPosts(request);
    }

    @PatchMapping("/{id}")
    public APIResponse<MessageResponse> updatePosts(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostsRequest request) {
        return postsService.updatePosts(id, request);
    }

    @DeleteMapping("/{id}")
    public APIResponse<MessageResponse> deletePosts(
            @PathVariable Long id) {
        return postsService.deletePosts(id);
    }

    @PostMapping("/{id}/answer")
    public APIResponse<MessageResponse> createPostsComment(
            @PathVariable Long id,
            @RequestBody PostsCommentRequest request) {
        return postsCommentService.createComment(id, request);
    }

    @DeleteMapping("/{id}/answer")
    public APIResponse<MessageResponse> deleteComment(
            @PathVariable Long id) {
        return postsCommentService.deleteComment(id);
    }

    @PostMapping("/{id}/accept/{ansid}")
    public APIResponse<MessageResponse> AnswerAcceptanceService(
            @PathVariable Long id,
            @PathVariable Long ansid) {
        return postsCommentService.AnswerAcceptanceService(id, ansid);
    }

    @PostMapping("/{id}/like")
    public APIResponse<String> toggleLike(
            @PathVariable Long id) {
        return postsLikeService.toggleLike(id);
    }
}
