package pl.edu.pjwstk.project.survivalkit;

import com.google.gson.JsonArray;
import org.springframework.stereotype.Repository;

/**
 * Interface with method declaration to add, delete or remove survival tips information.
 *
 * @author Zuzanna Borkowska
 */
@Repository
public interface SurvivalRepository {
    void setSurvivalKitFromFile();
    void addSurvivalTip(SurvivalKitRequest request);
    void removeSurvivalTip(String name);
    JsonArray getSurvivalTips();

}
