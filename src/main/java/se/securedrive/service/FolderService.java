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
     * Skapar en ny mapp som ägs av den angivna användaren.
     *
     * @param name  namn på mappen
     * @param owner autentiserad användare
     * @return skapad mapp
     */
    public Folder createFolder(String name, User owner) {
        Folder folder = Folder.builder()
                .name(name)
                .owner(owner)
                .build();

        return folderRepository.save(folder);
    }

    /**
     * Listar mappar som ägs av användaren.
     *
     * @param owner autentiserad användare
     * @return lista med mapp-sammanfattningar
     */
    public List<FolderSummary> listFolders(User owner) {
        return folderRepository.findByOwnerId(owner.getId())
                .stream()
                .map(folder -> new FolderSummary(folder.getId(), folder.getName()))
                .toList();
    }
}
