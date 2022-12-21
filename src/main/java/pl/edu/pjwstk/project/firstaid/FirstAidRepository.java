package pl.edu.pjwstk.project.firstaid;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Repository;

@Repository
public interface FirstAidRepository {

    void loadFirstAidFromFile();

    JsonObject getFirstAidContent();

    boolean addFirstAidKitToFile(FirstAidKitRequest firstAidKitRequest);

    boolean addLifeSupportActionToFile(LifeSupportActionRequest lifeSupportActionRequest);

    boolean editLifeSupportActionInFile(String name, LifeSupportActionRequest request);

    boolean editFirstAidKitInFile(String name, FirstAidKitRequest request);

    boolean removeLifeSupportActionFromFile(String name);

    boolean removeFirstAidKitFromFile(String name);
}
