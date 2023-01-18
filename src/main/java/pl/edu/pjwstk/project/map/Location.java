package pl.edu.pjwstk.project.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing a location on a map.
 *
 * @author Mikołaj Noga
 */
@Getter
@Setter
@AllArgsConstructor
public class Location {
    private String name;
    private double latitude;
    private double longitude;
}
