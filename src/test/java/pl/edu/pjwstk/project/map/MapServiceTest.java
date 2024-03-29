package pl.edu.pjwstk.project.map;

import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MapServiceTest {

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

}
