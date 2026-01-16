package B1ND.linkUp.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 1, max = 50)
    private String username;

    @NotBlank
    private String password;
}
