package pl.edu.pjwstk.project.securityconfig;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.project.config.IPFSService;
import pl.edu.pjwstk.project.securityconfig.dto.AuthenticationRequest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserRepository{

    private static final String ALGORITHM = "AES";

    private static final String KEY = "B?E(H+MbQeThWmZq3t6w9z$C&F)J@NcR";

    private final IPFSService ipfsService;
    private JsonObject jsonObject;

    @Override
    public void setUsersFromFile() {
        byte[] bytes = ipfsService.loadFile("users");
        this.jsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    private static String hashPassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    private static String encryptKey(String key) throws Exception {
        Key secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedKeyBytes = cipher.doFinal(key.getBytes());
        return Base64.getEncoder().encodeToString(encryptedKeyBytes);
    }

    private static String decryptKey(String encryptedKey) throws Exception {
        Key secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedKeyBytes = Base64.getDecoder().decode(encryptedKey);
        byte[] decryptedKeyBytes = cipher.doFinal(encryptedKeyBytes);
        return new String(decryptedKeyBytes);
    }

    private static boolean keyExist(String providedKey, JsonArray keys) throws Exception {
        for (JsonElement key : keys)
            if (key.getAsJsonObject().get("encryptedKey").getAsString().equals(encryptKey(providedKey)))
                return true;
        return false;
    }

    @Override
    public String generateKey() throws Exception {
        setUsersFromFile();
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[16];
        random.nextBytes(keyBytes);
        String key = Base64.getEncoder().encodeToString(keyBytes);

        while (keyExist(key, jsonObject.getAsJsonArray("newKeys"))) {
            random.nextBytes(keyBytes);
            key = Base64.getEncoder().encodeToString(keyBytes);
        }

        var newKey = new JsonObject();
        newKey.addProperty("encryptedKey", encryptKey(key));
        newKey.addProperty("createdBy", getUsernameOfCurrentLoggedUser());

        jsonObject.getAsJsonArray("newKeys").add(newKey);
        ipfsService.overrideFile("users", jsonObject);
        return key;
    }


    @Override
    public boolean createUser(AuthenticationRequest request, String key) throws Exception {
        setUsersFromFile();
        if(!keyExist(key, jsonObject.getAsJsonArray("newKeys")))
            return false;

        for (JsonElement username : jsonObject.getAsJsonArray("users"))
            if (username.getAsJsonObject().get("username").getAsString().equals(request.getUsername()))
                return false;


        var newUser = new JsonObject();

        newUser.addProperty("username", request.getUsername());
        newUser.addProperty("password", hashPassword(request.getPassword(), BCrypt.gensalt(10)));

         newUser.addProperty("createdBy", getUsernameOfCurrentLoggedUser());

        jsonObject.getAsJsonArray("users").add(newUser);

        JsonArray keys = jsonObject.getAsJsonArray("newKeys");
        for (int i = 0; i < keys.size(); i++) {
            JsonObject temp = keys.get(i).getAsJsonObject();
            if (temp.get("encryptedKey").getAsString().equals(encryptKey(key))) {
                keys.remove(i);
                break;
            }
        }

        ipfsService.overrideFile("users", jsonObject);
        return true;
    }
    @Override
    public String getUsernameOfCurrentLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return null;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
    @Override
    public UserDetails findUserByUsername(String username) {
        setUsersFromFile();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<User>>(){}.getType();
        List<User> appUsers = gson.fromJson(this.jsonObject.getAsJsonArray("users"),collectionType);
        var appUser =  appUsers
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_MODERATOR"));
        return buildUserForAuth(appUser, authorities);
    }


    private User buildUserForAuth(User user,
                                            List<GrantedAuthority> authorities) {
        return new User(user.getUsername(), user.getPassword(),
                true, true, true, true, authorities);
    }
}
