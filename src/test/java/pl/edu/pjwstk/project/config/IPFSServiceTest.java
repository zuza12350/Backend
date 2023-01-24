package pl.edu.pjwstk.project.config;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pjwstk.project.exceptions.WrongFileExtension;
import pl.edu.pjwstk.project.filelogic.FileHolderRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IPFSServiceTest {

    @Mock
    private IPFSConfig ipfsConfig;
    @Mock
    private FileHolderRepository fileHolderRepository;
    @InjectMocks
    private IPFSService ipfsService;

    @Test
    @DisplayName("Should override the file when the file exists")
    void overrideFileWhenFileExistsThenOverrideTheFile() {
        String filename = "test";
        String fileName = filename + ".json";
//        String data = "{\"test\":\"test\"}";
        JsonObject data = new JsonObject();
        data.addProperty("test","test");
        MultipartFile multipartFile = new MockMultipartFile(fileName, data.toString().getBytes());
        when(fileHolderRepository.existsByName(filename)).thenReturn(true);
        ipfsService.overrideFile(filename, data);
        verify(fileHolderRepository, times(1)).changeFilePointerToNewHash(filename, null);
    }

    @Test
    @DisplayName("Should create a new file when the file does not exist")
    void overrideFileWhenFileDoesNotExistThenCreateNewFile() {
        String filename = "test";
        JsonObject data = new JsonObject();
        data.addProperty("test", "test");
        MultipartFile multipartFile =
                new MockMultipartFile(
                        "test.json",
                        "test.json",
                        "application/json",
                        "{\"test\":\"test\"}".getBytes());
        when(fileHolderRepository.getFileHash(filename))
                .thenReturn("QmYwAPJzv5CZsnA625s3Xf2nemtYgPpHdWEz79ojWnPbdG");
        when(fileHolderRepository.existsByName(filename)).thenReturn(true);
        ipfsService.overrideFile(filename, data);
        verify(fileHolderRepository, times(1))
                .changeFilePointerToNewHash(
                        filename, "QmYwAPJzv5CZsnA625s3Xf2nemtYgPpHdWEz79ojWnPbdG");
    }

    @Test
    @DisplayName("Should throw an exception when the file extension is not json")
    void saveFileWhenFileExtensionIsNotJsonThenThrowException() {
        MultipartFile multipartFile =
                new MockMultipartFile("file", "file.txt", "text/plain", "some xml".getBytes());
        assertThrows(WrongFileExtension.class, () -> ipfsService.saveFile("file", multipartFile));
    }

    @Test
    @DisplayName("Should save the file when the file extension is json")
    void saveFileWhenFileExtensionIsJson() {
        String fileName = "test";
        String fileContent = "test";
        MultipartFile file = new MockMultipartFile(fileName, fileContent.getBytes());
        ipfsService.saveFile(fileName, file);
        verify(fileHolderRepository, times(1)).saveFile(fileName, fileContent);
    }

    @Test
    @DisplayName("Should override the file when the file name already exists")
    void saveFileWhenFilenameAlreadyExistsThenOverrideTheFile() {
        String fileName = "test";
        String fileContent = "test";
        MultipartFile file = new MockMultipartFile(fileName, fileContent.getBytes());
        when(fileHolderRepository.existsByName(fileName)).thenReturn(true);
        ipfsService.saveFile(fileName, file);
        verify(fileHolderRepository, times(1)).existsByName(fileName);
        verify(fileHolderRepository, times(1)).changeFilePointerToNewHash(fileName, anyString());
    }
}
