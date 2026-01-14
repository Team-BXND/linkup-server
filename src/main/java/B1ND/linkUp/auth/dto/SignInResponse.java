package B1ND.linkUp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
}
