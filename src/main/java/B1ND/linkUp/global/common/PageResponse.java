package B1ND.linkUp.global.common;

import B1ND.linkUp.domain.post.dto.response.ReadPostsResponse;
import B1ND.linkUp.domain.post.entity.Posts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public static PageResponse of(List<ReadPostsResponse> content, Page<Posts> postsPage) {
        return new PageResponse(
                content,
                postsPage.getNumber(),
                postsPage.getSize(),
                postsPage.getTotalElements(),
                postsPage.getTotalPages(),
                postsPage.hasNext(),
                postsPage.hasPrevious()
        );
    }
}
