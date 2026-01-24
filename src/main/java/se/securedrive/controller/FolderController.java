package se.securedrive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.securedrive.model.Folder;
import se.securedrive.model.User;
import se.securedrive.service.FolderService;

@RestController
@RequestMapping("/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public Folder createFolder(
            @RequestParam String name,
            @RequestAttribute("user") User user
    ) {
        return folderService.createFolder(name, user);
    }
}
