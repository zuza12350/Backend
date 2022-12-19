package firstaid;

import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.edu.pjwstk.project.firstaid.FirstAidController;
import pl.edu.pjwstk.project.firstaid.FirstAidKitRequest;
import pl.edu.pjwstk.project.firstaid.FirstAidService;
import pl.edu.pjwstk.project.firstaid.LifeSupportActionRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirstAidControllerTest {

    @InjectMocks
    private FirstAidController controller;

    @Mock
    private FirstAidService service;

    @Test
    public void getFileContent_ShouldReturnOkStatusAndCorrectBody() {
        // given
        JsonObject expectedBody = new JsonObject();
        when(service.getFirstAidContent()).thenReturn(expectedBody);

        // when
        ResponseEntity<JsonObject> response = controller.getFileContent();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void addFirstAidKitToFile_ShouldReturnOkStatus_WhenServiceReturnsTrue() {
        // given
        FirstAidKitRequest request = new FirstAidKitRequest();
        when(service.addFirstAidKitToFile(request)).thenReturn(true);

        // when
        HttpStatus status = controller.addFirstAidKitToFile(request);

        // then
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    public void addFirstAidKitToFile_ShouldReturnBadRequestStatus_WhenServiceReturnsFalse() {
        // given
        FirstAidKitRequest request = new FirstAidKitRequest();
        when(service.addFirstAidKitToFile(request)).thenReturn(false);

        // when
        HttpStatus status = controller.addFirstAidKitToFile(request);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    @Test
    public void addLifeSupportActionToFile_ShouldReturnOkStatus_WhenServiceReturnsTrue() {
        // given
        LifeSupportActionRequest request = new LifeSupportActionRequest();
        when(service.addLifeSupportActionToFile(request)).thenReturn(true);

        // when
        HttpStatus status = controller.addLifeSupportActionToFile(request);

        // then
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    public void addLifeSupportActionToFile_ShouldReturnBadRequestStatus_WhenServiceReturnsFalse() {
        // given
        LifeSupportActionRequest request = new LifeSupportActionRequest();
        when(service.addLifeSupportActionToFile(request)).thenReturn(false);

        // when
        HttpStatus status = controller.addLifeSupportActionToFile(request);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    @Test
    public void removeLifeSupportActionFromFile_ShouldReturnOkStatus_WhenServiceReturnsTrue() {
        // given
        String name = "name";
        when(service.removeLifeSupportActionFromFile(name)).thenReturn(true);

        // when
        HttpStatus status = controller.removeLifeSupportActionFromFile(name);

        // then
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    public void removeLifeSupportActionFromFile_ShouldReturnBadRequestStatus_WhenServiceReturnsFalse() {
        // given
        String name = "name";
        when(service.removeLifeSupportActionFromFile(name)).thenReturn(false);

        // when
        HttpStatus status = controller.removeLifeSupportActionFromFile(name);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    @Test
    public void removeFirstAidKitFromFile_ShouldReturnOkStatus_WhenServiceReturnsTrue() {
        // given
        String name = "name";
        when(service.removeFirstAidKitFromFile(name)).thenReturn(true);

        // when
        HttpStatus status = controller.removeFirstAidKitFromFile(name);

        // then
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    public void removeFirstAidKitFromFile_ShouldReturnBadRequestStatus_WhenServiceReturnsFalse() {
        // given
        String name = "name";
        when(service.removeFirstAidKitFromFile(name)).thenReturn(false);

        // when
        HttpStatus status = controller.removeFirstAidKitFromFile(name);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }
}
