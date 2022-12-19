package pl.edu.pjwstk.project.firstaid;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FirstAidKitRequest {
    private String name, description;
    private List<String> items;
}
