package B1ND.linkUp.domain.file.controller;

import B1ND.linkUp.domain.file.dto.request.S3Request;
import B1ND.linkUp.domain.file.service.FileService;
import B1ND.linkUp.global.common.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/profile")
    public APIResponse<?> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        return fileService.uploadProfileImage(file);
    }

    @GetMapping
    public APIResponse<String> generatePresignedUrl(@Valid @RequestBody S3Request request) {
        return fileService.generatePresignedUrl(request.s3key());
    }
}