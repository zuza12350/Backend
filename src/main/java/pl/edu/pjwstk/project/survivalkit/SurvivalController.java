package pl.edu.pjwstk.project.survivalkit;

import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *The SurvivalController class is responsible for defining the endpoints for saving, removal, and receipt survival tips thanks to @RestController, @PostMapping, @GetMapping and @DeleteMapping annotations.
 *
 * @author Zuzanna Borkowska
 */
@RestController
@AllArgsConstructor
public class SurvivalController {
    private final SurvivalService service;

    @GetMapping("/getSurvivalData")
    public ResponseEntity<JsonArray> getSurvivalData() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getSurvivalData());
    }
    @PostMapping("/addSurvivalKit")
    public void addSurvivalTip(@RequestBody SurvivalKitRequest request){
        service.addSurvivalTip(request);
    }

    @DeleteMapping("/removeSurvivalTip")
    public void removeSurvivalTip(@RequestBody String name){
       service.removeSurvivalTip(name);
    }

}
