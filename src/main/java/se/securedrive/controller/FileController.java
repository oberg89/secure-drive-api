package se.securedrive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.securedrive.dto.FileSummary;
import se.securedrive.model.FileEntity;
import se.securedrive.model.User;
import se.securedrive.service.FileService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller för att hantera filer.
 * Implementerar HATEOAS för att tillhandahålla länkar till nedladdning, radering och listning.
 */
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
        FileSummary summary = new FileSummary(saved.getId(), saved.getFilename(), saved.getFolder().getId());
        return addLinks(summary, user);
    }

    @GetMapping
    public CollectionModel<FileSummary> listFiles(@AuthenticationPrincipal User user) {
        List<FileSummary> summaries = fileService.listFiles(user).stream()
                .map(s -> addLinks(s, user))
                .toList();
        return CollectionModel.of(summaries,
                linkTo(methodOn(FileController.class).listFiles(user)).withSelfRel());
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
    public ResponseEntity<Void> deleteFile(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        fileService.deleteFile(id, user);
        return ResponseEntity.noContent().build();
    }

    /**
     * Hjälpmetod för att lägga till HATEOAS-länkar till en FileSummary.
     */
    private FileSummary addLinks(FileSummary summary, User user) {
        summary.add(linkTo(methodOn(FileController.class).downloadFile(summary.getId(), user)).withRel("download"));
        summary.add(linkTo(methodOn(FileController.class).deleteFile(summary.getId(), user)).withRel("delete"));
        summary.add(linkTo(methodOn(FileController.class).listFiles(user)).withRel("all-files"));
        return summary;
    }
}
