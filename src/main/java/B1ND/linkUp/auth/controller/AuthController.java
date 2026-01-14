package B1ND.linkUp.auth.controller;

import B1ND.linkUp.auth.dto.*;
import B1ND.linkUp.auth.service.AuthService;
import B1ND.linkUp.global.common.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<APIResponse<MessageResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        MessageResponse res = authService.signUp(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(APIResponse.of(HttpStatus.CREATED, res));
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<APIResponse<SignInResponse>> signIn(@Valid @RequestBody SignInRequest request) {
        SignInResponse res = authService.signIn(request);
        return ResponseEntity.ok(APIResponse.ok(res));
    }

    // 비밀번호 변경 & 인증번호 전송
    @PostMapping("/pwchange/send")
    public ResponseEntity<APIResponse<MessageResponse>> sendPwChangeCode(
            @Valid @RequestBody PwChangeSendRequest request
    ) {
        MessageResponse res = authService.sendPwChangeCode(request);
        return ResponseEntity.ok(APIResponse.ok(res));
    }

    // 비밀번호 변경 & 인증번호 검증
    @PostMapping("/pwchange/verify")
    public ResponseEntity<APIResponse<MessageResponse>> verifyPwChangeCode(
            @Valid @RequestBody PwChangeVerifyRequest request
    ) {
        MessageResponse res = authService.verifyPwChangeCode(request);
        return ResponseEntity.ok(APIResponse.ok(res));
    }

    // 비밀번호 변경완
    @PostMapping("/pwchange/change")
    public ResponseEntity<APIResponse<MessageResponse>> changePassword(
            @Valid @RequestBody PwChangeChangeRequest request
    ) {
        MessageResponse res = authService.changePassword(request);
        return ResponseEntity.ok(APIResponse.ok(res));
    }

    // 액세스 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<APIResponse<SignInResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        SignInResponse res = authService.refreshToken(request);
        return ResponseEntity.ok(APIResponse.ok(res));
    }

}
