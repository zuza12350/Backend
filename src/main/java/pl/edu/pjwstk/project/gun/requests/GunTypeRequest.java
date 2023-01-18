package pl.edu.pjwstk.project.unitesting.gun.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * GunTypeRequest is a class that is a reflection of the request for the type of weapons. By using @Expose
 * allowed to serialize variables to json object. Annotation @Serializable tells Spring how to name a variable in json.
 *
 * @author Zuzanna Borkowska
 */
@Getter
@Setter
public class GunTypeRequest {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;
}
