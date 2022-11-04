package pl.edu.pjwstk.project.gun;

import com.google.gson.JsonArray;
import com.sun.istack.Nullable;
import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.project.gun.requests.GunRequest;
import pl.edu.pjwstk.project.gun.requests.GunTypeRequest;

@Repository
public interface GunRepository {
    void setGunsFromFile();
    JsonArray getGunData(String kind);
    void addGun(Long categoryId, @Nullable Long subType, GunRequest gunRequest);
    void addGunType(GunTypeRequest gunTypeRequest);
    void removeGunByName(String gunName);
    void removeGunType(String gunTypeName);
}
