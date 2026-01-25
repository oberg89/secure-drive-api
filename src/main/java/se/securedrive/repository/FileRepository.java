package se.securedrive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.securedrive.model.FileEntity;
import se.securedrive.dto.FileSummary;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByOwnerId(Long ownerId);

    @Query("select new se.securedrive.dto.FileSummary(f.id, f.filename, f.folder.id) " +
            "from FileEntity f where f.owner.id = ?1")
    List<FileSummary> findSummariesByOwnerId(Long ownerId);
}
