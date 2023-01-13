package pl.edu.pjwstk.project.securityconfig;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.project.securityconfig.dto.AuthenticationRequest;

/**
 * The AuthController class is responsible for defining the endpoint for saving user, generating access key and user login thanks to @RestController, @PostMapping and @GetMapping annotations.
 *
 * @author Zuzanna Borkowska, Mikołaj Noga
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        final UserDetails userDetails = userService.findUserByUsername(request.getUsername());
        if(userDetails != null) {
            return ResponseEntity.ok(jwtUtils.generateToken(userDetails));
        }else{
            return ResponseEntity.status(400).body("Some error has occurred");
        }
    }

    @PostMapping("/createUser/{key}")
    public ResponseEntity<String> createUser(@RequestBody AuthenticationRequest request, @PathVariable String key) throws Exception {
        if (userService.createUser(request, key))
            return ResponseEntity.ok("redirect:/users/login");
        return ResponseEntity.badRequest().body("Some error has occurred");
    }

    @GetMapping("/generateKey")
    public ResponseEntity<String> generateKey() throws Exception {
        return ResponseEntity.ok(userService.generateKey());
    }

}
