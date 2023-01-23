package pl.edu.pjwstk.project.firstaid;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.pjwstk.project.config.IPFSService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FirstAidServiceTest {




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


}

