package pl.edu.pjwstk.project.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.project.config.IPFSService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapService implements MapRepository{

    private final IPFSService ipfsService;

    private JsonObject jsonLocations;

    public void setLocationPointsFromFile() {
        byte[] bytes = ipfsService.loadFile("mapLocations");
        this.jsonLocations = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    private boolean locationPointExists(double latitude, double longitude) {
        JsonArray locationPoints = jsonLocations.getAsJsonArray("location_points");
        for (JsonElement location : locationPoints) {
            if (location.getAsJsonObject().get("latitude").getAsDouble() == latitude && location.getAsJsonObject().get("longitude").getAsDouble() == longitude) {
                return true;
            }
        }
        return false;
    }



    @Override
    public JsonObject getLocations(double latitude, double longitude) throws IOException {
        setLocationPointsFromFile();

        List<Location> locations = LocationFinder.findLocationsWithinRange(jsonLocations, latitude,longitude);

        return parseLocations(locations);
    }

    public static JsonObject parseLocations(List<Location> locations) {
        JsonArray jsonArray = new JsonArray();
        for (Location location : locations) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", location.getName());
            jsonObject.addProperty("latitude", location.getLatitude());
            jsonObject.addProperty("longitude", location.getLongitude());
            jsonArray.add(jsonObject);
        }

        JsonObject result = new JsonObject();
        result.add("location_points", jsonArray);
        return result;
    }

    @Override
    public void addLocationPoint(String name, double latitude, double longitude) {
        if (locationPointExists(latitude, longitude)) return;

        setLocationPointsFromFile();

        JsonObject newLocation = new JsonObject();
        newLocation.addProperty("name", name);
        newLocation.addProperty("latitude", latitude);
        newLocation.addProperty("longitude", longitude);

        jsonLocations.getAsJsonArray("location_points").add(newLocation);

        ipfsService.overrideFile("mapLocations.json", jsonLocations);
    }

    @Override
    public void removeLocationPoint(double latitude, double longitude) {
        setLocationPointsFromFile();
        JsonArray locationPoints = jsonLocations.getAsJsonArray("location_points");
        for (int i = 0; i < locationPoints.size(); i++) {
            JsonObject location = locationPoints.get(i).getAsJsonObject();
            if (location.get("latitude").getAsDouble() == latitude && location.get("longitude").getAsDouble() == longitude) {
                locationPoints.remove(i);
                break;
            }
        }

        ipfsService.overrideFile("mapLocations.json", jsonLocations);
    }
}
