package pl.edu.pjwstk.project.gun;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.project.gun.requests.GunRequest;
import pl.edu.pjwstk.project.gun.requests.GunTypeRequest;
/**
 *The GunController class is responsible for defining the endpoint for saving weapons or their type and their removal and receipt thanks to
 *  RestController, PostMapping, GetMapping and DeleteMapping annotations.
 *
 * @author Zuzanna Borkowska
 */
@RestController
@AllArgsConstructor
public class GunController {
    private final GunService service;

    @CrossOrigin
    @GetMapping("/getGunData/{kind}")
    public ResponseEntity<JsonArray> getGunsData(@PathVariable("kind") String kind) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getGunData(kind));
    }

    @CrossOrigin
    @GetMapping("/getWholeGunData")
    public ResponseEntity<JsonObject> getWholeGunsData() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getWholeGunData());
    }
    @PostMapping(value = {"/addGun/{categoryId}", "/addGun/{categoryId}/{subType}"})
    public boolean addGun(@PathVariable(value = "categoryId") Long categoryId,
                       @PathVariable(value = "subType",required = false) Long subType,
                       @RequestBody GunRequest gunRequest){
        return service.addGun(categoryId,subType,gunRequest);
    }
    @PostMapping("/addGunType")
    public boolean addGunType(@RequestBody GunTypeRequest gunTypeRequest){
        return service.addGunType(gunTypeRequest);
    }

    @DeleteMapping("/removeGun")
    public boolean removeGun(@RequestBody String gunName){
        return service.removeGunByName(gunName);
    }
    @DeleteMapping("/removeGunType")
    public boolean removeGunType(@RequestBody String gunTypeName){
        return service.removeGunType(gunTypeName);
    }
}
