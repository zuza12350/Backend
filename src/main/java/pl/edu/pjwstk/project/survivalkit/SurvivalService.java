package pl.edu.pjwstk.project.survivalkit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.project.config.IPFSService;
import pl.edu.pjwstk.project.exceptions.ElementNotFoundException;
import pl.edu.pjwstk.project.securityconfig.UserService;

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
    private final UserService userService;
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
     * @param request object that represents request of survival gun
     */
    @Override
    public boolean addSurvivalTip(SurvivalKitRequest request){
        setSurvivalKitFromFile();
        request.setCreatedBy(userService.getUsernameOfCurrentLoggedUser());
        this.jsonObject.getAsJsonArray("tips").add(new Gson().toJsonTree(request));
        ipfsService.overrideFile("survivalKit",this.jsonObject);
        return true;
    }

    /**
     * Method responsible for removing survival tip by given in argument name.
     * @param name variable which represents name of survival tip
     */
    @Override
    public boolean removeSurvivalTip(String name){
        var survivalKit = getSurvivalTips();
        var found = false;
        for(int i=0; i< survivalKit.size();i++){
            if(survivalKit.get(i).getAsJsonObject().get("name").getAsString().equals(name)){
                survivalKit.remove(i);
                found=true;
                break;
            }
        }
        this.jsonObject.remove("tips");
        this.jsonObject.add("tips",survivalKit);
        ipfsService.overrideFile("survivalKit",this.jsonObject);
        return found;
    }

    /**
     * Method responsible for retrieving data about survival.
     * @return Array of survival tips
     * @throws ElementNotFoundException
     */
    @Override
    public JsonArray getSurvivalTips() throws ElementNotFoundException {
        setSurvivalKitFromFile();
        var data = this.jsonObject;
        return data.getAsJsonArray("tips");
    }

    public JsonObject getSurvivalData() {
        setSurvivalKitFromFile();
        return jsonObject;
    }
}
