package B1ND.linkUp.domain.profile.controller;

import B1ND.linkUp.domain.profile.dto.response.MyAnswerItemResponse;
import B1ND.linkUp.domain.profile.dto.response.MyQuestionItemResponse;
import B1ND.linkUp.domain.profile.dto.response.ProfileResponse;
import B1ND.linkUp.domain.profile.service.ProfileService;
import B1ND.linkUp.global.common.APIResponse;
import B1ND.linkUp.global.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public APIResponse<ProfileResponse> profile() {
        return profileService.getProfile();
    }

    @GetMapping("/myque")
    public PageResponse<MyQuestionItemResponse> myQuestions(
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        return profileService.getMyQuestions(page);
    }

    @GetMapping("/myans")
    public PageResponse<MyAnswerItemResponse> myAnswers(
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        return profileService.getMyAnswers(page);
    }
}