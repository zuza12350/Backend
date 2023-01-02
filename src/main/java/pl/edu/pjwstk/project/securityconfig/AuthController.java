package pl.edu.pjwstk.project.securityconfig;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.project.securityconfig.dao.UserDao;
import pl.edu.pjwstk.project.securityconfig.dto.AuthenticationRequest;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final JwtUtils jwtUtils;

    @PostMapping("/auth")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        final UserDetails userDetails = userDao.findUserByUsername(request.getUsername());
        if(userDetails != null) {
            return ResponseEntity.ok(jwtUtils.generateToken(userDetails));
        }else{
            return ResponseEntity.status(400).body("Some error has occurred");
        }
    }
}
