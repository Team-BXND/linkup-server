package B1ND.linkUp.domain.file.service;

import B1ND.linkUp.domain.file.entity.File;
import B1ND.linkUp.domain.file.repository.FileRepository;
import B1ND.linkUp.global.common.APIResponse;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final AmazonS3Client amazonS3Client;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp", "gif");
    private static final long PRESIGNED_URL_EXPIRATION_MINUTES = 10;
    private static final long PRESIGNED_URL_EXPIRATION_MS = PRESIGNED_URL_EXPIRATION_MINUTES * 60 * 1000;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public APIResponse<?> uploadPostImage(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return APIResponse.error(HttpStatus.BAD_REQUEST, "파일이 없습니다");
            }
            String filename = file.getOriginalFilename();
            if (filename == null || !filename.contains(".")) {
                return APIResponse.error(HttpStatus.BAD_REQUEST, "파일명이 유효하지 않습니다");
            }

            String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                return APIResponse.error(HttpStatus.BAD_REQUEST, "허용되지 않는 파일입니다.");
            }

            String s3Key = "post/" + UUID.randomUUID() + "." + extension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + extension);
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, s3Key, file.getInputStream(), metadata);

            fileRepository.save(File.builder()
                    .name(filename)
                    .url(s3Key)
                    .build());

            String presignedUrl = generatePresignedUrl(s3Key);

            return APIResponse.ok(presignedUrl);
        } catch (Exception e) {
            return APIResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "업로드 실패");
        }
    }
    
    private String generatePresignedUrl(String s3Key) {
        Date expiration = new Date(System.currentTimeMillis() + PRESIGNED_URL_EXPIRATION_MS);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, s3Key)
                        .withMethod(com.amazonaws.HttpMethod.GET)
                        .withExpiration(expiration);

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }
}