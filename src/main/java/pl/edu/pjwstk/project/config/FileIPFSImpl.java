package pl.edu.pjwstk.project.config;


import com.google.gson.JsonObject;
import org.springframework.web.multipart.MultipartFile;

/**
 *  Interface containing method definitions for writing, loading data, or overwriting a file,
 *  as well as changing the file hash to a new one.
 *
 * @author Zuzanna Borkowska
 */
public interface FileIPFSImpl {
    String saveFile(String fileName,MultipartFile file);
    byte[] loadFile(String fileName);
    void overrideFile(String fileName, JsonObject data);
    void changePointerToNewFile(MultipartFile file, String fileHash);
}
