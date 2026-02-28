package se.securedrive.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(unique = true)
    private String email;

    @Column(name = "github_id", unique = true)
    private String githubId;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Folder> folders;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<FileEntity> files;
}
