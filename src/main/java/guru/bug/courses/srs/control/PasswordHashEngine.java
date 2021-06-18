package guru.bug.courses.srs.control;

import guru.bug.courses.srs.control.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Singleton
public class PasswordHashEngine {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordHashEngine.class);

    public byte[] hash(String password, byte[] salt) throws ServiceException {
        byte[] hashedPassword;
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hashedPassword = f.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new ServiceException("Service error occurred", e);
        }
        return hashedPassword;
    }
}
