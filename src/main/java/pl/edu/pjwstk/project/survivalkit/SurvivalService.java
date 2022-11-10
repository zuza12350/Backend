package pl.edu.pjwstk.project.survivalkit;

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
public class SurvivalService implements SurvivalRepository{
    private final IPFSService ipfsService;
    private JsonObject jsonObject;

    @Override
    public void setSurvivalKitFromFile() {
        byte[] bytes = ipfsService.loadFile("survivalKit");
        this.jsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    @Override
    public void addSurvivalTip(SurvivalKitRequest request){
        setSurvivalKitFromFile();
        this.jsonObject.getAsJsonArray("tips").add(new Gson().toJsonTree(request));
        ipfsService.overrideFile("survivalKit",this.jsonObject);
    }
    @Override
    public void removeSurvivalTip(String name){
        var survivalKit = getSurvivalData();
        for(int i=0; i< survivalKit.size();i++){
            if(survivalKit.get(i).getAsJsonObject().get("name").getAsString().equals(name)){
                survivalKit.remove(i);
                break;
            }
        }
        this.jsonObject.remove("tips");
        this.jsonObject.add("tips",survivalKit);
        ipfsService.overrideFile("survivalKit",this.jsonObject);
    }
    @Override
    public JsonArray getSurvivalData() throws ElementNotFoundException {
        setSurvivalKitFromFile();
        var data = this.jsonObject;
        return data.getAsJsonArray("tips");
    }
}
