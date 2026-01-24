package se.securedrive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.securedrive.model.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
