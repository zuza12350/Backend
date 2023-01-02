package pl.edu.pjwstk.project.gun;


import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.project.gun.requests.GunRequest;
import pl.edu.pjwstk.project.gun.requests.GunTypeRequest;

@RestController
@AllArgsConstructor
public class GunController {
    private final GunService service;

    @GetMapping("/getGunData/{kind}")
    public ResponseEntity<JsonArray> getGunsData(@PathVariable("kind") String kind) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getGunData(kind));
    }
    @PostMapping(value = {"/addGun/{categoryId}", "/addGun/{categoryId}/{subType}"})
    public void addGun(@PathVariable(value = "categoryId") Long categoryId,
                       @PathVariable(value = "subType",required = false) Long subType,
                       @RequestBody GunRequest gunRequest){
        service.addGun(categoryId,subType,gunRequest);
    }
    @PostMapping("/addGunType")
    public void addGunType(@RequestBody GunTypeRequest gunTypeRequest){
        service.addGunType(gunTypeRequest);
    }

    @DeleteMapping("/removeGun")
    public void removeGun(@RequestBody String gunName){
        service.removeGunByName(gunName);
    }
    @DeleteMapping("/removeGunType")
    public void removeGunType(@RequestBody String gunTypeName){
        service.removeGunType(gunTypeName);
    }
}
