package pl.edu.pjwstk.project.unitesting.firstaid;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.pjwstk.project.unitesting.config.IPFSService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FirstAidServiceTest {

    @Test
    public void loadFirstAidFromFile_ValidInput_SetsJsonObject() throws IOException {
        //arrange
        IPFSService ipfsServiceMock = mock(IPFSService.class);
        FirstAidService firstAidService = new FirstAidService(ipfsServiceMock);

        byte[] bytes = "{\"kits\": {\"types\": [{\"name\": \"Basic\",\"description\": \"Basic first aid kit\"},{\"name\": \"Advanced\",\"description\": \"Advanced first aid kit\"}]}}".getBytes();
        when(ipfsServiceMock.loadFile("firstAid.json")).thenReturn(bytes);

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
        when(ipfsServiceMock.loadFile("firstAid.json")).thenReturn(bytes);

        firstAidService.loadFirstAidFromFile();

        //act
        JsonObject result = firstAidService.getFirstAidContent();

        //assert
        JsonObject expectedJsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
        assertEquals(expectedJsonObject, result);
    }

    @Test
    public void elementExists_ElementExists_ReturnsTrue() {
        //arrange
        IPFSService ipfsServiceMock = mock(IPFSService.class);
        FirstAidService firstAidService = new FirstAidService(ipfsServiceMock);

        JsonArray array = new JsonArray();
        JsonObject element1 = new JsonObject();
        element1.addProperty("name", "Test element 1");
        array.add(element1);
        JsonObject element2 = new JsonObject();
        element2.addProperty("name", "Test element 2");
        array.add(element2);

        //act
        boolean result = firstAidService.elementExists(array, "Test element 1");

        //assert
        assertTrue(result);
    }

    @Test
    public void elementExists_ElementDoesNotExist_ReturnsFalse() {
        //arrange
        IPFSService ipfsServiceMock = mock(IPFSService.class);
        FirstAidService firstAidService = new FirstAidService(ipfsServiceMock);

        JsonArray array = new JsonArray();
        JsonObject element1 = new JsonObject();
        element1.addProperty("name", "Test element 1");
        array.add(element1);
        JsonObject element2 = new JsonObject();
        element2.addProperty("name", "Test element 2");
        array.add(element2);

        //act
        boolean result = firstAidService.elementExists(array, "Test element 3");

        //assert
        assertFalse(result);
    }


    @Test
    public void elementExists_ArrayIsEmpty_ReturnsFalse() {
        //arrange
        IPFSService ipfsServiceMock = mock(IPFSService.class);
        FirstAidService firstAidService = new FirstAidService(ipfsServiceMock);

        JsonArray array = new JsonArray();
        //act
        boolean result = firstAidService.elementExists(array, "Test element 3");

        //assert
        assertFalse(result);
    }

    @Test
    public void addFirstAidKitToFile_ValidInput_AddsFirstAidKit() throws IOException {
        //arrange
        IPFSService ipfsServiceMock = mock(IPFSService.class);
        FirstAidService firstAidService = new FirstAidService(ipfsServiceMock);

        byte[] bytes = "{\"kits\": {\"types\": [{\"name\": \"Basic\",\"description\": \"Basic first aid kit\"},{\"name\": \"Advanced\",\"description\": \"Advanced first aid kit\"}]}}".getBytes();
        when(ipfsServiceMock.loadFile("firstAid.json")).thenReturn(bytes);

        firstAidService.loadFirstAidFromFile();

        FirstAidKitRequest request = new FirstAidKitRequest();
        request.setName("Test kit");
        request.setDescription("Test description");
        request.setItems(Arrays.asList("Test item 1", "Test item 2"));

        //act
        boolean result = firstAidService.addFirstAidKitToFile(request);

        //assert
        assertTrue(result);
        verify(ipfsServiceMock, times(1)).overrideFile(eq("firstAid"), any());
    }

    // TODO: 25.12.2022
//    @Test
//    public void addFirstAidKitToFile_KitWithSameNameExists_DoesNotAddFirstAidKit() throws IOException {
//        //arrange
//        IPFSService ipfsServiceMock = mock(IPFSService.class);
//        FirstAidService firstAidService = new FirstAidService(ipfsServiceMock);
//
//        byte[] bytes = "{\"kits\": {\"types\": [{\"name\": \"Basic\",\"description\": \"Basic first aid kit\"}]}}".getBytes();
//        when(ipfsServiceMock.loadFile("firstAid.json")).thenReturn(bytes);
//        when(ipfsServiceMock.overrideFile(anyString(), any(JsonObject.class))).thenReturn(true);
//        firstAidService.loadFirstAidFromFile();
//
//        FirstAidKitRequest request = new FirstAidKitRequest();
//        request.setName("Basic");
//        request.setDescription("Test description");
//        request.setItems(Arrays.asList("item1", "item2"));
//
//        //act
//        boolean result = firstAidService.addFirstAidKitToFile(request);
//
//        //assert
//        assertFalse(result);
//        verify(ipfsServiceMock, times(0)).overrideFile(anyString(), any(JsonObject.class));
//    }



}

