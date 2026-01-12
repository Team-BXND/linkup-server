package B1ND.linkUp.auth.service;

import B1ND.linkUp.auth.dto.*;
import B1ND.linkUp.auth.entity.User;
import B1ND.linkUp.auth.repository.UserRepository;
import B1ND.linkUp.global.config.VerificationCodeProperties;
import B1ND.linkUp.global.exception.CustomException;
import B1ND.linkUp.global.exception.ErrorCode;
import B1ND.linkUp.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final VerificationCodeProperties verificationCodeProperties;

    public MessageResponse signUp(SignUpRequest request) {
        validatePasswordFormat(request.getPassword());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_USED);
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return new MessageResponse("회원가입이 완료되었습니다.");
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        return new SignInResponse(accessToken, refreshToken);
    }

    public MessageResponse sendPwChangeCode(PwChangeSendRequest request) {
        if (!userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.INVALID_EMAIL);
        }

        emailService.sendPwChangeEmail(request.getEmail());
        return new MessageResponse("인증번호가 전송되었습니다.");
    }

    @Transactional(readOnly = true)
    public MessageResponse verifyPwChangeCode(PwChangeVerifyRequest request) {
        int digits = verificationCodeProperties.getDigits();
        String format = "%0" + digits + "d";
        String inputCode = String.format(format, request.getCode());

        emailService.verifyPwChangeCode(request.getEmail(), inputCode);
        return new MessageResponse("인증되었습니다.");
    }

    public MessageResponse changePassword(PwChangeChangeRequest request) {
        validatePasswordFormat(request.getPassword());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_EMAIL));

        user.changePassword(passwordEncoder.encode(request.getPassword()));

        return new MessageResponse("비밀번호가 변경되었습니다.");
    }

    private void validatePasswordFormat(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*[!@#$%^&*()_+\\-\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";

        if (password == null || !password.matches(regex)) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }
}
