package server.middleware;

import common.models.Permissions;
import common.utils.session.Session;
import common.router.Response;
import common.router.Request;
import common.router.response.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationTest {

    @Test
    public void InvalidAuthenticationTest() throws Exception {
        Permissions permission = Permissions.Random(0, "user");
        Session session = new Session(0, "user", permission);
        Request req = new Request("/foo", "foo", null, null);
        session.token = null;

        Response test = new Authentication.Authenticate().execute(req);
        assertEquals(Status.UNAUTHORIZED, test.status);
    }
}
