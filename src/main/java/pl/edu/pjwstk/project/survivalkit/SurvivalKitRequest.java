package pl.edu.pjwstk.project.survivalkit;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

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
