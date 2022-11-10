package pl.edu.pjwstk.project.filelogic;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "file_holder", schema = "public")
@Getter @Setter
public class FileHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String file_name;
    private String file_hash;
}
