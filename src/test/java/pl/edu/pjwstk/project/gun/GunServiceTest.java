package pl.edu.pjwstk.project.gun;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.pjwstk.project.config.IPFSService;
import pl.edu.pjwstk.project.exceptions.ElementNotFoundException;
import pl.edu.pjwstk.project.gun.requests.GunRequest;
import pl.edu.pjwstk.project.gun.requests.GunTypeRequest;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class GunServiceTest {
    @Test
    public void addGunDataTest() throws ElementNotFoundException {
        IPFSService ipfsService = mock(IPFSService.class);
        GunService gunService = new GunService(ipfsService);
        GunRequest gunRequest = new GunRequest();
        gunRequest.setName("AK");
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("guns",new JsonArray());
        when(ipfsService.loadFile("guns")).thenReturn(jsonObject.toString().getBytes());

        boolean addGun = gunService.addGun(1L,1L,gunRequest);

        assertTrue(addGun);

    }
    @Test
    public void addGunTypeTest() throws ElementNotFoundException {
        IPFSService ipfsService = mock(IPFSService.class);
        GunService gunService = new GunService(ipfsService);
        GunTypeRequest gunTypeRequest = new GunTypeRequest();
        gunTypeRequest.setDescription("abc");
        gunTypeRequest.setName("someName");

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("guns_types",new JsonArray());
        when(ipfsService.loadFile("guns")).thenReturn(jsonObject.toString().getBytes());

        boolean addGunType = gunService.addGunType(gunTypeRequest);

        assertTrue(addGunType);

    }
    @Test
    public void removeGunTypeByNameIfNotFoundThenReturnFalse() throws ElementNotFoundException {
        IPFSService ipfsService = mock(IPFSService.class);
        GunService gunService = new GunService(ipfsService);
        GunTypeRequest gunTypeRequest = new GunTypeRequest();
        gunTypeRequest.setDescription("abc");
        gunTypeRequest.setName("someName");

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("guns_types",new JsonArray());
        when(ipfsService.loadFile("guns")).thenReturn(jsonObject.toString().getBytes());

        boolean addGunType = gunService.removeGunType("someName");

        assertFalse(addGunType);

    }
    @Test
    public void removeGunFromFileWhenGunIsNotFoundThenReturnFalse() {
        IPFSService ipfsService = mock(IPFSService.class);
        GunService gunService = new GunService(ipfsService);


        JsonObject jsonObject = new JsonObject();
        jsonObject.add("guns", new JsonArray());
        when(ipfsService.loadFile("guns")).thenReturn(jsonObject.toString().getBytes());
        boolean result = gunService.removeGunByName("testGun");
        assertFalse(result);
    }

    @Test
    public void removeGunTypeWhenGunTypeIsFoundThenReturnTrue() {
        IPFSService ipfsService = mock(IPFSService.class);
        GunService gunService = new GunService(ipfsService);

        JsonObject jsonObject = new JsonObject();
        JsonObject gunType = new JsonObject();
        gunType.addProperty("id",0);
        gunType.addProperty("name","testGunTypeName");
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(gunType);
        jsonObject.add("guns_types",jsonArray);
        when(ipfsService.loadFile("guns")).thenReturn(jsonObject.toString().getBytes());
        boolean result = gunService.removeGunType("testGunTypeName");
        assertTrue(result);
    }
    @Test
    public void removeGunTypeWhenGunIsFoundThenReturnTrue() {
        IPFSService ipfsService = mock(IPFSService.class);
        GunService gunService = new GunService(ipfsService);

        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("name","gunName");
        jsonArray.add(jsonObject1);
        jsonObject.add("guns",new JsonArray());
        when(ipfsService.loadFile("guns")).thenReturn(jsonObject.toString().getBytes());
        boolean result = gunService.removeGunByName("gunName");
        assertFalse(result);
    }



}
