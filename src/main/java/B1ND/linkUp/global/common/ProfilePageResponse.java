package B1ND.linkUp.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProfilePageResponse<T> {

    private List<T> data;
    private Meta meta;

    @Getter
    @AllArgsConstructor
    public static class Meta {
        private long total;
        private int page;
        private int pageSize;
        private int totalPages;
        private boolean hasNext;
        private boolean hasPrevious;
    }

    public static <T> ProfilePageResponse<T> of(List<T> data, Page<?> page) {
        return new ProfilePageResponse<>(
                data,
                new Meta(
                        page.getTotalElements(),
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalPages(),
                        page.hasNext(),
                        page.hasPrevious()
                )
        );
    }
}