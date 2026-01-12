package B1ND.linkUp.domain.post.controller;

import B1ND.linkUp.domain.post.dto.request.ReadPostsRequest;
import B1ND.linkUp.domain.post.dto.response.ReadPostsResponse;
import B1ND.linkUp.domain.post.service.PostsService;
import B1ND.linkUp.global.common.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
    public APIResponse<List<ReadPostsResponse>> ReadAll(
            @RequestParam(defaultValue = "0") int page,
            @Valid @RequestBody ReadPostsRequest req) {
        return postsService.ReadPosts(page, req);
    }
}
