package se.securedrive.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToOne(optional = false)
    @JoinColumn(name = "folder_id")
    private Folder folder;
}