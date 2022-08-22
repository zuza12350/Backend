package pl.edu.pjwstk.project.config;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class IPFSController {
    private final IPFSService ipfsService;

    @GetMapping(value = "")
    public String saveText(@RequestParam("filepath") String filepath) {
        return ipfsService.saveFile(filepath);
    }

    @PostMapping( "/upload/{fileName}")
    public String uploadFile(@PathVariable("fileName") String filename, @RequestParam("file") MultipartFile file) {
        return ipfsService.saveFile(filename,file);
    }
    //Nie przyda sie chyba ale niech bedzie narazie
    @GetMapping( "/file/{fileName}")
    public ResponseEntity<byte[]> getFileContent(@PathVariable("fileName") String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", MediaType.ALL_VALUE);
        byte[] bytes = ipfsService.loadFile(filename);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(bytes);
    }
}
