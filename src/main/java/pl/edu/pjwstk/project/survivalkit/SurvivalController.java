package pl.edu.pjwstk.project.survivalkit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

    @CrossOrigin
    @GetMapping("/getSurvivalTips")
    public ResponseEntity<JsonArray> getSurvivalTips() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getSurvivalTips());
    }

    @CrossOrigin
    @GetMapping("/getSurvivalData")
    public ResponseEntity<JsonObject> getSurvivalData() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getSurvivalData());
    }



    @PostMapping("/addSurvivalKit")
    public boolean addSurvivalTip(@RequestBody SurvivalKitRequest request){
        return service.addSurvivalTip(request);
    }

    @DeleteMapping("/removeSurvivalTip")
    public boolean removeSurvivalTip(@RequestBody String name){
       return service.removeSurvivalTip(name);
    }

}
