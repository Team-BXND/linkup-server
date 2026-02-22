package B1ND.linkUp.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponse<T> {

    private int status;
    private List<T> data;
    private PageMeta meta;

    @Getter
    @AllArgsConstructor
    public static class PageMeta {
        private int currentPage;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean hasNext;
        private boolean hasPrevious;
    }

    public static <T> PageResponse<T> of(List<T> data, Page<?> page) {
        PageMeta meta = new PageMeta(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
        return new PageResponse<>(HttpStatus.OK.value(), data, meta);
    }
}
