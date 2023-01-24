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

/**
 *  The UserService class overrides the methods defined in UserRepository while adding all the logic that tells you how user is created or how to get information about logged user.
 *  It includes the @Service annotation, which assigns classes in the service layer in Spring.
 *
 * @author Zuzanna Borkowska, Mikołaj Noga
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserRepository{

    private static final String ALGORITHM = "AES";

    private static final String KEY = "B?E(H+MbQeThWmZq3t6w9z$C&F)J@NcR";

    private final IPFSService ipfsService;
    protected JsonObject jsonObject;


    /**
     * Method used to set the user information from a file on IPFS.
     */
    @Override
    public void setUsersFromFile() {
        byte[] bytes = ipfsService.loadFile("users");
        this.jsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    /**
     * Method used to hash a password using the BCrypt library.
     * @param password the password to be hashed
     * @param salt the salt used for hashing
     * @return the hashed password
     */
    protected static String hashPassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    /**
     * Method used to encrypt a user key using the AES algorithm.
     * @param key the key to be encrypted
     * @return the encrypted key
     * @throws Exception if encryption fails
     */
    protected static String encryptKey(String key) throws Exception {
        Key secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedKeyBytes = cipher.doFinal(key.getBytes());
        return Base64.getEncoder().encodeToString(encryptedKeyBytes);
    }

    /**
     * Method used to decrypt a user key using the AES algorithm.
     * @param encryptedKey the key to be decrypted
     * @return the decrypted key
     * @throws Exception if decryption fails
     */
    protected static String decryptKey(String encryptedKey) throws Exception {
        Key secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedKeyBytes = Base64.getDecoder().decode(encryptedKey);
        byte[] decryptedKeyBytes = cipher.doFinal(encryptedKeyBytes);
        return new String(decryptedKeyBytes);
    }

    /**
     * Method to check if provided key already exists in the list of keys.
     *
     * @param providedKey the key to be checked for existence
     * @param keys the list of keys to check against
     * @return true if the key already exists, false otherwise
     * @throws Exception if encryption of provided key fails
     */
    private static boolean keyExist(String providedKey, JsonArray keys) throws Exception {
        for (JsonElement key : keys)
            if (key.getAsJsonObject().get("encryptedKey").getAsString().equals(encryptKey(providedKey)))
                return true;
        return false;
    }

    /**
     * Method that generates a new key for the user.
     * It checks if the key already exists in the list of keys and if it does, it regenerates the key until it's unique.
     * It then adds the new key to the list of keys and saves the list to the file on IPFS.
     *
     * @return the newly generated key
     * @throws Exception if encryption of the key or saving to IPFS fails
     */
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

    /**
     * Method that creates a new user with given credentials and key.
     * The key is first checked to see if it exists in the list of keys.
     * It then checks if the provided username is already taken, and if not it creates a new user and adds it to the list of users.
     * It then removes the used key from the list of keys and saves the updated list to the file on IPFS.
     *
     * @param request the authentication request containing the new user's credentials
     * @param key the key to be used for creating the new user
     * @return true if user is created successfully, false otherwise
     * @throws Exception if encryption of key fails or saving to IPFS fails
     */
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

    /**
     * Method responsible for getting username of current authenticated user.
     * @return Name of current logged user.
     */
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

    /**
     * Method responsible for getting user core information by given username.
     * @param username variable which represents name of user.
     * @return UserDetails object which provides core user information.
     */
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


    /**
     * Method which builds needed user information for authentication.
     * @param user object with represents user for authentication
     * @param authorities list of user authorities
     * @return built user for authentication
     */
    private User buildUserForAuth(User user,
                                            List<GrantedAuthority> authorities) {
        return new User(user.getUsername(), user.getPassword(),
                true, true, true, true, authorities);
    }
}
