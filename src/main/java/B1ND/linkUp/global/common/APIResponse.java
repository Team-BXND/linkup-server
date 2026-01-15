package B1ND.linkUp.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class APIResponse<T> {
    private int status;
    private T data;

    public static <T> APIResponse<T> of(HttpStatus httpStatus, T data) {
        return new APIResponse<>(httpStatus.value(), data);
    }

    public static <T> APIResponse<T> ok(T data) {
        return of(HttpStatus.OK, data);
    }

    public static <T> APIResponse<T> error(HttpStatus httpStatus, T data) {
        return new APIResponse<>(httpStatus.value(), data);
    }
}
