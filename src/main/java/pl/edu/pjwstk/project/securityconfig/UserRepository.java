package pl.edu.pjwstk.project.securityconfig;

import org.springframework.stereotype.Repository;
import pl.edu.pjwstk.project.securityconfig.dto.AuthenticationRequest;

@Repository
public interface UserRepository {

    void setUsersFromFile();
    boolean createUser(AuthenticationRequest request, String key) throws Exception;
    String generateKey() throws Exception;
}
