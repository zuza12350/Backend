package pl.edu.pjwstk.project.unitesting.gun.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * MagazineRequest is a class that is a reflection of the request for the magazines array in specific weapon. By using @Expose
 * allowed to serialize variables to json object. Annotation @Serializable tells Spring how to name a variable in json.
 *
 * @author Zuzanna Borkowska
 */
@Getter
@Setter
public class MagazineRequest {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("capacity")
    @Expose
    private String capacity;

}
