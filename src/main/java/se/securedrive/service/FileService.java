package se.securedrive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import se.securedrive.dto.FileSummary;
import se.securedrive.model.FileEntity;
import se.securedrive.model.Folder;
import se.securedrive.model.User;
import se.securedrive.repository.FileRepository;
import se.securedrive.repository.FolderRepository;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    /**
     * Laddar upp en fil till en specifik mapp som ägs av användaren.
     *
     * @param file     den uppladdade filen
     * @param folderId målmappens id
     * @param user     den autentiserade användaren
     * @return sparad fil-entitet
     */
    public FileEntity uploadFile(MultipartFile file, Long folderId, User user){
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mappen hittades inte"));

        if (!folder.getOwner().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Åtkomst nekad");
        }
        try {
            FileEntity fileEntity = FileEntity.builder()
                    .filename(file.getOriginalFilename())
                    .data(file.getBytes())
                    .owner(user)
                    .folder(folder)
                    .build();

            return fileRepository.save(fileEntity);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Misslyckades med att ladda upp fil");
        }
    }
    /**
     * Laddar ner en fil som ägs av användaren.
     *
     * @param fileId filens id
     * @param user   den autentiserade användaren
     * @return fil-entitet
     */
    public FileEntity downloadFile(Long fileId, User user) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filen hittades inte"));

        if (!file.getOwner().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Åtkomst nekad");
        }

        return file;
    }

    /**
     * Tar bort en fil som ägs av användaren.
     *
     * @param fileId filens id
     * @param user   den autentiserade användaren
     */
    public void deleteFile(Long fileId, User user) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filen hittades inte"));

        if (!file.getOwner().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Åtkomst nekad");
        }

        fileRepository.delete(file);
    }

    /**
     * Listar filer som ägs av användaren (utan fildata).
     *
     * @param user den autentiserade användaren
     * @return lista med filsammanfattningar
     */
    public List<FileSummary> listFiles(User user) {
        return fileRepository.findSummariesByOwnerId(user.getId());
    }
}
