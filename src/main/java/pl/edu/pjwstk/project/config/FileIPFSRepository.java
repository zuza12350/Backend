package pl.edu.pjwstk.project.config;


import com.google.gson.JsonObject;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public interface FileIPFSRepository {
    String saveFile(String filePath);
    String saveFile(String fileName,MultipartFile file);
    byte[] loadFile(String fileName);
    void overrideFile(String fileName, JsonObject data);
    void changePointerToNewFile(MultipartFile file, String fileHash);
}
