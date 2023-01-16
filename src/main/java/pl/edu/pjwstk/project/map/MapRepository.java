package pl.edu.pjwstk.project.map;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Interface representing a repository for map data.
 *
 * @author Mikołaj Noga
 */
@Repository
public interface MapRepository {
    JsonObject getLocations(double latitude, double longitude) throws IOException;
    void addLocationPoint(String name, double latitude, double longitude);
    void removeLocationPoint(double latitude, double longitude);
}
