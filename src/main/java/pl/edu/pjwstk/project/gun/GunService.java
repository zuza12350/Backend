package pl.edu.pjwstk.project.unitesting.gun;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.project.unitesting.config.IPFSService;
import pl.edu.pjwstk.project.exceptions.ElementNotFoundException;
import pl.edu.pjwstk.project.unitesting.gun.requests.GunRequest;
import pl.edu.pjwstk.project.unitesting.gun.requests.GunTypeRequest;

import java.nio.charset.StandardCharsets;
/**
 * The GunService class overrides the methods defined in GunRepository while adding all the logic that tells you how to manipulate the data.
 * It includes the @Service annotation, which assigns classes in the service layer in Spring.
 *
 * @author Zuzanna Borkowska
 */
@Service
@RequiredArgsConstructor
public class GunService implements GunRepository{
    private final IPFSService ipfsService;
    private JsonObject jsonObject;

    /**
     * Method responsible for assigning to the jsonObject variable the data from the file located under the hash assigned to the guns name in the database.
     */
    @Override
    public void setGunsFromFile() {
        byte[] bytes = ipfsService.loadFile("guns");
        this.jsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }


    /**
     * The method is responsible for returning the weapon of the type of weapon specified in the argument.
     * @param kind a variable indicating the type of desired weapon
     * @return array of weapons of a particular type
     * @throws ElementNotFoundException
     */
    @Override
    public JsonArray getGunData(String kind) throws ElementNotFoundException {
        setGunsFromFile();
        var data = this.jsonObject;
        return data.getAsJsonArray(kind);
    }

    /**
     * The method is responsible for adding weapons to the category or subcategory specified in the argument.
     * @param categoryId id weapon category
     * @param subType subtype of given weapon @Nullable means that it is not required.
     * @param gunRequest object that represents request of gun
     */
    @Override
    public void addGun(Long categoryId, @Nullable Long subType, GunRequest gunRequest) {
        setGunsFromFile();
        gunRequest.setType(categoryId);
        if(subType != null){
            gunRequest.setSubType(subType);
        }
        this.jsonObject.getAsJsonArray("guns").add(new Gson().toJsonTree(gunRequest));
        ipfsService.overrideFile("guns",this.jsonObject);
    }

    /**
     * The method is responsible for adding a type of weapon.
     * @param gunTypeRequest object that represents request of gun type
     */
    @Override
    public void addGunType(GunTypeRequest gunTypeRequest) {
        setGunsFromFile();
        var list = this.jsonObject.getAsJsonArray("guns_types");
        var lastIndex= Integer.parseInt(String.valueOf(list.get(list.size()-1).getAsJsonObject().get("id")));
        gunTypeRequest.setId((long) (lastIndex + 1));
        this.jsonObject.getAsJsonArray("guns_types").add(new Gson().toJsonTree(gunTypeRequest));
        ipfsService.overrideFile("guns",this.jsonObject);
    }

    /**
     * The method responsible for removing weapons by a given name.
     * @param gunName variable representing the name of the weapon
     */
    @Override
    public void removeGunByName(String gunName) {
        var guns = getGunData("guns");
        for(int i=0; i< guns.size();i++){
            if(guns.get(i).getAsJsonObject().get("name").getAsString().equals(gunName)){
                guns.remove(i);
                break;
            }
        }
        this.jsonObject.remove("guns");
        this.jsonObject.add("guns",guns);

        ipfsService.overrideFile("guns",this.jsonObject);
    }

    /**
     * The method responsible for removing weapon type by a given type name.
     * @param gunTypeName ariable representing the name of the weapon type
     */
    @Override
    public void removeGunType(String gunTypeName) {
        var guns_types = getGunData("guns_types");
        for(int i=0; i< guns_types.size();i++){
            if(guns_types.get(i).getAsJsonObject().get("name").getAsString().equals(gunTypeName)){
                guns_types.remove(i);
                break;
            }
        }
        this.jsonObject.remove("guns_types");
        this.jsonObject.add("guns_types",guns_types);

        ipfsService.overrideFile("guns",this.jsonObject);
    }
}
