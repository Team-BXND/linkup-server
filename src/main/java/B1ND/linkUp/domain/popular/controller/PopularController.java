package B1ND.linkUp.domain.popular.controller;

import B1ND.linkUp.domain.popular.service.PopularService;
import B1ND.linkUp.domain.post.dto.response.ReadPostsResponse;
import B1ND.linkUp.global.common.APIResponse;
import B1ND.linkUp.global.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/popular")
public class PopularController {
    private final PopularService popularService;

    @GetMapping
    public APIResponse<PageResponse<ReadPostsResponse>> getPopular(
            @RequestParam(defaultValue = "0") int page) {
        return popularService.getPopular(page);
    }

    @GetMapping("/hot")
    public APIResponse<PageResponse<ReadPostsResponse>> getHotPopular(
            @RequestParam(defaultValue = "0") int page) {
        return popularService.getHotPopular(page);
    }
}
