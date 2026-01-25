package se.securedrive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.securedrive.dto.CreateFolderRequest;
import se.securedrive.dto.FolderSummary;
import se.securedrive.model.Folder;
import se.securedrive.model.User;
import se.securedrive.service.FolderService;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public FolderSummary createFolder(
            @RequestBody CreateFolderRequest request,
            @RequestAttribute("user") User user
    ) {
        Folder folder = folderService.createFolder(request.getName(), user);
        return new FolderSummary(folder.getId(), folder.getName());
    }

    @GetMapping
    public List<FolderSummary> listFolders(@RequestAttribute("user") User user) {
        return folderService.listFolders(user);
    }
}
