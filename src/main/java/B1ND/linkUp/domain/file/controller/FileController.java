package B1ND.linkUp.domain.file.controller;

import B1ND.linkUp.domain.file.service.FileService;
import B1ND.linkUp.global.common.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class FileController {
    private final FileService fileService;

    @PostMapping("/post")
    public APIResponse<?> uploadPostImage(@RequestParam("file") MultipartFile file) {
        return fileService.uploadPostImage(file);
    }
}
