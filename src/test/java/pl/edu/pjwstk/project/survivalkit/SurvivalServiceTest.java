package pl.edu.pjwstk.project.survivalkit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.pjwstk.project.config.IPFSService;
import pl.edu.pjwstk.project.exceptions.ElementNotFoundException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class SurvivalServiceTest {
    @Test
    public void addSurvivalDataTest() throws ElementNotFoundException {
        IPFSService ipfsService = mock(IPFSService.class);
        SurvivalService service = new SurvivalService(ipfsService);
        SurvivalKitRequest survivalKitRequest = new SurvivalKitRequest();
        survivalKitRequest.setName("testName");
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("tips",new JsonArray());
        when(ipfsService.loadFile("survivalKit")).thenReturn(jsonObject.toString().getBytes());

        boolean addSurvivalTip = service.addSurvivalTip(survivalKitRequest);

        assertTrue(addSurvivalTip);

    }
    @Test
    public void removeSurvivalTipByNameIfFoundThenReturnTrue() throws ElementNotFoundException {
        IPFSService ipfsService = mock(IPFSService.class);
        SurvivalService service = new SurvivalService(ipfsService);
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("name","nameS");
        jsonArray.add(jsonObject1);
        jsonObject.add("tips",jsonArray);
        when(ipfsService.loadFile("survivalKit")).thenReturn(jsonObject.toString().getBytes());

        boolean addSurvivalTip = service.removeSurvivalTip("nameS");

        assertTrue(addSurvivalTip);
    }
    @Test
    public void removeSurvivalTipByNameIfNotFoundThenReturnFalse() throws ElementNotFoundException {
        IPFSService ipfsService = mock(IPFSService.class);
        SurvivalService service = new SurvivalService(ipfsService);
        JsonObject jsonObject = new JsonObject();


        jsonObject.add("tips",new JsonArray());
        when(ipfsService.loadFile("survivalKit")).thenReturn(jsonObject.toString().getBytes());

        boolean removeSurvivalTip = service.removeSurvivalTip("nameS");

        assertFalse(removeSurvivalTip);
    }

}

