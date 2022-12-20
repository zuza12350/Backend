package gun;

import com.google.gson.JsonArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import pl.edu.pjwstk.project.gun.GunController;
import pl.edu.pjwstk.project.gun.GunService;
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
        // given
        String kind = "pistol";
        JsonArray expectedBody = new JsonArray();
        when(service.getGunData(kind)).thenReturn(expectedBody);

        // when
        ResponseEntity<JsonArray> response = controller.getGunsData(kind);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void addGun_ShouldInvokeServiceMethod_WhenCategoryIdIsProvided() {
        // given
        Long categoryId = 1L;
        GunRequest gunRequest = new GunRequest();

        // when
        controller.addGun(categoryId, null, gunRequest);

        // then
        verify(service).addGun(categoryId, null, gunRequest);
    }

    @Test
    public void addGun_ShouldInvokeServiceMethod_WhenCategoryIdAndSubTypeAreProvided() {
        // given
        Long categoryId = 1L;
        Long subType = 2L;
        GunRequest gunRequest = new GunRequest();

        // when
        controller.addGun(categoryId, subType, gunRequest);

        // then
        verify(service).addGun(categoryId, subType, gunRequest);
    }

    @Test
    public void addGunType_ShouldInvokeServiceMethod() {
        // given
        GunTypeRequest gunTypeRequest = new GunTypeRequest();

        // when
        controller.addGunType(gunTypeRequest);

        // then
        verify(service).addGunType(gunTypeRequest);
    }

    @Test
    public void removeGun_ShouldInvokeServiceMethod() {
        // given
        String gunName = "gunName";

        // when
        controller.removeGun(gunName);

        // then
        verify(service).removeGunByName(gunName);
    }

    @Test
    public void removeGunType_ShouldInvokeServiceMethod() {
        // given
        String gunTypeName = "gunTypeName";

        // when
        controller.removeGunType(gunTypeName);

        // then
        verify(service).removeGunType(gunTypeName);
    }

}