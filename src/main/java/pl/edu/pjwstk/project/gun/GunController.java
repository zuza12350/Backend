package pl.edu.pjwstk.project.gun;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
public class GunController {
    private final GunService service;

    @GetMapping("getGunsCategory/{category}")
    public ResponseEntity<JsonObject> getGunCategory(@PathVariable("category") String category) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getGunCategory(category));
    }
    @GetMapping("getGunsFromCategory/{category}")
    public ResponseEntity<JsonArray> getAllGunsFormGivenCategory(@PathVariable("category") String category) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getGunsFromCategory(category));
    }

    @PostMapping(value = {"addGunToCategory/{category}","addGunToCategory/{category}/{subCategoryAcronym}"})
    public void addGunToCategory(@PathVariable(value = "category") String category,
                                 @PathVariable(value = "subCategoryAcronym",required = false) String subCategoryAcronym,
                                 @RequestBody GunRequest newGun){
        if(subCategoryAcronym!=null && !subCategoryAcronym.isEmpty()){
            service.addGunToCategory(category,subCategoryAcronym,newGun);
        }else{
            service.addGunToCategory(category,"",newGun);
        }
    }


    @PostMapping("addCategory/{categoryName}")
    public void createCategory(@PathVariable(value = "categoryName") String categoryName
            ,@RequestBody CategoryRequest categoryRequest) {
        service.createGunCategory(categoryName,categoryRequest);
    }

    @DeleteMapping(value = {"deleteGunFromCategory/{categoryName}","deleteGunFromCategory/{categoryName}/{subCategoryAcronym}"})
    public void deleteGunFromCategory(@PathVariable(value = "categoryName") String categoryName,
                                      @PathVariable(value = "subCategoryAcronym",required = false) String subCategoryAcronym,
                                      @RequestBody GunRequest gun){
        if(subCategoryAcronym!=null && !subCategoryAcronym.isEmpty()){
            service.deleteGunFromCategory(categoryName,subCategoryAcronym,gun.getName());
        }else{
            service.deleteGunFromCategory(categoryName,"", gun.getName());
        }

    }

    @DeleteMapping(value = {"removeCategory/{categoryName}","removeSubCategory/{categoryName}/{subCategoryAcronym}"})
    public void removeCategory(@PathVariable("categoryName") String categoryName,
                               @PathVariable(value = "subCategoryAcronym",required = false) String subCategoryAcronym){
        if(subCategoryAcronym!=null && !subCategoryAcronym.isEmpty()){
            service.removeSubCategory(subCategoryAcronym);
        }else{
            service.removeCategory(categoryName);
        }

    }
}
