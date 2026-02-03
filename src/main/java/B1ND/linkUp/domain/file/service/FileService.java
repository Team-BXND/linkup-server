package B1ND.linkUp.domain.file.service;

import B1ND.linkUp.domain.file.entity.File;
import B1ND.linkUp.domain.file.exception.FileErrorCode;
import B1ND.linkUp.domain.file.exception.FileException;
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
                throw new FileException(FileErrorCode.FILE_EMPTY);
            }
            String filename = file.getOriginalFilename();
            if (filename == null || !filename.contains(".")) {
                throw new FileException(FileErrorCode.FILE_EMPTY);
            }

            String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new FileException(FileErrorCode.INVALID_FILE_EXTENSION);
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

            return APIResponse.ok(s3Key);
        } catch (Exception e) {
            throw new FileException(FileErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public APIResponse<String> generatePresignedUrl(String s3Key) {
        Date expiration = new Date(System.currentTimeMillis() + PRESIGNED_URL_EXPIRATION_MS);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, s3Key)
                        .withMethod(com.amazonaws.HttpMethod.GET)
                        .withExpiration(expiration);

        return APIResponse.ok(amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString());
    }
}