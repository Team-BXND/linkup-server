package B1ND.linkUp.domain.file.controller;

import B1ND.linkUp.domain.file.dto.request.S3Request;
import B1ND.linkUp.domain.file.service.FileService;
import B1ND.linkUp.global.common.APIResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class FileController {
    private final FileService fileService;

    @PostMapping
    public APIResponse<?> uploadPostImage(@RequestParam("file") MultipartFile file) {
        return fileService.uploadPostImage(file);
    }

    @GetMapping
    public APIResponse<String> generatePresignedUrl(@Valid @RequestBody S3Request request) {
        return fileService.generatePresignedUrl(request.s3key());
    }
}
