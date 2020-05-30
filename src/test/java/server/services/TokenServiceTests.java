package server.services;

import common.models.Permissions;
import common.utils.session.Session;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
public class TokenServiceTests {
    // Test assigning Tokens, generating hashes, mock logging out, etc

    @Test
    public void TestVerifyingToken() throws Exception {
        Permissions perm = Permissions.Random(1,"kevin");
        Session newSess = TokenService.getInstance().createSession(1,"kevin",perm);
        assertTrue(TokenService.getInstance().verify(newSess.token));
    }

    @Test
    public void TestWrongToken() throws Exception {
        assertFalse(TokenService.getInstance().verify("blahblahblah"));
    }

    @Test
    public void TestInvalidToken() throws Exception {
        assertFalse(TokenService.getInstance().verify(null));
    }

    @Test
    public void TestEmptyToken() throws Exception{
        assertFalse(TokenService.getInstance().verify(""));
    }

    @Test
    public void TestExpiredToken() throws Exception {
        Permissions perm = Permissions.Random(1,"kevin");
        Session newSess = TokenService.getInstance().createSession(1,"kevin",perm);
        newSess.expireTime = newSess.expireTime.minus(2, ChronoUnit.DAYS);
        assertTrue(TokenService.getInstance().expired(newSess.token));
    }

    @Test
    public void TestNonExpiredToken() throws Exception {
        Permissions perm = Permissions.Random(1,"kevin");
        Session newSess = TokenService.getInstance().createSession(1,"kevin",perm);
        assertFalse(TokenService.getInstance().expired(newSess.token));
    }
    @Test
    public void TestExpiredEmptyToken() throws Exception {
        assertTrue(TokenService.getInstance().expired(""));
    }

    @Test
    public void TestExpiredNullToken() throws Exception {
        assertTrue(TokenService.getInstance().expired(null));
    }

    @Test
    public void TestExpiredUnknowToken() throws Exception {
        assertTrue(TokenService.getInstance().expired("Audiisrtarded"));
    }

    @Test
    public void testGetSessionByUsername() throws Exception{
        Permissions perm = Permissions.Random(1,"audi");
        Session newSess = TokenService.getInstance().createSession(1,"audi",perm);
        Optional<Session> result = TokenService.getInstance().getSessionByUsername("audi");
        assertEquals(newSess,result.get());
    }

    @Test
    public void testGetSessionWithUnknownUsername() throws Exception{
        Optional<Session> result = TokenService.getInstance().getSessionByUsername("jamie");
        assert result.isEmpty();
    }

    @Test
    public void testGetSessionWithEmptyUsername() throws Exception{
        Optional<Session> result = TokenService.getInstance().getSessionByUsername("");
        assert result.isEmpty();
    }

    @Test
    public void testGetSessionWithNullUsername() throws Exception{
        Optional<Session> result = TokenService.getInstance().getSessionByUsername(null);
        assertNull(result);
    }

    @Test
    public void testGetSessionByToken() throws Exception{
        Permissions perm = Permissions.Random(1,"kevin");
        Session newSess = TokenService.getInstance().createSession(1,"kevin",perm);
        Optional<Session> result = TokenService.getInstance().getSessionByToken(newSess.token);
        assertEquals(newSess,result.get());
    }

    @Test
    public void testGetSessionWithUnknownToken() throws Exception{
        Optional<Session> result = TokenService.getInstance().getSessionByToken("randomToken");
        assert result.isEmpty();
    }
    @Test
    public void testGetSessionWithEmptyToken() throws Exception{
        Optional<Session> result = TokenService.getInstance().getSessionByToken("");
        assert result.isEmpty();
    }

    @Test
    public void testGetSessionWithNullToken() throws Exception{
        Optional<Session> result = TokenService.getInstance().getSessionByToken(null);
        assertNull(result);
    }
}
