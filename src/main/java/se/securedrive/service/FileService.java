package se.securedrive.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se.securedrive.dto.FileSummary;
import se.securedrive.model.FileEntity;
import se.securedrive.model.Folder;
import se.securedrive.model.User;
import se.securedrive.repository.FileRepository;
import se.securedrive.repository.FolderRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;

    /**
     * Uploads a file to a specific folder owned by the user.
     *
     * @param file     uploaded file
     * @param folderId target folder id
     * @param user     authenticated user
     * @return saved file entity
     */

    public FileEntity uploadFile(MultipartFile file, Long folderId, User user){
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder not found"));

        if (!folder.getOwner().getId().equals(user.getId())){
            throw new RuntimeException("Access denied");
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
            throw new RuntimeException("Failed to upload file");
        }
    }
    /**
     * Downloads a file owned by the user.
     *
     * @param fileId file id
     * @param user   authenticated user
     * @return file entity
     */
    public FileEntity downloadFile(Long fileId, User user) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!file.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return file;
    }

    /**
     * Deletes a file owned by the user.
     *
     * @param fileId file id
     * @param user   authenticated user
     */
    public void deleteFile(Long fileId, User user) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!file.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        fileRepository.delete(file);
    }

    /**
     * Lists files owned by the user (without file data).
     *
     * @param user authenticated user
     * @return list of file summaries
     */
    public List<FileSummary> listFiles(User user) {
        return fileRepository.findSummariesByOwnerId(user.getId());
    }
}
