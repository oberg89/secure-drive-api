package se.securedrive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.securedrive.dto.FolderSummary;
import se.securedrive.model.Folder;
import se.securedrive.model.User;
import se.securedrive.repository.FolderRepository;

import java.util.List;

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

    /**
     * Lists folders owned by the user.
     *
     * @param owner authenticated user
     * @return list of folder summaries
     */
    public List<FolderSummary> listFolders(User owner) {
        return folderRepository.findByOwnerId(owner.getId())
                .stream()
                .map(folder -> new FolderSummary(folder.getId(), folder.getName()))
                .toList();
    }
}
