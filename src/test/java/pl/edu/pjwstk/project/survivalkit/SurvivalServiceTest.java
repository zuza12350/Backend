package pl.edu.pjwstk.project.survivalkit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.pjwstk.project.config.IPFSService;
import pl.edu.pjwstk.project.exceptions.ElementNotFoundException;
import pl.edu.pjwstk.project.securityconfig.UserService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class SurvivalServiceTest {

    @Mock
    IPFSService ipfsService;
    @Mock
    UserService userService;

    @InjectMocks
    SurvivalService service;

    @Test
    public void addSurvivalDataTest() throws ElementNotFoundException {
        SurvivalKitRequest survivalKitRequest = new SurvivalKitRequest();
        survivalKitRequest.setName("testName");
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("tips",new JsonArray());
        when(ipfsService.loadFile("survivalKit")).thenReturn(jsonObject.toString().getBytes());
        when(userService.getUsernameOfCurrentLoggedUser()).thenReturn("test");

        boolean addSurvivalTip = service.addSurvivalTip(survivalKitRequest);

        assertTrue(addSurvivalTip);

    }
    @Test
    public void removeSurvivalTipByNameIfFoundThenReturnTrue() throws ElementNotFoundException {
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
        JsonObject jsonObject = new JsonObject();


        jsonObject.add("tips",new JsonArray());
        when(ipfsService.loadFile("survivalKit")).thenReturn(jsonObject.toString().getBytes());

        boolean removeSurvivalTip = service.removeSurvivalTip("nameS");

        assertFalse(removeSurvivalTip);
    }

}

