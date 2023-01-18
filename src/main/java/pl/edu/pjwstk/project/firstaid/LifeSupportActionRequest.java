package pl.edu.pjwstk.project.unitesting.firstaid;

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
    private String name, description ;
    private List<String> procedure;
}
