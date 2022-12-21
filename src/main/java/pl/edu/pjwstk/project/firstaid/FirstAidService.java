package pl.edu.pjwstk.project.firstaid;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.project.config.IPFSService;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class FirstAidService implements FirstAidRepository {

    private final IPFSService ipfsService;
    private JsonObject jsonObject;

    @Override
    public void setFirstAidFromFile() {
        byte[] bytes = ipfsService.loadFile("firstAid.json");
        this.jsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    @Override
    public JsonObject getFirstAidContent() {
        return jsonObject;
    }

    @Override
    public boolean addFirstAidKitToFile(FirstAidKitRequest request) {
        JsonObject newFirstAidKit = new JsonObject();
        newFirstAidKit.add("name", new Gson().toJsonTree(request.getName()));
        newFirstAidKit.add("description", new Gson().toJsonTree(request.getDescription()));

        setFirstAidFromFile();

        this.jsonObject.get("kits").getAsJsonObject().get("types").getAsJsonArray().add(newFirstAidKit);

        ipfsService.overrideFile("firstAid", this.jsonObject);
        return true;
    }

    @Override
    public boolean addLifeSupportActionToFile(LifeSupportActionRequest request) {
        JsonObject newFirstAidKit = new JsonObject();
        newFirstAidKit.add("name", new Gson().toJsonTree(request.getName()));
        newFirstAidKit.add("description", new Gson().toJsonTree(request.getDescription()));
        newFirstAidKit.add("procedure", new Gson().toJsonTree(request.getProcedure()));

        setFirstAidFromFile();

        this.jsonObject.get("support_actions").getAsJsonObject().get("types").getAsJsonArray().add(newFirstAidKit);

        ipfsService.overrideFile("firstAid", this.jsonObject);

        return true;
    }

    @Override
    public boolean removeLifeSupportActionFromFile(String name) {
        setFirstAidFromFile();
        JsonElement element = null;
        for (JsonElement obj : this.jsonObject.get("support_actions").getAsJsonObject().get("types").getAsJsonArray()){
            if (obj.getAsJsonObject().get("name").getAsString().equals(name)) {
                element = obj;
            }
        }
        this.jsonObject.get("support_actions").getAsJsonObject().get("types").getAsJsonArray().remove(element);
        ipfsService.overrideFile("firstAid", this.jsonObject);

        return true;
    }

    @Override
    public boolean removeFirstAidKitFromFile(String name) {
        setFirstAidFromFile();
        JsonElement element = null;
        for (JsonElement obj : this.jsonObject.get("kits").getAsJsonObject().get("types").getAsJsonArray()){
            if (obj.getAsJsonObject().get("name").getAsString().equals(name)) {
                   element = obj;
            }
        }

        this.jsonObject.get("kits").getAsJsonObject().get("types").getAsJsonArray().remove(element);

        ipfsService.overrideFile("firstAid", this.jsonObject);

        return true;
    }


}
