package pl.edu.pjwstk.project.firstaid;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class FirstAidController {

    private final FirstAidService service;

    @GetMapping("/getFirstAidFile")
    public ResponseEntity<JsonObject> getFileContent() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getFirstAidContent());
    }

    @PostMapping("/addFirstAidKitToFile")
    public HttpStatus addFirstAidKitToFile(@RequestBody FirstAidKitRequest request) {
        if (service.addFirstAidKitToFile(request)) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
    }

    @PostMapping("/addLifeSupportActionToFile")
    public HttpStatus addLifeSupportActionToFile(@RequestBody LifeSupportActionRequest request) {
        if (service.addLifeSupportActionToFile(request)) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
    }

    @PutMapping("/editFirstAidKit/{name}")
    public ResponseEntity<Object> editFirstAidKit(@PathVariable String name, @RequestBody FirstAidKitRequest request) {
        if (service.editFirstAidKitInFile(name, request)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/editLifeSupportAction/{name}")
    public ResponseEntity<Object> editLifeSupportAction(@PathVariable String name, @RequestBody LifeSupportActionRequest request) {
        if (service.editLifeSupportActionInFile(name, request)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/removeLifeSupportActionFromFile/{name}")
    public HttpStatus removeLifeSupportActionFromFile(@PathVariable(value = "name") String name) {
        if (service.removeLifeSupportActionFromFile(name)) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
    }

    @DeleteMapping("/removeFirstAidKitFromFile/{name}")
    public HttpStatus removeFirstAidKitFromFile(@PathVariable("name") String name) {
        if (service.removeFirstAidKitFromFile(name)) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
    }

}
