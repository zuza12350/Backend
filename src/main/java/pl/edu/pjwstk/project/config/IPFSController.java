package pl.edu.pjwstk.project.config;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *The IPFSController class is responsible for defining the endpoint for writing the file to IPFS thanks to @RestController and @PostMapping annotations.
 * With the uploadFile() method, you can add a file by specifying its short name as @PathVariable and posting the file as @RequestBody.
 *
 * @author Zuzanna Borkowska
 */
@RestController
@AllArgsConstructor
public class IPFSController {
    private final IPFSService ipfsService;

    @PostMapping( "/upload/{fileName}")
    public String uploadFile(@PathVariable("fileName") String filename, @RequestParam("file") MultipartFile file) {
        return ipfsService.saveFile(filename,file);
    }

}
