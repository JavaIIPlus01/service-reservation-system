package guru.bug.courses.srs.control;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PasswordHashEngineTest {

    static final byte[] salt = "salt".getBytes();
    static final byte[] salt1 = "salt1".getBytes();

    @Inject
    PasswordHashEngine passwordHashEngine;

    @Test
    void whenSameSaltAndPasswordsHashesShouldMatchTest() throws Exception {
        var hash1 = passwordHashEngine.hash("password1", salt);
        var hash2 = passwordHashEngine.hash("password1", salt);
        assertArrayEquals(hash1, hash2);
    }

    @Test
    void whenDifferentSaltAndSamePasswordsHashesShouldDiffer() throws Exception {
        var hash1 = passwordHashEngine.hash("password1", salt);
        var hash2 = passwordHashEngine.hash("password1", salt1);
        assertFalse(Arrays.equals(hash1, hash2));
    }

    @Test
    void whenSameSaltAndDifferentPasswordsHashesShouldDiffer() throws Exception {
        var hash1 = passwordHashEngine.hash("password1", salt);
        var hash2 = passwordHashEngine.hash("password2", salt);
        assertFalse(Arrays.equals(hash1, hash2));
    }
}