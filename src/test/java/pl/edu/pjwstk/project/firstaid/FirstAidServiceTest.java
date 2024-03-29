package pl.edu.pjwstk.project.firstaid;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.pjwstk.project.config.IPFSService;
import pl.edu.pjwstk.project.securityconfig.UserService;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirstAidServiceTest {

    @Mock
    private IPFSService ipfsService;
    @Mock
    private UserService userService;

    @InjectMocks
    private FirstAidService firstAidService;

    /**
     * Should return false when the life support action is not found
     */
    @Test
    public void removeLifeSupportActionFromFileWhenLifeSupportActionIsNotFoundThenReturnFalse() {
        JsonObject supportActions = new JsonObject();
        JsonArray types = new JsonArray();
        JsonObject type = new JsonObject();

        type.addProperty("name", "test");
        types.add(type);

        supportActions.add("types", types);

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("support_actions", supportActions);
        when(ipfsService.loadFile("firstAid")).thenReturn(jsonObject.toString().getBytes());
        boolean result = firstAidService.removeLifeSupportActionFromFile("test1");
        assertFalse(result);


    }

    /**
     * Should return false when the life support action does not exist
     */
    @Test
    public void editLifeSupportActionInFileWhenLifeSupportActionDoesNotExistThenReturnFalse() {
        LifeSupportActionRequest lifeSupportActionRequest = new LifeSupportActionRequest();
        lifeSupportActionRequest.setName("name");
        lifeSupportActionRequest.setDescription("description");
        lifeSupportActionRequest.setVideo("video");
        lifeSupportActionRequest.setElements(new ArrayList<>());

        JsonObject jsonObject = new JsonObject();
        JsonObject supportActions = new JsonObject();
        JsonArray types = new JsonArray();
        supportActions.add("types", types);
        jsonObject.add("support_actions", supportActions);

        when(ipfsService.loadFile("firstAid")).thenReturn(jsonObject.toString().getBytes());

        boolean result =
                firstAidService.editLifeSupportActionInFile("name", lifeSupportActionRequest);

        assertFalse(result);
    }

    /**
     * Should return true when the life support action exists
     */
    @Test
    public void editLifeSupportActionInFileWhenLifeSupportActionExistsThenReturnTrue() {
        JsonObject jsonObject = new JsonObject();
        JsonObject supportActions = new JsonObject();
        JsonArray types = new JsonArray();
        JsonObject lifeSupportAction = new JsonObject();
        lifeSupportAction.addProperty("name", "test");
        lifeSupportAction.addProperty("description", "test");
        lifeSupportAction.addProperty("video", "test");
        JsonArray procedure = new JsonArray();
        procedure.add("test");
        lifeSupportAction.add("procedure", procedure);
        types.add(lifeSupportAction);
        supportActions.add("types", types);
        jsonObject.add("support_actions", supportActions);
        when(ipfsService.loadFile("firstAid")).thenReturn(jsonObject.toString().getBytes());

        LifeSupportActionRequest request = new LifeSupportActionRequest();
        request.setName("test");
        request.setDescription("test");
        request.setVideo("test");
        request.setElements(new ArrayList<>());

        boolean result = firstAidService.editLifeSupportActionInFile("test", request);

        assertTrue(result);
    }

    /**
     * Should return false when the kit does not exist
     */
    @Test
    public void editFirstAidKitInFileWhenKitDoesNotExistThenReturnFalse() {
        FirstAidService firstAidService = new FirstAidService(ipfsService, userService);
        FirstAidKitRequest request = new FirstAidKitRequest();
        request.setName("name");
        request.setDescription("description");
        request.setElements(new ArrayList<>());

        JsonObject jsonObject = new JsonObject();
        JsonObject kits = new JsonObject();
        JsonArray types = new JsonArray();
        kits.add("types", types);
        jsonObject.add("kits", kits);

        when(ipfsService.loadFile("firstAid")).thenReturn(jsonObject.toString().getBytes());

        assertFalse(firstAidService.editFirstAidKitInFile("name", request));
    }

    /**
     * Should return true when the kit exists
     */
    @Test
    public void editFirstAidKitInFileWhenKitExistsThenReturnTrue() {

        JsonObject jsonObject = new JsonObject();
        JsonObject kits = new JsonObject();
        JsonArray types = new JsonArray();
        JsonObject kit = new JsonObject();
        kit.addProperty("name", "kit");
        kit.addProperty("description", "desc");
        JsonArray items = new JsonArray();
        items.add("item1");
        items.add("item2");
        kit.add("items", items);
        types.add(kit);
        kits.add("types", types);
        jsonObject.add("kits", kits);

        when(ipfsService.loadFile("firstAid")).thenReturn(jsonObject.toString().getBytes());

        FirstAidKitRequest request = new FirstAidKitRequest();
        request.setName("kit");
        request.setDescription("desc");
        ArrayList<String> itemsList = new ArrayList<>();
        itemsList.add("item1");
        itemsList.add("item2");
        request.setElements(itemsList);

        assertTrue(firstAidService.editFirstAidKitInFile("kit", request));
    }

    /**
     * Should return true when the life support action is not in the file
     */
    @Test
    public void addLifeSupportActionToFileWhenLifeSupportActionIsNotInTheFileThenReturnTrue() {
        LifeSupportActionRequest lifeSupportActionRequest = new LifeSupportActionRequest();
        lifeSupportActionRequest.setName("name");
        lifeSupportActionRequest.setDescription("description");
        lifeSupportActionRequest.setVideo("video");
        lifeSupportActionRequest.setElements(new ArrayList<>());

        JsonObject jsonObject = new JsonObject();
        JsonObject supportActions = new JsonObject();
        JsonArray types = new JsonArray();
        supportActions.add("types", types);
        jsonObject.add("support_actions", supportActions);

        when(ipfsService.loadFile("firstAid")).thenReturn(jsonObject.toString().getBytes());

        boolean result = firstAidService.addLifeSupportActionToFile(lifeSupportActionRequest);

        assertTrue(result);
    }

    /**
     * Should return false when the life support action is already in the file
     */
    @Test
    public void addLifeSupportActionToFileWhenLifeSupportActionIsAlreadyInTheFileThenReturnFalse() {
        LifeSupportActionRequest lifeSupportActionRequest = new LifeSupportActionRequest();
        lifeSupportActionRequest.setName("name");
        lifeSupportActionRequest.setDescription("description");
        lifeSupportActionRequest.setVideo("video");
        lifeSupportActionRequest.setElements(new ArrayList<>());

        JsonObject supportActions = new JsonObject();
        JsonArray types = new JsonArray();
        JsonObject type = new JsonObject();

        type.addProperty("name", "name");
        types.add(type);

        supportActions.add("types", types);

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("support_actions", supportActions);

        when(ipfsService.loadFile("firstAid")).thenReturn(jsonObject.toString().getBytes());

        boolean result = firstAidService.addLifeSupportActionToFile(lifeSupportActionRequest);

        assertFalse(result);
    }

    /**
     * Should return true when the kit does not exist
     */
    @Test
    public void addFirstAidKitToFileWhenKitDoesNotExistThenReturnTrue() {
        FirstAidKitRequest request = new FirstAidKitRequest();
        request.setName("test");
        request.setDescription("test");
        request.setElements(new ArrayList<>());
        JsonObject jsonObject = new JsonObject();
        JsonObject kits = new JsonObject();
        JsonArray types = new JsonArray();
        kits.add("types", types);
        jsonObject.add("kits", kits);
        when(ipfsService.loadFile("firstAid")).thenReturn(jsonObject.toString().getBytes());

        boolean result = firstAidService.addFirstAidKitToFile(request);

        assertTrue(result);
    }

    /**
     * Should return false when the kit already exists
     */
    @Test
    public void addFirstAidKitToFileWhenKitAlreadyExistsThenReturnFalse() {
        FirstAidKitRequest request = new FirstAidKitRequest();
        request.setName("test");
        request.setDescription("test");
        request.setElements(new ArrayList<>());
        JsonObject jsonObject = new JsonObject();
        JsonObject kits = new JsonObject();
        JsonArray types = new JsonArray();
        JsonObject kit = new JsonObject();
        kit.addProperty("name", "test");
        kit.addProperty("description", "test");
        kit.add("items", new JsonArray());
        types.add(kit);
        kits.add("types", types);
        jsonObject.add("kits", kits);
        when(ipfsService.loadFile("firstAid")).thenReturn(jsonObject.toString().getBytes());

        boolean result = firstAidService.addFirstAidKitToFile(request);

        assertFalse(result);
    }

    @Test
    public void elementExists_ElementExists_ReturnsTrue() {
        //arrange


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

        JsonArray array = new JsonArray();
        //act
        boolean result = firstAidService.elementExists(array, "Test element 3");

        //assert
        assertFalse(result);
    }


}

