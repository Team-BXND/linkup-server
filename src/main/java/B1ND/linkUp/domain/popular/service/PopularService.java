package B1ND.linkUp.domain.popular.service;

import B1ND.linkUp.domain.post.dto.response.ReadPostsResponse;
import B1ND.linkUp.domain.post.entity.Posts;
import B1ND.linkUp.domain.post.repository.PostsRepository;
import B1ND.linkUp.global.common.APIResponse;
import B1ND.linkUp.global.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PopularService {
    private final PostsRepository postsRepository;

    public PageResponse<ReadPostsResponse> getPopular(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Posts> postsPage = postsRepository.findPopular(pageable);

        return PageResponse.of(ReadPostsResponse.fromPage(postsPage), postsPage);
    }

    public PageResponse<ReadPostsResponse> getHotPopular(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        LocalDateTime monday = getWeekend();
        LocalDateTime sunday = monday.plusDays(6).withHour(23).withMinute(59).withSecond(59);
        Page<Posts> postsPage = postsRepository.findPopularPosts(monday, sunday, pageable);

        return PageResponse.of(ReadPostsResponse.fromPage(postsPage), postsPage);
    }

    private LocalDateTime getWeekend() {
        LocalDate today = LocalDate.now();
        int dayNumber = today.getDayOfWeek().getValue();
        return today.minusDays(dayNumber - 1).atStartOfDay();
    }
}
