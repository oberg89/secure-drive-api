package se.securedrive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.securedrive.model.Folder;
import se.securedrive.model.User;
import se.securedrive.repository.FolderRepository;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    /**
     * Creates a new folder owned by the given user.
     *
     * @param name  name of the folder
     * @param owner authenticated user
     * @return created folder
     */
    public Folder createFolder(String name, User owner) {
        Folder folder = Folder.builder()
                .name(name)
                .owner(owner)
                .build();

        return folderRepository.save(folder);
    }
}
