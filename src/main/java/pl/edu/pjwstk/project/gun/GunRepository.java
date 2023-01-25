package pl.edu.pjwstk.project.gun;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.istack.Nullable;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.project.gun.requests.GunRequest;
import pl.edu.pjwstk.project.gun.requests.GunTypeRequest;

/**
 * Interface with method declaration to add, delete or remove weapon information.
 *
 * @author Zuzanna Borkowska
 */
@Repository
public interface GunRepository {
    void setGunsFromFile();
    JsonObject getWholeGunData();
    JsonArray getGunData(String kind);
    boolean addGun(Long categoryId, @Nullable Long subType, GunRequest gunRequest);
    boolean addGunType(GunTypeRequest gunTypeRequest);
    boolean removeGunByName(String gunName);
    boolean removeGunType(String gunTypeName);
}
