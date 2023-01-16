package pl.edu.pjwstk.project.securityconfig.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AuthenticationRequest class is a class that is a reflection of the request needed for authentication.
 *
 * @author Zuzanna Borkowska
 */
@Getter @Setter @NoArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String password;
}
