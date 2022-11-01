package pl.edu.pjwstk.project.firstAid;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Repository;

@Repository
public interface firstAidRepository {

    JsonObject getFirstAidContent();

    void setFirstAidFromFile();
}
