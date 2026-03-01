package se.securedrive.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileSummary extends RepresentationModel<FileSummary> {
    private Long id;
    private String filename;
    private Long folderId;
}
