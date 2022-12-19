package pl.edu.pjwstk.project.firstaid;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LifeSupportActionRequest {
    private String name, description ;
    private List<String> procedure;
}
