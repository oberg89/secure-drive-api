package se.securedrive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.securedrive.dto.FileSummary;
import se.securedrive.model.FileEntity;
import se.securedrive.model.User;
import se.securedrive.service.FileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.nio.charset.StandardCharsets;
import org.springframework.http.ContentDisposition;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public FileSummary uploadFile(
            @RequestParam MultipartFile file,
            @RequestParam Long folderId,
            @AuthenticationPrincipal User user
    ) {
        FileEntity saved = fileService.uploadFile(file, folderId, user);
        return new FileSummary(saved.getId(), saved.getFilename(), saved.getFolder().getId());
    }

    @GetMapping
    public List<FileSummary> listFiles(@AuthenticationPrincipal User user) {
        return fileService.listFiles(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        FileEntity file = fileService.downloadFile(id, user);

        String filename = file.getFilename();
        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(file.getData());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        fileService.deleteFile(id, user);
    }
}
