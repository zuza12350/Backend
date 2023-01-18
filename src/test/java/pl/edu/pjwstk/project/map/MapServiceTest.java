package pl.edu.pjwstk.project.unitesting.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.edu.pjwstk.project.unitesting.config.IPFSService;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class MapServiceTest {

    @Mock
    private IPFSService ipfsService;

    @InjectMocks
    private MapService mapService;

    @Test
    public void setLocationPointsFromFile_shouldLoadFileFromIPFSService() throws IOException {
        // given
        byte[] fileBytes = "{\"location_points\": []}".getBytes(StandardCharsets.UTF_8);
        when(ipfsService.loadFile("mapLocations.json")).thenReturn(fileBytes);

        // when
        mapService.setLocationPointsFromFile();

        // then
        verify(ipfsService).loadFile("mapLocations.json");
    }

    @Test
    public void getLocations_shouldLoadLocationPointsFromFile() throws IOException {
        // given
        byte[] fileBytes = "{\"location_points\": []}".getBytes(StandardCharsets.UTF_8);
        when(ipfsService.loadFile("mapLocations.json")).thenReturn(fileBytes);

        // when
        mapService.getLocations(51.10, 17.03);

        // then
        verify(ipfsService).loadFile("mapLocations.json");
    }

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

    @Test
    public void addLocationPoint_shouldReturnIfLocationPointAlreadyExists() throws Exception {
        // given
        MapService mapService = new MapService(ipfsService);
        JsonObject jsonLocations = new JsonObject();
        jsonLocations.add("location_points", new JsonArray());
        JsonObject locationPoint = new JsonObject();
        locationPoint.addProperty("latitude", 51.10);
        locationPoint.addProperty("longitude", 17.03);
        jsonLocations.getAsJsonArray("location_points").add(locationPoint);
        Field field = mapService.getClass().getDeclaredField("jsonLocations");
        field.setAccessible(true);
        field.set(mapService, jsonLocations);

        // when
        mapService.addLocationPoint("Location 1", 51.10, 17.03);

        // then
        assertThat(jsonLocations.getAsJsonArray("location_points").size()).isEqualTo(1);
    }


    @Test
    public void addLocationPoint_shouldLoadLocationPointsFromFile() {
        // given
        byte[] fileBytes = "{\"location_points\": []}".getBytes(StandardCharsets.UTF_8);
        IPFSService ipfsService = mock(IPFSService.class);
        when(ipfsService.loadFile("mapLocations")).thenReturn(fileBytes);
        MapService mapService = new MapService(ipfsService);

        // when
        mapService.addLocationPoint("Location 1", 51.10, 17.03);

        // then
        verify(ipfsService).loadFile("mapLocations");
    }

    @Test
    public void addLocationPoint_shouldAddNewLocationPoint() throws NoSuchFieldException, IllegalAccessException {
        // given
        byte[] fileBytes = "{\"location_points\": []}".getBytes(StandardCharsets.UTF_8);
        IPFSService ipfsService = mock(IPFSService.class);
        when(ipfsService.loadFile("mapLocations.json")).thenReturn(fileBytes);
        MapService mapService = new MapService(ipfsService);

        // when
        mapService.addLocationPoint("Location 1", 51.10, 17.03);

        // then
        Field field = mapService.getClass().getDeclaredField("jsonLocations");
        field.setAccessible(true);
        JsonObject jsonLocations = (JsonObject) field.get(mapService);

        assertThat(jsonLocations.getAsJsonArray("location_points").size()).isEqualTo(1);
        JsonObject locationPoint = jsonLocations.getAsJsonArray("location_points").get(0).getAsJsonObject();
        assertThat(locationPoint.get("name").getAsString()).isEqualTo("Location 1");
        assertThat(locationPoint.get("latitude").getAsDouble()).isEqualTo(51.10);
        assertThat(locationPoint.get("longitude").getAsDouble()).isEqualTo(17.03);
    }

    @Test
    public void addLocationPoint_shouldOverrideFile() throws NoSuchFieldException, IllegalAccessException {
        // given
        byte[] fileBytes = "{\"location_points\": []}".getBytes(StandardCharsets.UTF_8);
        IPFSService ipfsService = mock(IPFSService.class);
        when(ipfsService.loadFile("mapLocations.json")).thenReturn(fileBytes);
        MapService mapService = new MapService(ipfsService);

        // when
        mapService.addLocationPoint("Location 1", 51.10, 17.03);

        // then

        Field field = mapService.getClass().getDeclaredField("jsonLocations");
        field.setAccessible(true);
        JsonObject jsonLocations = (JsonObject) field.get(mapService);

        verify(ipfsService).overrideFile("mapLocations.json", jsonLocations);
    }
}
