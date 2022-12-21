package pl.edu.pjwstk.project.firstaid;

import com.google.gson.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pjwstk.project.config.IPFSService;

import java.nio.charset.StandardCharsets;

/**
 * Klasa odpowiedzialna za obsługę pliku json z informacjami o pierwszej pomocy.
 * Implementuje interfejs {@link FirstAidRepository}.
 * Używa biblioteki Gson do parsowania i tworzenia obiektów JSON.
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
     * Ładuje plik z informacjami o pierwszej pomocy do pamięci.
     */
    @Override
    public void loadFirstAidFromFile() {
        byte[] bytes = ipfsService.loadFile("firstAid");
        this.jsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    /**
     * Zwraca zawartość pliku z informacjami o pierwszej pomocy jako obiekt JSON.
     *
     * @return obiekt JSON zawierający informacje o pierwszej pomocy
     */
    @Override
    public JsonObject getFirstAidContent() {
        loadFirstAidFromFile();
        return jsonObject;
    }

    /**
     * Sprawdza, czy element o podanej nazwie istnieje w tablicy JSON.
     *
     * @param array tablica JSON
     * @param name nazwa szukanego elementu
     * @return true, jeśli element o podanej nazwie istnieje w tablicy, false w przeciwnym wypadku
     */
    private boolean elementExists(JsonArray array, String name){
        for (JsonElement element : array){
            if (element.getAsJsonObject().get("name").getAsString().equals(name))
                return true;
        }
        return false;
    }

    /**
     * Dodaje nowy zestaw pierwszej pomocy do pliku.
     * Operacja jest wykonywana w ramach transakcji bazodanowej.
     *
     * @param request obiekt zawierający dane nowego zestawu pierwszej pomocy
     * @return true, jeśli dodawanie się powiodło, false w przeciwnym wypadku
     */
    @Override
    @Transactional
    public boolean addFirstAidKitToFile(FirstAidKitRequest request) {
        loadFirstAidFromFile();
        JsonArray firstAidKits = jsonObject.get("kits").getAsJsonObject().get("types").getAsJsonArray();

        if (elementExists(firstAidKits, request.getName()))
            return false;

        JsonObject newFirstAidKit = new JsonObject();
        newFirstAidKit.add("name", JsonParser.parseString(request.getName()));
        newFirstAidKit.add("description", JsonParser.parseString(request.getDescription()));

        JsonArray itemsArray = new JsonArray();
        for (String item : request.getItems()) {
            itemsArray.add(JsonParser.parseString(item));
        }
        newFirstAidKit.add("items", itemsArray);

        firstAidKits.add(newFirstAidKit);
        ipfsService.overrideFile("firstAid", this.jsonObject);
        return true;
    }


    @Override
    @Transactional
    public boolean addLifeSupportActionToFile(LifeSupportActionRequest request) {
        loadFirstAidFromFile();
        JsonArray lifeSupportActions = jsonObject.get("support_actions").getAsJsonObject().get("types").getAsJsonArray();

        if (!elementExists(lifeSupportActions, request.getName()))
            return false;

        JsonObject newFirstAidSupportAction = new JsonObject();
        newFirstAidSupportAction.add("name", JsonParser.parseString(request.getName()));
        newFirstAidSupportAction.add("description", JsonParser.parseString(request.getDescription()));

        JsonArray procedureArray = new JsonArray();
        for (String procedure : request.getProcedure()) {
            procedureArray.add(JsonParser.parseString(procedure));
        }
        newFirstAidSupportAction.add("procedure", procedureArray);

        lifeSupportActions.add(newFirstAidSupportAction);
        ipfsService.overrideFile("firstAid", this.jsonObject);

        return true;
    }



    @Override
    @Transactional
    public boolean editFirstAidKitInFile(String name, FirstAidKitRequest request) {
        loadFirstAidFromFile();

        JsonArray firstAidKits = jsonObject.get("kits").getAsJsonObject().get("types").getAsJsonArray();
        for (int i = 0; i < firstAidKits.size(); i++) {
            JsonObject firstAidKit = firstAidKits.get(i).getAsJsonObject();
            if (firstAidKit.get("name").getAsString().equals(name)) {
                firstAidKit.add("name", JsonParser.parseString(request.getName()));
                firstAidKit.add("description", JsonParser.parseString(request.getDescription()));
                JsonArray itemsArray = new JsonArray();
                for (String item : request.getItems()) {
                    itemsArray.add(JsonParser.parseString(item));
                }
                firstAidKit.add("items", itemsArray);
                break;
            }
        }

        ipfsService.overrideFile("firstAid", this.jsonObject);
        return true;
    }



    @Override
    @Transactional
    public boolean editLifeSupportActionInFile(String name, LifeSupportActionRequest request) {
        loadFirstAidFromFile();

        JsonArray lifeSupportActions = jsonObject.get("support_actions").getAsJsonObject().get("types").getAsJsonArray();
        for (int i = 0; i < lifeSupportActions.size(); i++) {
            JsonObject lifeSupportAction = lifeSupportActions.get(i).getAsJsonObject();
            if (lifeSupportAction.get("name").getAsString().equals(name)) {
                lifeSupportAction.add("name", JsonParser.parseString(request.getName()));
                lifeSupportAction.add("description", JsonParser.parseString(request.getDescription()));

                JsonArray procedureArray = new JsonArray();
                for (String procedure : request.getProcedure()) {
                    procedureArray.add(JsonParser.parseString(procedure));
                }
                lifeSupportAction.add("procedure", procedureArray);
                break;
            }
        }

        ipfsService.overrideFile("firstAid", this.jsonObject);
        return true;

    }



    @Override
    @Transactional
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

    @Override
    @Transactional
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
