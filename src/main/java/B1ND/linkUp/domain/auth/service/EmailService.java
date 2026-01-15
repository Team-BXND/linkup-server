package B1ND.linkUp.domain.auth.service;

import B1ND.linkUp.global.config.VerificationCodeProperties;
import B1ND.linkUp.global.exception.CustomException;
import B1ND.linkUp.global.exception.ErrorCode;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private final TemplateEngine templateEngine;
    private final VerificationCodeProperties verificationCodeProperties;

    @Value("${spring.mail.username}")
    private String serviceName;

    public void sendPwChangeEmail(String email) {
        int authNum = generateAuthNumber();

        String title = "비밀번호 변경을 위한 인증코드입니다.";

        Context context = new Context();
        String formattedCode = formatCode(authNum);
        context.setVariable("authNum", formattedCode);

        String content = templateEngine.process("EmailAuth", context);

        sendEmail(serviceName, email, title, content);

        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(email, formattedCode, 5, TimeUnit.MINUTES);
    }

    public void verifyPwChangeCode(String email, String code) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String saved = ops.get(email);

        if (saved == null) {
            throw new CustomException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        if (!Objects.equals(saved, code)) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        redisTemplate.delete(email);
    }

    private int generateAuthNumber() {
        int digits = verificationCodeProperties.getDigits();
        int min = (int) Math.pow(10, digits - 1);
        int max = (int) Math.pow(10, digits) - 1;

        Random random = new Random();
        return min + random.nextInt(max - min + 1);
    }

    private String formatCode(int code) {
        int digits = verificationCodeProperties.getDigits();
        String format = "%0" + digits + "d";
        return String.format(format, code);
    }

    private void sendEmail(String setFrom, String toMail, String title, String content) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);

            Path logoPath = Paths.get(System.getProperty("user.dir"), "assets", "logo.png");
            if (Files.exists(logoPath) && Files.isRegularFile(logoPath)) {
                FileSystemResource logo = new FileSystemResource(logoPath.toFile());
                String contentType = Files.probeContentType(logoPath);
                helper.addInline("serviceLogo", logo, contentType != null ? contentType : "image/png");
            } else {
                log.warn("Email logo not found: {}", logoPath.toAbsolutePath());
            }

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("sendEmail failed", e);
            throw new CustomException(ErrorCode.INVALID_EMAIL);
        }
    }
}
