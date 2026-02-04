package B1ND.linkUp.domain.profile.controller;

import B1ND.linkUp.domain.profile.dto.ProfileRequest;
import B1ND.linkUp.domain.profile.dto.ProfileResponse;
import B1ND.linkUp.domain.profile.service.ProfileService;
import B1ND.linkUp.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    // 프로필 생성
    @PostMapping
    public APIResponse<ProfileResponse> createProfile(
            @RequestParam("userId") Long userId,
            @RequestPart("profile") ProfileRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        return profileService.createProfile(userId, request, profileImage);
    }

    // 프로필 조회
    @GetMapping("/{userId}")
    public APIResponse<ProfileResponse> getProfile(@PathVariable Long userId) {
        return profileService.getProfile(userId);
    }

    //프로필 정보수정
    @PutMapping("/{userId}")
    public APIResponse<ProfileResponse> updateProfile(
            @PathVariable Long userId,
            @RequestBody ProfileRequest request) {
        return profileService.updateProfile(userId, request);
    }

    // 프로필 이미지 수정
    @PutMapping("/{userId}/image")
    public APIResponse<ProfileResponse> updateProfileImage(
            @PathVariable Long userId,
            @RequestParam("profileImage") MultipartFile profileImage) {
        return profileService.updateProfileImage(userId, profileImage);
    }

    //이미지 삭제
    @DeleteMapping("/{userId}/image")
    public APIResponse<ProfileResponse> deleteProfileImage(@PathVariable Long userId) {
        return profileService.deleteProfileImage(userId);
    }
}