package pl.edu.pjwstk.project.gun;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.istack.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.project.config.IPFSService;
import pl.edu.pjwstk.project.exceptions.ElementNotFoundException;
import pl.edu.pjwstk.project.gun.requests.GunRequest;
import pl.edu.pjwstk.project.gun.requests.GunTypeRequest;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class GunService implements GunRepository{
    private final IPFSService ipfsService;
    private JsonObject jsonObject;

    @Override
    public void setGunsFromFile() {
        byte[] bytes = ipfsService.loadFile("guns.json");
        this.jsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }
    @Override
    public JsonArray getGunData(String kind) throws ElementNotFoundException {
        setGunsFromFile();
        var data = this.jsonObject;
        return data.getAsJsonArray(kind);
    }
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
    @Override
    public void addGunType(GunTypeRequest gunTypeRequest) {
        setGunsFromFile();
        var list = this.jsonObject.getAsJsonArray("guns_types");
        var lastIndex= Integer.parseInt(String.valueOf(list.get(list.size()-1).getAsJsonObject().get("id")));
        gunTypeRequest.setId((long) (lastIndex + 1));
        this.jsonObject.getAsJsonArray("guns_types").add(new Gson().toJsonTree(gunTypeRequest));
        ipfsService.overrideFile("guns",this.jsonObject);
    }
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
