package B1ND.linkUp.domain.file.repository;

import B1ND.linkUp.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, String> {
    Optional<File> findByUrl(String url);
}