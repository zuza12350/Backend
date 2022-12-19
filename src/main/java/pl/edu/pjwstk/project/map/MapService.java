package pl.edu.pjwstk.project.map;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static class Province {
        public String name;
        public List<List<Double>> border_points;
    }

    private static class Provinces {
        public List<Province> provinces;
    }

    private final IPFSService ipfsService;

    private JsonObject jsonProvinces;

    public void setFirstAidFromFile() {
        byte[] bytes = ipfsService.loadFile("provinces");
        this.jsonProvinces = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    @Override
    public JsonObject getLocations(double latitude, double longitude) throws IOException {
        setFirstAidFromFile();
        String province = getProvince(latitude, longitude, String.valueOf(jsonProvinces));

        if (province == null) {
            return new JsonObject();
        }

        byte[] locationsJson = ipfsService.loadFile(province);

        return JsonParser.parseString(new String(locationsJson, StandardCharsets.UTF_8)).getAsJsonObject();
    }


    private static String getProvince(double latitude, double longitude, String provincesJson) throws IOException {
        Provinces provinces = new ObjectMapper().readValue(provincesJson, Provinces.class);
        double[] point = new double[]{latitude, longitude};

        for (Province province : provinces.provinces) {
            List<List<Double>> borderPoints = province.border_points;
            if (isPointInPolygon(point, borderPoints)) {
                return province.name;
            }
        }

        return null;
    }

    /**
     * Sprawdza, czy podany punkt geograficzny znajduje się wewnątrz podanego wielokąta.
     *
     * @param point Punkt geograficzny do sprawdzenia, podany jako tablica z dwoma elementami - współrzędnymi szerokości geograficznej i długości geograficznej.
     * @param polygon Wielokąt, którego należy sprawdzić, czy zawiera punkt geograficzny. Podany jako lista punktów geograficznych,
     *                podanych jako listy z dwoma elementami - współrzędnymi szerokości geograficznej i długości geograficznej.
     * @return  `true`, jeśli punkt geograficzny znajduje się wewnątrz wielokąta, w przeciwnym razie `false`.
     */
    private static boolean isPointInPolygon(double[] point, List<List<Double>> polygon) {
        double x = point[0];
        double y = point[1];
        int n = polygon.size();
        boolean inside = false;

        double[] p1 = polygon.get(0).stream().mapToDouble(Double::doubleValue).toArray();
        for (int i = 0; i < n + 1; i++) {
            double[] p2 = polygon.get(i % n).stream().mapToDouble(Double::doubleValue).toArray();
            double p1x = p1[0];
            double p1y = p1[1];
            double p2x = p2[0];
            double p2y = p2[1];
            if (y > Math.min(p1y, p2y)) {
                if (y <= Math.max(p1y, p2y)) {
                    if (x <= Math.max(p1x, p2x)) {
                        if (p1y != p2y) {
                            double xints = (y - p1y) * (p2x - p1x) / (p2y - p1y) + p1x;
                            if (p1x == p2x || x <= xints) {
                                inside = !inside;
                            }
                        }
                    }
                }
            }
            p1 = p2;
        }

        return inside;
    }
}
