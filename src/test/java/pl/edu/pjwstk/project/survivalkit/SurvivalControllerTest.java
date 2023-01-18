package pl.edu.pjwstk.project.unitesting.survivalkit;

import com.google.gson.JsonArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        JsonArray expectedBody = new JsonArray();
        when(service.getSurvivalData()).thenReturn(expectedBody);

        ResponseEntity<JsonArray> response = controller.getSurvivalData();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void addSurvivalTip_ShouldInvokeServiceMethod() {
        SurvivalKitRequest request = new SurvivalKitRequest();

        controller.addSurvivalTip(request);

        verify(service).addSurvivalTip(request);
    }

    @Test
    public void removeSurvivalTip_ShouldInvokeServiceMethod() {
        String name = "name";

        controller.removeSurvivalTip(name);

        verify(service).removeSurvivalTip(name);
    }

}
