package B1ND.linkUp.domain.file.service;

import B1ND.linkUp.global.common.APIResponse;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FIleService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.region}")
    private String region;

    public APIResponse<?> uploadPostImage(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return APIResponse.error(HttpStatus.BAD_REQUEST, "파일이 없습니다");
            }

            String filename = UUID.randomUUID() + ".jpg";
            String s3Key = "post/" + filename;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, s3Key, file.getInputStream(), metadata);

            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + s3Key;
            return APIResponse.ok(fileUrl);
        } catch (Exception e) {
            return APIResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "업로드 실패");
        }
    }
}
