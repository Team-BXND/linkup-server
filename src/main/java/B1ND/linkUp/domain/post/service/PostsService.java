package B1ND.linkUp.domain.post.service;

import B1ND.linkUp.domain.post.dto.request.ReadPostsReq;
import B1ND.linkUp.domain.post.dto.response.ReadPostsRes;
import B1ND.linkUp.domain.post.entity.Posts;
import B1ND.linkUp.domain.post.repository.PostsRepository;
import B1ND.linkUp.global.common.APIResponse;
import B1ND.linkUp.global.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;

    public APIResponse<PageResponse<List<ReadPostsRes>>> ReadPosts(int page, ReadPostsReq req) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, req.category().toString()));
        Page<Posts> postsPage = postsRepository.findAllByIsAcceptedTrue(pageable);

        return APIResponse.ok(PageResponse.of(ReadPostsRes.fromPage(postsPage), postsPage));
    }
}
