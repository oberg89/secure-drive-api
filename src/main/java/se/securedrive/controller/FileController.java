package se.securedrive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.securedrive.model.FileEntity;
import se.securedrive.model.User;
import se.securedrive.service.FileService;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public FileEntity uploadFile(
            @RequestParam MultipartFile file,
            @RequestParam Long folderId,
            @RequestAttribute("user") User user
    ) {
        return fileService.uploadFile(file, folderId, user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable Long id,
            @RequestAttribute("user") User user
    ) {
        FileEntity file = fileService.downloadFile(id, user);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file.getData());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(
            @PathVariable Long id,
            @RequestAttribute("user") User user
    ) {
        fileService.deleteFile(id, user);
    }
}
