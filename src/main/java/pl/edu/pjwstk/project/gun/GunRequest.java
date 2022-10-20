package pl.edu.pjwstk.project.gun;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GunRequest {
    private String name;
    private String description;
    private String caliber;
    private String barrelLength;
    private String weight;
    private String velocityOfBullets;
    private String magazine;
    private String bullet;
    private String range;
    private String photo;
    private String url;
}
