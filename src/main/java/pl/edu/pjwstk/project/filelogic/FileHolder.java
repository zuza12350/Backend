package pl.edu.pjwstk.project.filelogic;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A class that is a representation of a database entity.
 * It represents an entity named file_holder, which stores annotations to files with given information and the hash assigned to them so as to simplify finding files in IPFS.
 *
 * @author Zuzanna Borkowska
 */
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
