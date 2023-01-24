package pl.edu.pjwstk.project.firstaid;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.project.config.IPFSService;

import java.nio.charset.StandardCharsets;

/**
 * The class responsible for handling the json file with first aid information.
 * Implements the {@link FirstAidRepository} interface.
 *
 * @author Mikołaj Noga
 * @see FirstAidRepository
 * @see IPFSService
 */
@Service
@RequiredArgsConstructor
public class FirstAidService implements FirstAidRepository {

    private final IPFSService ipfsService;
    private JsonObject jsonObject;

    /**
     * Loads a file with first aid information into memory.
     */
    @Override
    public void loadFirstAidFromFile() {
        byte[] bytes = ipfsService.loadFile("firstAid");
        this.jsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    /**
     * Returns the contents of the first aid information file as a JSON object.
     *
     * @return JSON object containing first aid information.
     */
    @Override
    public JsonObject getFirstAidContent() {
        loadFirstAidFromFile();
        return jsonObject;
    }

    /**
     * Checks whether an element with the specified name exists in the JSON array.
     *
     * @param array JSON Array
     * @param name name of the search element
     * @return true if the element with the given name exists in the array, false otherwise
     */
    boolean elementExists(JsonArray array, String name){
        for (JsonElement element : array){
            if (element.getAsJsonObject().get("name").getAsString().equals(name))
                return true;
        }
        return false;
    }

    /**
     * Adds a new first aid kit to the file.
     *
     * @param request object containing data of the new first aid kit
     * @return true if the addition was successful, false otherwise
     */
    @Override
    public boolean addFirstAidKitToFile(FirstAidKitRequest request) {
        loadFirstAidFromFile();
        JsonArray firstAidKits = jsonObject.get("kits").getAsJsonObject().get("types").getAsJsonArray();

        if (elementExists(firstAidKits, request.getName()))
            return false;

        JsonObject newFirstAidKit = new JsonObject();
        newFirstAidKit.add("name", JsonParser.parseString(request.getName()));
        newFirstAidKit.add("description", JsonParser.parseString(request.getDescription()));

        JsonArray itemsArray = new JsonArray();
        for (String item : request.getElements()) {
            itemsArray.add(JsonParser.parseString(item));
        }
        newFirstAidKit.add("elements", itemsArray);

        firstAidKits.add(newFirstAidKit);
        ipfsService.overrideFile("firstAid", this.jsonObject);
        return true;
    }


    /**
     * Adds a new life support action to the file.
     *
     * @param request object containing data of the new life support action
     * @return true if the addition was successful, false otherwise
     */
    @Override
    public boolean addLifeSupportActionToFile(LifeSupportActionRequest request) {
        loadFirstAidFromFile();
        JsonArray lifeSupportActions = jsonObject.get("support_actions").getAsJsonObject().get("types").getAsJsonArray();

        if (elementExists(lifeSupportActions, request.getName()))
            return false;

        JsonObject newFirstAidSupportAction = new JsonObject();
        newFirstAidSupportAction.add("name", JsonParser.parseString(request.getName()));
        newFirstAidSupportAction.add("description", JsonParser.parseString(request.getDescription()));
        newFirstAidSupportAction.add("video", JsonParser.parseString(request.getVideo()));

        JsonArray procedureArray = new JsonArray();
        for (String procedure : request.getElements()) {
            procedureArray.add(JsonParser.parseString(procedure));
        }
        newFirstAidSupportAction.add("elements", procedureArray);


        lifeSupportActions.add(newFirstAidSupportAction);
        ipfsService.overrideFile("firstAid", this.jsonObject);

        return true;
    }


    /**
     * Edit first aid kit from the file.
     *
     * @param name name of edited object
     * @param request object containing data of the first aid kit
     * @return true if the modification was successful, false otherwise
     */
    @Override
    public boolean editFirstAidKitInFile(String name, FirstAidKitRequest request) {
        loadFirstAidFromFile();

        JsonArray firstAidKits = jsonObject.get("kits").getAsJsonObject().get("types").getAsJsonArray();
        if (!elementExists(firstAidKits,name))
            return false;

        for (int i = 0; i < firstAidKits.size(); i++) {
            JsonObject firstAidKit = firstAidKits.get(i).getAsJsonObject();
            if (firstAidKit.get("name").getAsString().equals(name)) {
                firstAidKit.add("name", JsonParser.parseString(request.getName()));
                firstAidKit.add("description", JsonParser.parseString(request.getDescription()));
                JsonArray itemsArray = new JsonArray();
                for (String item : request.getElements()) {
                    itemsArray.add(JsonParser.parseString(item));
                }
                firstAidKit.add("elements", itemsArray);
                break;
            }
        }

        ipfsService.overrideFile("firstAid", this.jsonObject);
        return true;
    }


    /**
     * Edit life support action from the file.
     *
     * @param name name of edited object
     * @param request object containing data of the life support action
     * @return true if the modification was successful, false otherwise
     */
    @Override
    public boolean editLifeSupportActionInFile(String name, LifeSupportActionRequest request) {
        loadFirstAidFromFile();

        JsonArray lifeSupportActions = jsonObject.get("support_actions").getAsJsonObject().get("types").getAsJsonArray();
        if (!elementExists(lifeSupportActions, name))
            return false;

        for (int i = 0; i < lifeSupportActions.size(); i++) {
            JsonObject lifeSupportAction = lifeSupportActions.get(i).getAsJsonObject();
            if (lifeSupportAction.get("name").getAsString().equals(name)) {
                lifeSupportAction.add("name", JsonParser.parseString(request.getName()));
                lifeSupportAction.add("description", JsonParser.parseString(request.getDescription()));
                lifeSupportAction.add("video", JsonParser.parseString(request.getVideo()));

                JsonArray procedureArray = new JsonArray();
                for (String procedure : request.getElements()) {
                    procedureArray.add(JsonParser.parseString(procedure));
                }
                lifeSupportAction.add("elements", procedureArray);
                break;
            }
        }

        ipfsService.overrideFile("firstAid", this.jsonObject);
        return true;

    }


    /**
     * Remove life support action from the file.
     *
     * @param name name of removed object
     * @return true if the modification was successful, false otherwise
     */
    @Override
    public boolean removeLifeSupportActionFromFile(String name) {
        loadFirstAidFromFile();
        JsonArray lifeSupportActions = jsonObject.get("support_actions").getAsJsonObject().get("types").getAsJsonArray();
        boolean found = false;
        for (int i = 0; i < lifeSupportActions.size(); i++) {
            JsonObject lifeSupportAction = lifeSupportActions.get(i).getAsJsonObject();
            if (lifeSupportAction.get("name").getAsString().equals(name)) {
                lifeSupportActions.remove(i);
                found = true;
                break;
            }
        }
        if (found) {
            ipfsService.overrideFile("firstAid", this.jsonObject);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove first aid kit from the file.
     *
     * @param name name of removed object
     * @return true if the modification was successful, false otherwise
     */
    @Override
    public boolean removeFirstAidKitFromFile(String name) {
        loadFirstAidFromFile();
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
