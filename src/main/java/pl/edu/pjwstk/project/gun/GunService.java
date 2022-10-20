package pl.edu.pjwstk.project.gun;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.project.config.IPFSService;
import pl.edu.pjwstk.project.exceptions.ElementNotFoundException;

import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
public class GunService implements GunRepository{
    private final IPFSService ipfsService;
    private  JsonObject jsonObject;

    @Override
    public void setGunsFromFile() {
       byte[] bytes = ipfsService.loadFile("guns");
       this.jsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    @Override
    public JsonObject getGunCategory(String category) throws ElementNotFoundException{
        setGunsFromFile();
        var data = this.jsonObject;
        var gunList = data.getAsJsonArray("guns");

        for(int i=0;i<gunList.size();i++){
            if(gunList.get(i).getAsJsonObject().get(category) != null){
                return (JsonObject) gunList.get(i).getAsJsonObject().get(category);
            }
        }
        throw new ElementNotFoundException();
    }

    @Override
    public JsonArray getGunsFromCategory(String category) {
        var gunCategory = getGunCategory(category);
        return gunCategory.getAsJsonArray("kinds");
    }

    @Override
    public String getFullNameOfSubCategory(String subCategoryName){
        switch (subCategoryName) {
            case "ckm":
                subCategoryName = "Ciężkie karabiny maszynowe (ckm)";
                break;
            case "Ikm":
                subCategoryName = "Lekkie karabiny maszynowe (Ikm)";
                break;
            case "rkm":
                subCategoryName = "Ręczne karabiny maszynowe (rkm)";
                break;
            case "ukm":
                subCategoryName = "Uniwersalne karabiny maszynowe (ukm)";
                break;
            case "wkm":
                subCategoryName = "Wielkokalibrowe karabiny maszynowe (wkm)";
                break;
            default:
                throw new ElementNotFoundException();
        }
        return subCategoryName;
    }
    @Override
    public JsonObject getSubCategory(String category, String subCategory) throws ElementNotFoundException {
        var gunsSubcategoryList = getGunsFromCategory(category);
        var subCategoryName = getFullNameOfSubCategory(subCategory);

        for(int i = 0; i < gunsSubcategoryList.size(); ++i) {
            if(gunsSubcategoryList.get(i).getAsJsonObject().has(subCategoryName)){
                return gunsSubcategoryList.get(i).getAsJsonObject();
            }
        }
        throw new ElementNotFoundException();
    }

    @Override
    public void addGunToCategory(String categoryName, String subCategoryAcronym,GunRequest newGun)  {
        JsonObject newGunObj = new JsonObject();
        newGunObj.add("name", new Gson().toJsonTree(newGun.getName()));
        newGunObj.add("description", new Gson().toJsonTree(newGun.getDescription()));
        newGunObj.add("caliber", new Gson().toJsonTree(newGun.getCaliber()));
        newGunObj.add("barrelLength", new Gson().toJsonTree(newGun.getBarrelLength()));
        newGunObj.add("velocityOfBullets", new Gson().toJsonTree(newGun.getVelocityOfBullets()));
        newGunObj.add("magazine", new Gson().toJsonTree(newGun.getMagazine()));
        newGunObj.add("bullet", new Gson().toJsonTree(newGun.getBullet()));
        newGunObj.add("photo", new Gson().toJsonTree(newGun.getPhoto()));
        newGunObj.add("url", new Gson().toJsonTree(newGun.getUrl()));

        var contentOfCategory = getGunCategory(categoryName);
        JsonObject category = new JsonObject();
        category.add(categoryName,contentOfCategory);
        this.jsonObject.getAsJsonArray("guns").remove(category);

        if(!categoryName.equals("MachineGuns")){
            contentOfCategory.getAsJsonArray("kinds").add(newGunObj);
            this.jsonObject.getAsJsonArray("guns").add(category);
        }else{
            var subCategory = getSubCategory(categoryName,subCategoryAcronym);
            contentOfCategory.getAsJsonArray("kinds").remove(subCategory);
            subCategory.get(getFullNameOfSubCategory(subCategoryAcronym))
                    .getAsJsonObject().getAsJsonArray("kinds")
                    .add(newGunObj);
            contentOfCategory.getAsJsonArray("kinds").add(subCategory);
        }
        ipfsService.overrideFile("guns",this.jsonObject);
    }

    @Override
    public void createGunCategory(String categoryName, CategoryRequest categoryRequest) {
        setGunsFromFile();
        JsonObject newCategory = new JsonObject();
        JsonObject categoryBody = new JsonObject();
        categoryBody.add("description", new Gson().toJsonTree(categoryRequest.getDescription()));
        categoryBody.add("kinds", new JsonArray());
        newCategory.add(categoryName,categoryBody);
        this.jsonObject.getAsJsonArray("guns").add(newCategory);
        ipfsService.overrideFile("guns",this.jsonObject);
    }

    @Override
    public void deleteGunFromCategory(String categoryName,String subCategoryAcronym,String gunName){
        var contentOfCategory = getGunCategory(categoryName);
        JsonObject category = new JsonObject();
        category.add(categoryName,contentOfCategory);
        this.jsonObject.getAsJsonArray("guns").remove(category);

        if(!categoryName.equals("MachineGuns")){
            var guns = contentOfCategory.getAsJsonArray("kinds");
            for(int i=0;i<guns.size();i++){
                if(guns.get(i).getAsJsonObject().get("name").getAsString().equals(gunName)){
                    guns.remove(i);
                    break;
                }
            }
            contentOfCategory.remove("kinds");
            contentOfCategory.add("kinds",guns);
            category.remove(categoryName);
            category.add(categoryName,contentOfCategory);
            this.jsonObject.getAsJsonArray("guns").add(category);
        } else {
            var subCategory = getSubCategory(categoryName,subCategoryAcronym);
            contentOfCategory.getAsJsonArray("kinds").remove(subCategory);
            var gunsFromSubCategory = subCategory.get(getFullNameOfSubCategory(subCategoryAcronym))
                    .getAsJsonObject().getAsJsonArray("kinds");
            for(int i=0;i<gunsFromSubCategory.size();i++){
                if(gunsFromSubCategory.get(i).getAsJsonObject().get("name").getAsString().equals(gunName)) {
                    gunsFromSubCategory.remove(i);
                    break;
                }
            }
            contentOfCategory.getAsJsonArray("kinds").add(subCategory);
        }

        ipfsService.overrideFile("guns",this.jsonObject);
    }

    @Override
    public void removeCategory(String categoryName) {
        var contentOfCategory = getGunCategory(categoryName);
        JsonObject category = new JsonObject();
        category.add(categoryName,contentOfCategory);
        this.jsonObject.getAsJsonArray("guns").remove(category);
        ipfsService.overrideFile("guns",this.jsonObject);
    }
    @Override
    public void removeSubCategory(String subCategoryAcronym){
        var contentOfCategory = getGunCategory("MachineGuns");
        var subCategory = getSubCategory("MachineGuns",subCategoryAcronym);
        JsonObject category = new JsonObject();
        category.add("MachineGuns",contentOfCategory);
        this.jsonObject.getAsJsonArray("guns").remove(category);
        contentOfCategory.getAsJsonArray("kinds").remove(subCategory);
        category.remove("MachineGuns");
        category.add("MachineGuns",contentOfCategory);

        this.jsonObject.getAsJsonArray("guns").add(category);
        ipfsService.overrideFile("guns",this.jsonObject);
    }
}
