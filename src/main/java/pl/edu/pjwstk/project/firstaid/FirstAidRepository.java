package pl.edu.pjwstk.project.firstaid;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Repository;

@Repository
public interface FirstAidRepository {

    void setFirstAidFromFile();

    JsonObject getFirstAidContent();

    boolean addFirstAidKitToFile(FirstAidKitRequest firstAidKitRequest);

    boolean addLifeSupportActionToFile(LifeSupportActionRequest lifeSupportActionRequest);

    boolean removeLifeSupportActionFromFile(String name);

    boolean removeFirstAidKitFromFile(String name);
}
