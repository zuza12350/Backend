package pl.edu.pjwstk.project.firstaid;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * FirstAidKitRequest is class used to add and modify firstAidKit data.
 *
 * @author Mikołaj Noga
 */
@Getter
@Setter
public class FirstAidKitRequest {
    private String name, description, video;
    private List<String> elements;
}
