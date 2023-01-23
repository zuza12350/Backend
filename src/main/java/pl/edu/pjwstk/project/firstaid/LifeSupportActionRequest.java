package pl.edu.pjwstk.project.firstaid;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * LifeSupportActionRequest is class used to add and modify LifeSupportActionRequest data.
 *
 * @author Mikołaj Noga
 */
@Getter
@Setter
public class LifeSupportActionRequest {
    private String name, description, video;
    private List<String> procedure;
}
