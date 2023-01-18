package pl.edu.pjwstk.project.map;

import com.google.gson.JsonArray;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

public class LocationFinderTest {
    @Test
    public void testFindLocationsWithinRange() throws IOException {
        // Tworzymy przykładowy obiekt JsonObject z dwoma punktami lokalizacji
        JsonObject json = new JsonObject();
        JsonArray locationPoints = new JsonArray();
        JsonObject locationPoint1 = new JsonObject();
        locationPoint1.addProperty("name", "Location 1");
        locationPoint1.addProperty("latitude", 54.146702);
        locationPoint1.addProperty("longitude", 19.326771);
        JsonObject locationPoint2 = new JsonObject();
        locationPoint2.addProperty("name", "Location 2");
        locationPoint2.addProperty("latitude", 50.855224);
        locationPoint2.addProperty("longitude", 20.500507);
        locationPoints.add(locationPoint1);
        locationPoints.add(locationPoint2);
        json.add("location_points", locationPoints);

        // Wywołujemy metodę findLocationsWithinRange() dla obiektu JsonObject i współrzędnych użytkownika
        List<Location> locations = LocationFinder.findLocationsWithinRange(json, 54.335526, 18.612455);

        // Sprawdzamy, czy został zwrócony odpowiedni rozmiar listy
        assertEquals(1, locations.size());

        // Sprawdzamy, czy został zwrócony poprawny obiekt lokalizacji
        Location location = locations.get(0);
        assertEquals("Location 1", location.getName());
        assertEquals(54.146702, location.getLatitude(), 0.000001);
        assertEquals(19.326771, location.getLongitude(), 0.000001);
    }

    @Test
    public void testCalculateDistance() {
        // Wywołujemy metodę calculateDistance() dla dwóch punktów lokalizacji
        double distance = LocationFinder.calculateDistance(50.00, 50.00, 0.00, 0.00);

        // Sprawdzamy, czy zostało obliczone poprawne odległość
        assertEquals(7293.887106, distance, 0.000001);
    }
}

