package se.securedrive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.securedrive.model.Folder;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByOwnerId(Long ownerId);
}
