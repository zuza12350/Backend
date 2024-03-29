package pl.edu.pjwstk.project.gun.requests;

import lombok.Getter;
import lombok.Setter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * GunRequest is a class that is a reflection of the request for the weapon. By using @Expose
 * allowed to serialize variables to json object. Annotation @Serializable tells Spring how to name a variable in json.
 *
 * @author Zuzanna Borkowska
 */
@Getter
@Setter
public class GunRequest {
    @SerializedName("type")
    @Expose
    private Long type;
    @SerializedName("sub_type")
    @Expose
    private Long subType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String  description;
    @SerializedName("caliber")
    @Expose
    private double  caliber;
    @SerializedName("barrelLength")
    @Expose
    private Long  barrelLength;
    @SerializedName("weight")
    @Expose
    private double weight;
    @SerializedName("fireRate")
    @Expose
    private double fireRate;
    @SerializedName("range")
    @Expose
    private Long range;
    @SerializedName("magazines")
    @Expose
    private List<MagazineRequest> magazines;
    @SerializedName("ammunition")
    @Expose
    private String ammunition;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("video")
    @Expose
    private String video;

    private String createdBy;
}
