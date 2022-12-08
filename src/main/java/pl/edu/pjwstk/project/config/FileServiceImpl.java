package pl.edu.pjwstk.project;


import com.google.gson.JsonObject;
import org.springframework.web.multipart.MultipartFile;

public interface FileServiceImpl {
    String saveFile(String filePath);
    String saveFile(String fileName,MultipartFile file);
    byte[] loadFile(String fileName);
    void overrideFile(String fileName, JsonObject data);
    void changePointerToNewFile(MultipartFile file, String fileHash);
}
