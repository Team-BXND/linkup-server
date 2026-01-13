package B1ND.linkUp.domain.post.controller;

import B1ND.linkUp.domain.post.dto.request.CreatePostsRequest;
import B1ND.linkUp.domain.post.dto.request.ReadPostsRequest;
import B1ND.linkUp.domain.post.dto.response.MessageResponse;
import B1ND.linkUp.domain.post.dto.response.ReadPostsResponse;
import B1ND.linkUp.domain.post.dto.response.ViewPostsResponse;
import B1ND.linkUp.domain.post.service.PostsService;
import B1ND.linkUp.global.common.APIResponse;
import B1ND.linkUp.global.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public APIResponse<PageResponse<List<ReadPostsResponse>>> ReadAll(
            @RequestParam(defaultValue = "0") int page,
            @Valid @RequestBody ReadPostsRequest req) {
        return postsService.ReadPosts(page, req);
    }

    @GetMapping("/{id}")
    public APIResponse<ViewPostsResponse> ViewPosts(
            @PathVariable Long id) {
        return postsService.viewPosts(id);
    }

    @PostMapping
    public APIResponse<ResponseEntity<MessageResponse>> createPosts(
            @Valid @RequestBody CreatePostsRequest request) {
        return postsService.createPosts(request);
    }
}
