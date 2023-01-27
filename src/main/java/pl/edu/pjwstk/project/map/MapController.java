package pl.edu.pjwstk.project.map;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Class representing a MapController.
 *
 * @author Mikołaj Noga
 */
@RestController
@AllArgsConstructor
public class MapController {

    private final MapService service;

    /**
     * Retrieves a list of locations within a certain distance from the given latitude and longitude.
     *
     * @param latitude the latitude of the point from which to measure distance
     * @param longitude the longitude of the point from which to measure distance
     * @return a JSON object containing the list of locations
     * @throws IOException if an error occurs while reading the location data
     */

    @CrossOrigin
    @GetMapping("/getLocations/{latitude}/{longitude}")
    public ResponseEntity<JsonObject> getLocations(
            @PathVariable("latitude") double latitude, @PathVariable("longitude") double longitude) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(service.getLocations(latitude, longitude));
    }

    @CrossOrigin
    @GetMapping("/isUserInPoland/{latitude}/{longitude}")
    public boolean isUserInPoland(
            @PathVariable("latitude") double latitude, @PathVariable("longitude") double longitude) {
        return service.isUserInPoland(latitude,longitude);
    }

    /**
     * Add a new location with the given name, latitude, and longitude.
     *
     * @param name the name of the location
     * @param latitude the latitude of the location
     * @param longitude the longitude of the location
     * @return a string indicating that the location was added successfully
     */
    @PostMapping("/addLocation/{name}/{latitude}/{longitude}")
    public ResponseEntity<String> addLocation(@PathVariable String name,
                                              @PathVariable double latitude,
                                              @PathVariable double longitude) {
        service.addLocationPoint(name, latitude, longitude);
        return ResponseEntity.ok("Location added successfully");
    }

    /**
     * Remove the location with the given latitude and longitude.
     *
     * @param latitude the latitude of the location to remove
     * @param longitude the longitude of the location to remove
     * @return a string indicating that the location was removed successfully
     */
    @DeleteMapping("/removeLocation/{latitude}/{longitude}")
    public ResponseEntity<String> removeLocation(@PathVariable double latitude, @PathVariable double longitude) {
        service.removeLocationPoint(latitude, longitude);
        return ResponseEntity.ok("Location removed successfully");
    }


    /**
     * Handle a TypeMismatchException.
     *
     * @param ex the TypeMismatchException to handle
     * @return a string indicating that the exception occurred
     */
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatchException(TypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nieprawidłowy typ danych w ścieżce");
    }
}
