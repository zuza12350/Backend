package pl.edu.pjwstk.project.firstAid;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class firstAidController {

    private final firstAidService service;

    @GetMapping("getFirstAidFile")
    public ResponseEntity<JsonObject> getFileContent(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getFirstAidContent());
    }
}
