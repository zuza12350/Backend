package pl.edu.pjwstk.project.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class LocationFinder {

    private static final double EARTH_RADIUS = 6371; // radius of Earth in kilometers
    private static final double MAX_DISTANCE = 70; // maximum distance in kilometers

    public static List<Location> findLocationsWithinRange(JsonObject json, double userLatitude, double userLongitude) throws IOException {
        List<Location> locations = new ArrayList<>();

        JsonArray locationPoints = json.getAsJsonArray("location_points");
        for (int i = 0; i < locationPoints.size(); i++) {
            JsonObject locationPoint = locationPoints.get(i).getAsJsonObject();
            double latitude = locationPoint.get("latitude").getAsDouble();
            double longitude = locationPoint.get("longitude").getAsDouble();

            double distance = calculateDistance(userLatitude, userLongitude, latitude, longitude);
            if (distance <= MAX_DISTANCE) {
                String name = locationPoint.get("name").getAsString();
                locations.add(new Location(name, latitude, longitude));
            }
        }

        return locations;
    }

    private static double calculateDistance(double userLatitude, double userLongitude, double latitude, double longitude) {
        double latitudeDifference = Math.toRadians(userLatitude - latitude);
        double longitudeDifference = Math.toRadians(userLongitude - longitude);
        double a = Math.sin(latitudeDifference / 2) * Math.sin(latitudeDifference / 2) +
                Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(latitude)) *
                        Math.sin(longitudeDifference / 2) * Math.sin(longitudeDifference / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
