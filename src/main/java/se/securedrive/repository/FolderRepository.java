package se.securedrive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.securedrive.model.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {
}
