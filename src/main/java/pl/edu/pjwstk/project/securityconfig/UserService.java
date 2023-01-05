package pl.edu.pjwstk.project.securityconfig;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.project.config.IPFSService;
import pl.edu.pjwstk.project.securityconfig.dto.AuthenticationRequest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserService implements UserRepository{
    private static final String ALGORITHM = "AES";

    private static final String KEY = "B?E(H+MbQeThWmZq3t6w9z$C&F)J@NcR";

    private final IPFSService ipfsService;
    private JsonObject jsonObject;

    @Override
    public void setUsersFromFile() {
        byte[] bytes = ipfsService.loadFile("users.json");
        this.jsonObject = JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
    }

    private static String hashPassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    // TODO: 05.01.2023 do sprawdzenia bo ta wersja dekryptowania jest wrażliwa
    private static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
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
        // TODO: 05.01.2023 new.Key.addProperty("createdBy", "usernameOfUserWhoCreatedThisKey")

        jsonObject.getAsJsonArray("newKeys").add(newKey);
        ipfsService.overrideFile("users", jsonObject);
        return key;
    }


    @Override
    public boolean createUser(AuthenticationRequest request, String key) throws Exception {
        setUsersFromFile();
        if(!keyExist(key, jsonObject.getAsJsonArray("newKeys")))
            return false;


        var newUser = new JsonObject();
        newUser.addProperty("username", request.getUsername());
        newUser.addProperty("password", hashPassword(request.getPassword(), BCrypt.gensalt(10)));
        // TODO: 05.01.2023 newUser.addProperty("addedBy", "usernameOfUserWhoAddedThisUser");

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
}
