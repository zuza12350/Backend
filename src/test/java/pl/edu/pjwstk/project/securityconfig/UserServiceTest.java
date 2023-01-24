package pl.edu.pjwstk.project.securityconfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.edu.pjwstk.project.config.IPFSService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private IPFSService ipfsService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetUsersFromFile() {
        byte[] bytes = "users".getBytes();
        when(ipfsService.loadFile("users")).thenReturn(bytes);

        userService.setUsersFromFile();

        assertNotNull(userService.jsonObject);
    }

    @Test
    public void testHashPassword() {
        String password = "password";
        String salt = "$2a$10$B?E(H+MbQeThWmZq3t6w9z$C&F)J@NcR";

        String hashedPassword = userService.hashPassword(password, salt);

        assertNotNull(hashedPassword);
        assertEquals("$2a$10$B?E(H+MbQeThWmZq3t6w9z$C&F)J@NcR", hashedPassword);
    }

    @Test
    public void testEncryptKey() throws Exception {
        String key = "key";

        String encryptedKey = userService.encryptKey(key);

        assertNotNull(encryptedKey);
        assertEquals("key", encryptedKey);
    }

    @Test
    public void testDecryptKey() throws Exception {
        String encryptedKey = "encryptedKey";

        String decryptedKey = userService.decryptKey(encryptedKey);

        assertNotNull(decryptedKey);
        assertEquals("encryptedKey", decryptedKey);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsername() {
        String username = "username";

        userService.findUserByUsername(username);
    }
}
