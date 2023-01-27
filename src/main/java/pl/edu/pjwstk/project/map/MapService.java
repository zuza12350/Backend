package pl.edu.pjwstk.project.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.project.config.IPFSService;
import pl.edu.pjwstk.project.securityconfig.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * MapService is a class that implements the {@link MapRepository} interface. It is responsible for handling location points on a map.
 * The class uses an {@link IPFSService} to load and save location points from/to a file on IPFS.
 *
 * @author Mikołaj Noga
 * @see IPFSService
 * @see MapRepository
 */
@Service
@RequiredArgsConstructor
public class MapService implements MapRepository{

    private final IPFSService ipfsService;

    private final UserService userService;

    private JsonObject jsonLocations;


    /**
     * Method used to set the location points from a file on IPFS.
     */
    public void setLocationPointsFromFile() {
        byte[] bytes = ipfsService.loadFile("mapLocations");
        this.jsonLocations = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    /**
     * Method used to check if a location point with the given latitude and longitude already exists in the location points file.
     * @param latitude the latitude of the location point
     * @param longitude the longitude of the location point
     * @return true if the location point already exists, false otherwise
     */
    boolean locationPointExists(double latitude, double longitude) {
        JsonArray locationPoints = jsonLocations.getAsJsonArray("location_points");
        for (JsonElement location : locationPoints) {
            if (location.getAsJsonObject().get("latitude").getAsDouble() == latitude && location.getAsJsonObject().get("longitude").getAsDouble() == longitude) {
                return true;
            }
        }
        return false;
    }


    /**
     * Method used to get the locations within a range of the given latitude and longitude.
     *
     * @param latitude the latitude of the location point
     * @param longitude the longitude of the location point
     * @return JsonObject containing all location points within range
     * @throws IOException if the location points file cannot be loaded
     */
    @Override
    public JsonObject getLocations(double latitude, double longitude) throws IOException {
        setLocationPointsFromFile();

        List<Location> locations = LocationFinder.findLocationsWithinRange(jsonLocations, latitude,longitude);

        return parseLocations(locations);
    }

    /**
     * Method used to parse a list of Location objects into a JsonObject.
     * @param locations the list of Location objects
     * @return JsonObject containing all location points
     */
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

    /**
     * Method used to add location point to JSON file.
     * @param name name of location
     * @param latitude the latitude of the location point
     * @param longitude the longitude of the location point
     */
    @Override
    public void addLocationPoint(String name, double latitude, double longitude) {
        setLocationPointsFromFile();

        if (locationPointExists(latitude, longitude)) return;


        JsonObject newLocation = new JsonObject();
        newLocation.addProperty("name", name);
        newLocation.addProperty("latitude", latitude);
        newLocation.addProperty("longitude", longitude);
        newLocation.addProperty("createdBy", userService.getUsernameOfCurrentLoggedUser());

        jsonLocations.getAsJsonArray("location_points").add(newLocation);

        ipfsService.overrideFile("mapLocations", jsonLocations);
    }

    /**
     * Method used to remove location point from JSON file.
     * @param latitude the latitude of the location point
     * @param longitude the longitude of the location point
     */
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
