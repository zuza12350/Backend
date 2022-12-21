package pl.edu.pjwstk.project.map;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class MapController {

    private final MapService service;

    @GetMapping("/getLocations/{latitude}/{longitude}")
    public ResponseEntity<JsonObject> getLocations(
            @PathVariable("latitude") double latitude, @PathVariable("longitude") double longitude) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(service.getLocations(latitude, longitude));
    }

    @PostMapping("/addLocation/{name}/{latitude}/{longitude}")
    public ResponseEntity<String> addLocation(@PathVariable String name,
                                              @PathVariable double latitude,
                                              @PathVariable double longitude) {
        service.addLocationPoint(name, latitude, longitude);
        return ResponseEntity.ok("Location added successfully");
    }

    @DeleteMapping("/removeLocation/{latitude}/{longitude}")
    public ResponseEntity<String> removeLocation(@PathVariable double latitude, @PathVariable double longitude) {
        service.removeLocationPoint(latitude, longitude);
        return ResponseEntity.ok("Location removed successfully");
    }


    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatchException(TypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nieprawidłowy typ danych w ścieżce");
    }


}
