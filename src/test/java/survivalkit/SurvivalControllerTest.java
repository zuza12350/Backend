package survivalkit;
import com.google.gson.JsonArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.edu.pjwstk.project.survivalkit.SurvivalController;
import pl.edu.pjwstk.project.survivalkit.SurvivalKitRequest;
import pl.edu.pjwstk.project.survivalkit.SurvivalService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SurvivalControllerTest {

    @InjectMocks
    private SurvivalController controller;

    @Mock
    private SurvivalService service;

    @Test
    public void getSurvivalData_ShouldReturnOkStatusAndCorrectBody() {
        // given
        JsonArray expectedBody = new JsonArray();
        when(service.getSurvivalData()).thenReturn(expectedBody);

        // when
        ResponseEntity<JsonArray> response = controller.getSurvivalData();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void addSurvivalTip_ShouldInvokeServiceMethod() {
        // given
        SurvivalKitRequest request = new SurvivalKitRequest();

        // when
        controller.addSurvivalTip(request);

        // then
        verify(service).addSurvivalTip(request);
    }

    @Test
    public void removeSurvivalTip_ShouldInvokeServiceMethod() {
        // given
        String name = "name";

        // when
        controller.removeSurvivalTip(name);

        // then
        verify(service).removeSurvivalTip(name);
    }

}
