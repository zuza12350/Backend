package pl.edu.pjwstk.project.gun;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Repository;

@Repository
public interface GunRepository {
    void setGunsFromFile();
    String getFullNameOfSubCategory(String subCategoryAcronym);
    JsonObject getSubCategory(String category, String subCategoryAcronym);
    JsonObject getGunCategory(String category);
    JsonArray getGunsFromCategory(String category);
    void addGunToCategory(String category, String subCategoryAcronym,GunRequest newGun);
    void createGunCategory(String categoryName, CategoryRequest categoryRequest);
    void deleteGunFromCategory(String categoryName,String subCategoryAcronym,String gunName);
    void removeCategory(String categoryName);
    void removeSubCategory(String subCategoryAcronym);

}
