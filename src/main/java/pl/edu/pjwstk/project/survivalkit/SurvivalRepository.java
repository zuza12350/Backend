package pl.edu.pjwstk.project.survivalkit;

import com.google.gson.JsonArray;
import org.springframework.stereotype.Repository;

@Repository
public interface SurvivalRepository {
    void setSurvivalKitFromFile();
    void addSurvivalTip(SurvivalKitRequest request);
    void removeSurvivalTip(String name);
    JsonArray getSurvivalData();

}
