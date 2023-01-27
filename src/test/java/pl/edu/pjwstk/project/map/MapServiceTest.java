package pl.edu.pjwstk.project.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.edu.pjwstk.project.config.IPFSService;
import pl.edu.pjwstk.project.securityconfig.UserService;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class MapServiceTest {

    @Mock
    private IPFSService ipfsService;

    @Test
    public void parseLocations_shouldReturnJsonObjectWithLocationPointsArray() {
        // given
        List<Location> locations = Arrays.asList(
                new Location("Location 1", 51.10, 17.03),
                new Location("Location 2", 51.11, 17.04)
        );

        // when
        JsonObject result = MapService.parseLocations(locations);

        // then
        assertThat(result).isInstanceOf(JsonObject.class);
        assertThat(result.has("location_points")).isTrue();
        assertThat(result.get("location_points").isJsonArray()).isTrue();
        assertThat(result.getAsJsonArray("location_points").size()).isEqualTo(2);
        assertThat(result.getAsJsonArray("location_points")
                .get(0).getAsJsonObject().get("name").getAsString()).isEqualTo("Location 1");
        assertThat(result.getAsJsonArray("location_points")
                .get(0).getAsJsonObject().get("latitude").getAsDouble()).isEqualTo(51.10);
        assertThat(result.getAsJsonArray("location_points")
                .get(0).getAsJsonObject().get("longitude").getAsDouble()).isEqualTo(17.03);
        assertThat(result.getAsJsonArray("location_points")
                .get(1).getAsJsonObject().get("name").getAsString()).isEqualTo("Location 2");
        assertThat(result.getAsJsonArray("location_points")
                .get(1).getAsJsonObject().get("latitude").getAsDouble()).isEqualTo(51.11);
        assertThat(result.getAsJsonArray("location_points")
                .get(1).getAsJsonObject().get("longitude").getAsDouble()).isEqualTo(17.04);
    }

    @Test
    public void parseLocations_shouldReturnEmptyJsonObjectForEmptyList() {
        // given
        List<Location> locations = Collections.emptyList();

        // when
        JsonObject result = MapService.parseLocations(locations);

        // then
        assertThat(result).isInstanceOf(JsonObject.class);
        assertThat(result.has("location_points")).isTrue();
        assertThat(result.get("location_points").isJsonArray()).isTrue();
        assertThat(result.getAsJsonArray("location_points").size()).isEqualTo(0);
    }

//    @Test
//    public void addLocationPoint_shouldReturnIfLocationPointAlreadyExists() throws Exception {
//        // given
//        UserService userService = mock(UserService.class);
//        MapService mapService = new MapService(ipfsService, userService);
//        JsonObject jsonLocations = new JsonObject();
//        jsonLocations.add("location_points", new JsonArray());
//        JsonObject locationPoint = new JsonObject();
//        locationPoint.addProperty("latitude", 51.10);
//        locationPoint.addProperty("longitude", 17.03);
//        jsonLocations.getAsJsonArray("location_points").add(locationPoint);
//        Field field = mapService.getClass().getDeclaredField("jsonLocations");
//        field.setAccessible(true);
//        field.set(mapService, jsonLocations);
//
//
//
//        // when
//        mapService.addLocationPoint("Location 1", 51.10, 17.03);
//
//        // then
//        assertThat(jsonLocations.getAsJsonArray("location_points").size()).isEqualTo(1);
//    }



}
