package pl.edu.pjwstk.project.gun;

import com.google.gson.JsonArray;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import pl.edu.pjwstk.project.gun.requests.GunRequest;
import pl.edu.pjwstk.project.gun.requests.GunTypeRequest;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GunRepositoryTest {

    @Mock
    private GunRepository gunRepository;

    @Test
    public void testSetGunsFromFile() {
        gunRepository.setGunsFromFile();
        verify(gunRepository).setGunsFromFile();
    }

    @Test
    public void testGetGunData() {
        String kind = "pistol";
        JsonArray expectedData = new JsonArray();
        when(gunRepository.getGunData(kind)).thenReturn(expectedData);

        JsonArray actualData = gunRepository.getGunData(kind);
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testAddGun() {
        Long categoryId = 1L;
        Long subType = 2L;
        GunRequest request = new GunRequest();
        request.setName("Glock 17");
        request.setCaliber(9.0);
        request.setPhoto("semiautomatic");
        request.setCaliber(17.0);

        gunRepository.addGun(categoryId, subType, request);
        verify(gunRepository).addGun(categoryId, subType, request);
    }

    @Test
    public void testAddGunType() {
        GunTypeRequest request = new GunTypeRequest();
        request.setName("pistol");
        request.setDescription("A pistol is a type of handgun.");

        gunRepository.addGunType(request);
        verify(gunRepository).addGunType(request);
    }

    @Test
    public void testRemoveGunByName() {
        String name = "Glock 17";
        gunRepository.removeGunByName(name);
        verify(gunRepository).removeGunByName(name);
    }

    @Test
    public void testRemoveGunType() {
        String name = "pistol";
        gunRepository.removeGunType(name);
        verify(gunRepository).removeGunType(name);
    }
}
