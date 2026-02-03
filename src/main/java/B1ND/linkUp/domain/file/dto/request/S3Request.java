package B1ND.linkUp.domain.file.dto.request;

import jakarta.validation.constraints.NotBlank;

public record S3Request(
        @NotBlank(message = "s3Key는 필수입니다.")
        String s3key
) {
}
