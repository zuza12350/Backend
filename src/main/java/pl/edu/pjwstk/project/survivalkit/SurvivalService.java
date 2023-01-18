package pl.edu.pjwstk.project.unitesting.survivalkit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.project.unitesting.config.IPFSService;
import pl.edu.pjwstk.project.exceptions.ElementNotFoundException;

import java.nio.charset.StandardCharsets;

/**
 * The SurvivalService class overrides the methods defined in SurvivalRepository while adding all the logic that tells you how to manipulate the data.
 * It includes the @Service annotation, which assigns classes in the service layer in Spring.
 *
 * @author Zuzanna Borkowska
 */
@Service
@RequiredArgsConstructor
public class SurvivalService implements SurvivalRepository{
    private final IPFSService ipfsService;
    private JsonObject jsonObject;

    /**
     * Method responsible for assigning to the jsonObject variable the data from the file located under the hash assigned to the survivalKit name in the database.
     */
    @Override
    public void setSurvivalKitFromFile() {
        byte[] bytes = ipfsService.loadFile("survivalKit");
        this.jsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    /**
     * The method is responsible for adding survival tip specified in the argument object.
     *
     * @param request bject that represents request of survival gun
     */
    @Override
    public void addSurvivalTip(SurvivalKitRequest request){
        setSurvivalKitFromFile();
        this.jsonObject.getAsJsonArray("tips").add(new Gson().toJsonTree(request));
        ipfsService.overrideFile("survivalKit",this.jsonObject);
    }

    /**
     * Method responsible for removing survival tip by given in argument name.
     * @param name variable which represents name of survival tip
     */
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

    /**
     * Method responsible for retrieving data about survival.
     * @return Array of survival tips
     * @throws ElementNotFoundException
     */
    @Override
    public JsonArray getSurvivalData() throws ElementNotFoundException {
        setSurvivalKitFromFile();
        var data = this.jsonObject;
        return data.getAsJsonArray("tips");
    }
}
