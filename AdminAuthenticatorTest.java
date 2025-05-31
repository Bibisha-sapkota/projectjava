package coursework;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AdminAuthenticatorTest {

    @Test
    void testValidAdminLogin() {
        assertTrue(AdminAuthenticator.authenticate("ADMIN", "quizgame"), "Admin should log in successfully.");
    }

    @Test
    void testInvalidAdminLogin() {
        assertFalse(AdminAuthenticator.authenticate("admin", "wrongpassword"), "Invalid credentials should fail.");
    }

    @Test
    void testEmptyUsernameAndPassword() {
        assertFalse(AdminAuthenticator.authenticate("", ""), "Empty credentials should fail.");
    }

    @Test
    void testNonExistentUser() {
        assertFalse(AdminAuthenticator.authenticate("fakeUser", "fakePass"), "Non-existent user should not log in.");
    }
}
