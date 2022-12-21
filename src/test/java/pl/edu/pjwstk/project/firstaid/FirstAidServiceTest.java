package pl.edu.pjwstk.project.firstaid;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.pjwstk.project.config.IPFSService;
import pl.edu.pjwstk.project.firstaid.FirstAidService;
import pl.edu.pjwstk.project.firstaid.LifeSupportActionRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FirstAidServiceTest {

    @Test
    public void setFirstAidFromFile_ValidInput_SetsJsonObject() throws IOException {
        //arrange
        IPFSService ipfsServiceMock = mock(IPFSService.class);
        FirstAidService firstAidService = new FirstAidService(ipfsServiceMock);

        byte[] bytes = "{\"kits\": {\"types\": [{\"name\": \"Basic\",\"description\": \"Basic first aid kit\"},{\"name\": \"Advanced\",\"description\": \"Advanced first aid kit\"}]}}".getBytes();
        when(ipfsServiceMock.loadFile("firstAid")).thenReturn(bytes);

        //act
        firstAidService.loadFirstAidFromFile();

        //assert
        JsonObject expectedJsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
        assertEquals(expectedJsonObject, firstAidService.getFirstAidContent());
    }

    @Test
    public void getFirstAidContent_ValidInput_ReturnsJsonObject() throws IOException {
        //arrange
        IPFSService ipfsServiceMock = mock(IPFSService.class);
        FirstAidService firstAidService = new FirstAidService(ipfsServiceMock);

        byte[] bytes = "{\"kits\": {\"types\": [{\"name\": \"Basic\",\"description\": \"Basic first aid kit\"},{\"name\": \"Advanced\",\"description\": \"Advanced first aid kit\"}]}}".getBytes();
        when(ipfsServiceMock.loadFile("firstAid")).thenReturn(bytes);

        firstAidService.loadFirstAidFromFile();

        //act
        JsonObject result = firstAidService.getFirstAidContent();

        //assert
        JsonObject expectedJsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
        assertEquals(expectedJsonObject, result);
    }

    @Test
    public void addLifeSupportActionToFile_ValidInput_AddsLifeSupportAction() throws IOException {
        //arrange
        IPFSService ipfsServiceMock = mock(IPFSService.class);
        FirstAidService firstAidService = new FirstAidService(ipfsServiceMock);

        byte[] bytes = "{\"support_actions\": {\"types\": [{\"name\": \"CPR\",\"description\": \"Cardiopulmonary resuscitation\",\"procedure\": \"Step 1...\"},{\"name\": \"Heimlich maneuver\",\"description\": \"Procedure to remove foreign object from the airway\",\"procedure\": \"Step 1...\"}]}}".getBytes();
        when(ipfsServiceMock.loadFile("firstAid")).thenReturn(bytes);

        firstAidService.loadFirstAidFromFile();

        LifeSupportActionRequest request = new LifeSupportActionRequest();
        request.setName("Test action");
        request.setDescription("Test description");
        request.setProcedure(Arrays.asList("Test", "procedure"));

        //act
        boolean result = firstAidService.addLifeSupportActionToFile(request);

        //assert
        assertTrue(result);
        verify(ipfsServiceMock, times(1)).overrideFile("firstAid", firstAidService.getFirstAidContent());
        JsonObject expectedJsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
        expectedJsonObject.get("support_actions").getAsJsonObject().get("types").getAsJsonArray().add(new JsonParser().parse(new Gson().toJson(request)).getAsJsonObject());
        assertEquals(expectedJsonObject, firstAidService.getFirstAidContent());

    }
}
