package pl.edu.pjwstk.project.filelogic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

/**
 * Interface includes @Repository annotation which indicate that class is at the persistence layer,
 * which will act as a database repository for data manipulation in FileHolder entity.
 *
 * The class, thanks to @Query annotations, is able to directly write, read, modify or search for a given piece of information in the file_hash table.
 * @author Zuzanna Borkowska
 */
@Repository
public interface FileHolderRepository extends JpaRepository<FileHolder,Long> {
    @Modifying
    @Query(value = "insert into file_holder(file_name,file_hash) values (:fileName,:fileHash)", nativeQuery = true)
    @Transactional
    void saveFile(@Param("fileName") String fileName, @Param("fileHash") String fileHash);

    @Query(value = "select file_hash from file_holder where file_name=:fileName",nativeQuery = true)
    String getFileHash(@Param("fileName") String fileName);

    @Query(value = "select case when count(file_name) > 0 then true else false end from file_holder where file_name=:fileName",nativeQuery = true)
    boolean existsByName(@Param("fileName") String fileName);

    @Modifying
    @Query(value = "update file_holder set file_hash=:fileHash where file_name=:fileName",nativeQuery = true)
    @Transactional
    void changeFilePointerToNewHash(@Param("fileName") String fileName, @Param("fileHash") String fileHash);

}
