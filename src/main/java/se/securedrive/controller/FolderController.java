package se.securedrive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.securedrive.dto.CreateFolderRequest;
import se.securedrive.dto.FolderSummary;
import se.securedrive.model.Folder;
import se.securedrive.model.User;
import se.securedrive.service.FolderService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller för att hantera mappar.
 * Implementerar HATEOAS för att tillhandahålla länkar till relaterade resurser.
 */
@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public FolderSummary createFolder(
            @RequestBody CreateFolderRequest request,
            @AuthenticationPrincipal User user
    ) {
        Folder folder = folderService.createFolder(request.getName(), user);
        FolderSummary summary = new FolderSummary(folder.getId(), folder.getName());
        return addLinks(summary, user);
    }

    @GetMapping
    public CollectionModel<FolderSummary> listFolders(@AuthenticationPrincipal User user) {
        List<FolderSummary> summaries = folderService.listFolders(user).stream()
                .map(s -> addLinks(s, user))
                .toList();

        return CollectionModel.of(summaries,
                linkTo(methodOn(FolderController.class).listFolders(user)).withSelfRel());
    }

    /**
     * Hjälpmetod för att lägga till HATEOAS-länkar till en FolderSummary.
     */
    private FolderSummary addLinks(FolderSummary summary, User user) {
        summary.add(linkTo(methodOn(FolderController.class).listFolders(user)).withRel("all-folders"));
        // Man skulle kunna lägga till länk till filer i mappen här om FileController har en sådan endpoint
        return summary;
    }
}
