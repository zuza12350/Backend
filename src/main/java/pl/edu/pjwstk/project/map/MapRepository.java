package pl.edu.pjwstk.project.map;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public interface MapRepository {
    JsonObject getLocations(double latitude, double longitude) throws IOException;
}
