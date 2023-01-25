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
    boolean addSurvivalTip(SurvivalKitRequest request);
    boolean removeSurvivalTip(String name);
    JsonArray getSurvivalTips();

}
