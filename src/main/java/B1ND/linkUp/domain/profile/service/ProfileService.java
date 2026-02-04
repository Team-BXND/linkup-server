package B1ND.linkUp.domain.profile.service;

import B1ND.linkUp.domain.file.service.FileService;
import B1ND.linkUp.domain.profile.dto.ProfileRequest;
import B1ND.linkUp.domain.profile.dto.ProfileResponse;
import B1ND.linkUp.domain.profile.entity.Profile;
import B1ND.linkUp.domain.profile.exception.ProfileErrorCode;
import B1ND.linkUp.domain.profile.exception.ProfileException;
import B1ND.linkUp.domain.profile.repository.ProfileRepository;
import B1ND.linkUp.global.common.APIResponse;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final FileService fileService;
    private final AmazonS3Client amazonS3Client;
    private static final long PRESIGNED_URL_EXPIRATION_MS = 10 * 60 * 1000;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public APIResponse<ProfileResponse> createProfile(Long userId, ProfileRequest request, MultipartFile profileImage) {
        try {
            if (profileRepository.existsByUserId(userId)) {
                throw new ProfileException(ProfileErrorCode.PROFILE_ALREADY_EXISTS);
            }

            String profileImageKey;

            if (profileImage != null && !profileImage.isEmpty()) {
                profileImageKey = uploadImageToS3(profileImage);
            } else {
                profileImageKey = fileService.getDefaultProfileImageKey();
            }

            Profile profile = Profile.builder()
                    .userId(userId)
                    .nickname(request.getNickname())
                    .profileImageKey(profileImageKey)
                    .bio(request.getBio())
                    .build();

            Profile savedProfile = profileRepository.save(profile);
            String presignedUrl = generatePresignedUrl(savedProfile.getProfileImageKey());

            return APIResponse.ok(ProfileResponse.from(savedProfile, presignedUrl));
        } catch (ProfileException e) {
            throw e;
        } catch (Exception e) {
            throw new ProfileException(ProfileErrorCode.PROFILE_CREATE_FAILED);
        }
    }

    @Transactional
    public APIResponse<ProfileResponse> getProfile(Long userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

        String presignedUrl = generatePresignedUrl(profile.getProfileImageKey());
        return APIResponse.ok(ProfileResponse.from(profile, presignedUrl));
    }

    @Transactional
    public APIResponse<ProfileResponse> updateProfile(Long userId, ProfileRequest request) {
        try {
            Profile profile = profileRepository.findByUserId(userId)
                    .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

            profile.updateProfile(request.getNickname(), request.getBio());
            Profile updatedProfile = profileRepository.save(profile);

            String presignedUrl = generatePresignedUrl(updatedProfile.getProfileImageKey());
            return APIResponse.ok(ProfileResponse.from(updatedProfile, presignedUrl));
        } catch (ProfileException e) {
            throw e;
        } catch (Exception e) {
            throw new ProfileException(ProfileErrorCode.PROFILE_UPDATE_FAILED);
        }
    }

    @Transactional
    public APIResponse<ProfileResponse> updateProfileImage(Long userId, MultipartFile newProfileImage) {
        try {
            Profile profile = profileRepository.findByUserId(userId)
                    .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

            String oldImageKey = profile.getProfileImageKey();

            if (!oldImageKey.equals(fileService.getDefaultProfileImageKey())) {
                fileService.deleteFileFromS3(oldImageKey);
            }

            String newImageKey = uploadImageToS3(newProfileImage);

            profile.updateProfileImage(newImageKey);
            Profile updatedProfile = profileRepository.save(profile);

            String presignedUrl = generatePresignedUrl(updatedProfile.getProfileImageKey());
            return APIResponse.ok(ProfileResponse.from(updatedProfile, presignedUrl));
        } catch (ProfileException e) {
            throw e;
        } catch (Exception e) {
            throw new ProfileException(ProfileErrorCode.PROFILE_UPDATE_FAILED);
        }
    }

    @Transactional
    public APIResponse<ProfileResponse> deleteProfileImage(Long userId) {
        try {
            Profile profile = profileRepository.findByUserId(userId)
                    .orElseThrow(() -> new ProfileException(ProfileErrorCode.PROFILE_NOT_FOUND));

            String oldImageKey = profile.getProfileImageKey();

            if (!oldImageKey.equals(fileService.getDefaultProfileImageKey())) {
                fileService.deleteFileFromS3(oldImageKey);
            }

            profile.updateProfileImage(fileService.getDefaultProfileImageKey());
            Profile updatedProfile = profileRepository.save(profile);

            String presignedUrl = fileService.getDefaultProfileImageUrl();
            return APIResponse.ok(ProfileResponse.from(updatedProfile, presignedUrl));
        } catch (ProfileException e) {
            throw e;
        } catch (Exception e) {
            throw new ProfileException(ProfileErrorCode.PROFILE_DELETE_FAILED);
        }
    }

    private String uploadImageToS3(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String extension = filename != null && filename.contains(".")
                    ? filename.substring(filename.lastIndexOf(".") + 1).toLowerCase()
                    : "webp";

            String s3Key = "profile/" + UUID.randomUUID() + "." + extension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + extension);
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, s3Key, file.getInputStream(), metadata);

            return s3Key;
        } catch (Exception e) {
            throw new ProfileException(ProfileErrorCode.PROFILE_CREATE_FAILED, "이미지 업로드 실패");
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