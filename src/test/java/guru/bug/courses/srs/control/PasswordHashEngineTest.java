package guru.bug.courses.srs.control;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashEngineTest {
    static final byte[] salt1 = new byte[] {1, 2, 3, 4};

    @Test
    void hashSamePassword() throws Exception {
        var hash1 = PasswordHashEngine.hash("12345", salt1);
        var hash2 = PasswordHashEngine.hash("12345", salt1);
        assertArrayEquals(hash1, hash2);
    }

    @Test
    void hashDifferentPasswords() throws Exception {
        var hash1 = PasswordHashEngine.hash("54321", salt1);
        var hash2 = PasswordHashEngine.hash("12345", salt1);
        assertFalse(Arrays.equals(hash1, hash2));
    }
}