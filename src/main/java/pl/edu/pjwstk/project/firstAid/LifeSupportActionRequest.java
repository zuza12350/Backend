package pl.edu.pjwstk.project.firstAid;

import com.google.gson.JsonArray;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LifeSupportActionRequest {
    private String name, description ;
    private JsonArray procedure;
}
