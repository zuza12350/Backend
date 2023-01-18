package pl.edu.pjwstk.project.unitesting.survivalkit;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * SurvivalKitRequest is a class that is a reflection of the request for the survival tips. By using @Expose
 * allowed to serialize variables to json object. Annotation @Serializable tells Spring how to name a variable in json.
 *
 * @author Zuzanna Borkowska
 */
@Setter
@Getter
public class SurvivalKitRequest {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("needed_items")
    @Expose
    private JsonArray needed_items;
    @SerializedName("guide")
    @Expose
    private String guide;
}
