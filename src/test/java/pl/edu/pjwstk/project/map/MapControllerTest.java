package pl.edu.pjwstk.project.map;

import com.google.gson.JsonObject;
import org.hibernate.TypeMismatchException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapControllerTest {
    private MapService mockService;
    private MapController controller;

    @Before
    public void setUp() {
        // Tworzymy mock obiektu MapService
        mockService = Mockito.mock(MapService.class);
        controller = new MapController(mockService);
    }


    @Test
    public void testGetLocations() throws IOException {
        // Tworzymy przykładowy obiekt JsonObject
        JsonObject json = new JsonObject();
        json.addProperty("key", "value");

        // Ustawiamy, że mock metody getLocations() zwraca przykładowy obiekt JsonObject
        when(mockService.getLocations(52.229841, 21.011736)).thenReturn(json);

        // Wywołujemy metodę getLocations() z przykładowymi danymi wejściowymi
        ResponseEntity<JsonObject> response = controller.getLocations(52.229841, 21.011736);

        // Sprawdzamy, czy został zwrócony odpowiedni status HTTP
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Sprawdzamy, czy został zwrócony odpowiedni obiekt JsonObject
        JsonObject result = response.getBody();
        assertEquals("value", result.get("key").getAsString());
    }

    @Test
    public void testAddLocation() {
        // Wywołujemy metodę addLocation() z przykładowymi danymi wejściowymi
        ResponseEntity<String> response = controller.addLocation("Warsaw", 52.229841, 21.011736);

        // Sprawdzamy, czy został zwrócony odpowiedni status HTTP
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Sprawdzamy, czy została wywołana metoda addLocationPoint() z odpowiednimi parametrami
        verify(mockService).addLocationPoint("Warsaw", 52.229841, 21.011736);

        // Sprawdzamy, czy została zwrócona odpowiednia wiadomość
        assertEquals("Location added successfully", response.getBody());
    }

    @Test
    public void testRemoveLocation() {
        // Wywołujemy metodę removeLocation() z przykładowymi danymi wejściowymi
        ResponseEntity<String> response = controller.removeLocation(52.229841, 21.011736);

        // Sprawdzamy, czy został zwrócony odpowiedni status HTTP
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Sprawdzamy, czy została wywołana metoda removeLocationPoint() z odpowiednimi parametrami
        verify(mockService).removeLocationPoint(52.229841, 21.011736);

        // Sprawdzamy, czy została zwrócona odpowiednia wiadomość
        assertEquals("Location removed successfully", response.getBody());
    }

    @Test
    public void testHandleTypeMismatchException() {
        // Tworzymy przykładowy obiekt TypeMismatchException
        TypeMismatchException ex = new TypeMismatchException("test");

        // Wywołujemy metodę handleTypeMismatchException() z przykładowym obiektem TypeMismatchException
        ResponseEntity<String> response = controller.handleTypeMismatchException(ex);

        // Sprawdzamy, czy został zwrócony odpowiedni status HTTP
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Sprawdzamy, czy została zwrócona odpowiednia wiadomość
        assertEquals("Nieprawidłowy typ danych w ścieżce", response.getBody());
    }
}
