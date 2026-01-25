package se.securedrive.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileSummary {
    private Long id;
    private String filename;
    private Long folderId;
}
