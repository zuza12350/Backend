package pl.edu.pjwstk.project.gun;

import com.google.gson.JsonArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.edu.pjwstk.project.gun.requests.GunRequest;
import pl.edu.pjwstk.project.gun.requests.GunTypeRequest;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GunControllerTest {
    @InjectMocks
    private GunController controller;

    @Mock
    private GunService service;

    @Test
    public void getGunsData_ShouldReturnOkStatusAndCorrectBody() {
        String kind = "pistol";
        JsonArray expectedBody = new JsonArray();
        when(service.getGunData(kind)).thenReturn(expectedBody);

        ResponseEntity<JsonArray> response = controller.getGunsData(kind);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void addGun_ShouldInvokeServiceMethod_WhenCategoryIdIsProvided() {
        Long categoryId = 1L;
        GunRequest gunRequest = new GunRequest();

        controller.addGun(categoryId, null, gunRequest);

        verify(service).addGun(categoryId, null, gunRequest);
    }

    @Test
    public void addGun_ShouldInvokeServiceMethod_WhenCategoryIdAndSubTypeAreProvided() {
        Long categoryId = 1L;
        Long subType = 2L;
        GunRequest gunRequest = new GunRequest();

        controller.addGun(categoryId, subType, gunRequest);


        verify(service).addGun(categoryId, subType, gunRequest);
    }

    @Test
    public void addGunType_ShouldInvokeServiceMethod() {
        GunTypeRequest gunTypeRequest = new GunTypeRequest();

        controller.addGunType(gunTypeRequest);

        verify(service).addGunType(gunTypeRequest);
    }

    @Test
    public void removeGun_ShouldInvokeServiceMethod() {
        String gunName = "gunName";

        controller.removeGun(gunName);

        verify(service).removeGunByName(gunName);
    }

    @Test
    public void removeGunType_ShouldInvokeServiceMethod() {
        String gunTypeName = "gunTypeName";

        controller.removeGunType(gunTypeName);

        verify(service).removeGunType(gunTypeName);
    }

}