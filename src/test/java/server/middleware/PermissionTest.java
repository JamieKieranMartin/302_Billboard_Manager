package server.middleware;

import common.models.Billboard;
import common.models.Permissions;
import common.utils.session.Session;
import common.models.User;
import common.router.Response;
import common.router.Request;
import common.router.response.Status;
import common.utils.session.HashingFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermissionTest {

    Request req = null;
    Permissions permission = null;

    @BeforeEach
    public void PermissionInitialiseTest() {
        req = new Request("/foo", "foo", null, null);
        permission = Permissions.Random(0, "user");
    }

    @Test
    public void ValidCanEditUserTest() throws Exception {
        permission.canEditUser = true;
        req.permissions = permission;

        Response test = new Permission.canEditUser().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void InvalidCanEditUserTest() throws Exception {
        permission.canEditUser = false;
        req.permissions = permission;

        Response test = new Permission.canEditUser().execute(req);
        assertEquals(Status.UNAUTHORIZED, test.status);
    }

    @Test
    public void ValidCanCreateBillboardTest() throws Exception {
        permission.canCreateBillboard = true;
        req.permissions = permission;

        Response test = new Permission.canCreateBillboard().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void InvalidCanCreateBillboardTest() throws Exception {
        permission.canCreateBillboard = false;
        req.permissions = permission;

        Response test = new Permission.canCreateBillboard().execute(req);
        assertEquals(Status.UNAUTHORIZED, test.status);
    }

    @Test
    public void ValidCanScheduleBillboardTest() throws Exception {
        permission.canScheduleBillboard = true;
        req.permissions = permission;

        Response test = new Permission.canScheduleBillboard().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void InvalidCanScheduleBillboardTest() throws Exception {
        permission.canScheduleBillboard = false;
        req.permissions = permission;

        Response test = new Permission.canScheduleBillboard().execute(req);
        assertEquals(Status.UNAUTHORIZED, test.status);
    }

    @Test
    public void AdminChangePasswordTest() throws Exception {
        permission.canEditUser = true;
        req.permissions = permission;

        Response test = new Permission.canChangePassword().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void ChangeOwnPasswordTest() throws Exception {
        // Create a params with the username that is gonna change password
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "kevin");
        params.put("password", HashingFactory.hashPassword("1234"));

        req = new Request("/foo", "foo", params, null);
        req.session = new Session(
            0, "kevin", permission
        );

        permission.canEditUser = false;
        req.permissions = permission;
        Response test = new Permission.canChangePassword().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void ChangeOtherPasswordWithoutPermission() throws Exception {
        // Create a params with the username that is gonna change password
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "kevin");
        params.put("password", HashingFactory.hashPassword("1234"));

        req = new Request("/foo", "foo", params, null);
        req.session = new Session(
            0, "kevin1", permission
        );
        permission.canEditUser = false;
        req.permissions = permission;
        Response test = new Permission.canChangePassword().execute(req);
        assertEquals(Status.UNAUTHORIZED, test.status);
    }

    @Test
    public void ValidCanEditBillboardTest() throws Exception {
        permission.canEditBillboard = true;
        req.permissions = permission;

        Response test = new Permission.canEditBillboard().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void EditOwnBillboardThatIsNotScheduled() throws Exception {
        permission.canEditBillboard = false;

        Billboard bb = Billboard.Random(1);
        bb.locked = false;
        bb.id = 4;
        bb.userId = 1;

        req = new Request("/foo", "foo", null, bb);
        req.session = new Session(
            1, "kevin", permission
        );
        req.permissions = permission;

        Response test = new Permission.canEditBillboard().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void InvalidRequestBody() throws Exception {
        permission.canEditBillboard = false;

        Billboard bb = Billboard.Random(1);
        bb.locked = false;
        bb.id = 4;
        bb.userId = 1;

        req = new Request("/foo", "foo", null, null);
        req.session = new Session(
            1, "kevin", permission
        );
        req.permissions = permission;

        Response test = new Permission.canEditBillboard().execute(req);
        assertEquals(Status.UNSUPPORTED_TYPE, test.status);
    }

    @Test
    public void LockedBillboard() throws Exception {
        permission.canEditBillboard = false;

        Billboard bb = Billboard.Random(1);
        bb.locked = true;
        bb.id = 4;
        bb.userId = 1;

        req = new Request("/foo", "foo", null, bb);
        req.session = new Session(
            1, "kevin", permission
        );
        req.permissions = permission;

        Response test = new Permission.canEditBillboard().execute(req);
        assertEquals(Status.BAD_REQUEST, test.status);
        assertEquals("Can't change a scheduled billboard.", test.message);
    }

    @Test
    public void UnknownBillboard() throws Exception {
        permission.canEditBillboard = false;

        Billboard bb = Billboard.Random(1);
        bb.locked = false;
        bb.userId = 1;

        req = new Request("/foo", "foo", null, bb);
        req.session = new Session(
            1, "kevin", permission
        );
        req.permissions = permission;

        Response test = new Permission.canEditBillboard().execute(req);
        assertEquals(Status.SUCCESS, test.status);

    }

    @Test
    public void UnauthorizedToEditBillboard() throws Exception {
        permission.canEditBillboard = false;

        Billboard bb = Billboard.Random(1);
        bb.locked = false;
        bb.id = 4;

        req = new Request("/foo", "foo", null, bb);
        req.session = new Session(
            10, "kevin", permission
        );
        req.permissions = permission;

        Response test = new Permission.canEditBillboard().execute(req);
        assertEquals(Status.UNAUTHORIZED, test.status);
    }

    @Test
    public void ViewPermission() throws Exception {
        permission.canEditUser = true;
        req.permissions = permission;

        Response test = new Permission.canViewPermission().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void ViewOwnPermission() throws Exception {
        permission.canEditUser = false;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "kevin");
        req = new Request("/foo", "foo", params, null);
        req.session = new Session(2, "kevin", permission);
        req.permissions = permission;
        Response test = new Permission.canViewPermission().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void ViewPermissionWithInvalidParams() throws Exception {
        permission.canEditUser = false;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "");
        req = new Request("/foo", "foo", params, null);
        req.session = new Session(2, "kevin", permission);
        req.permissions = permission;
        Response test = new Permission.canViewPermission().execute(req);
        assertEquals(Status.UNSUPPORTED_TYPE, test.status);
    }

    @Test
    public void ViewPermissionUnknownUser() throws Exception {
        permission.canEditUser = false;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "kevin1");
        req = new Request("/foo", "foo", params, null);
        req.session = new Session(2, "kevin", permission);
        req.permissions = permission;
        Response test = new Permission.canViewPermission().execute(req);
        assertEquals(Status.BAD_REQUEST, test.status);
    }

    @Test
    public void UnauthorziedToViewPermission() throws Exception {
        permission.canEditUser = false;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "kevin");
        req = new Request("/foo", "foo", params, null);
        req.session = new Session(1, "admin", permission);
        req.permissions = permission;
        Response test = new Permission.canViewPermission().execute(req);
        assertEquals(Status.UNAUTHORIZED, test.status);
    }

    @Test
    public void NoPermissionToDeleteUser() throws Exception {
        permission.canEditUser = false;
        req.permissions = permission;

        Response test = new Permission.canDeleteUser().execute(req);
        assertEquals(Status.UNAUTHORIZED, test.status);
    }

    @Test
    public void DeleteUser() throws Exception {
        permission.canEditUser = true;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "kevin");
        req = new Request("/foo", "foo", params, null);
        req.session = new Session(1, "admin", permission);
        req.permissions = permission;

        Response test = new Permission.canDeleteUser().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void InvalidBodyDeleteTest() throws Exception {
        permission.canEditUser = true;
        Billboard bb = Billboard.Random(1);
        req = new Request("/foo", "foo", null, bb);
        req.session = new Session(1, "admin", permission);
        req.permissions = permission;

        Response test = new Permission.canDeleteUser().execute(req);
        assertEquals(Status.UNSUPPORTED_TYPE, test.status);
    }

    @Test
    public void DeleteUnknownUser() throws Exception {
        permission.canEditUser = true;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "randomName");
        req = new Request("/foo", "foo", params, null);
        req.session = new Session(1, "admin", permission);
        req.permissions = permission;

        Response test = new Permission.canDeleteUser().execute(req);
        assertEquals(Status.BAD_REQUEST, test.status);
    }

    @Test
    public void DeleteYourself() throws Exception {
        permission.canEditUser = true;
        User user = new User("admin", "1234", null);
        user.id = 1;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "admin");
        req = new Request("/foo", "foo", params, user);
        req.session = new Session(1, "admin", permission);
        req.permissions = permission;

        Response test = new Permission.canDeleteUser().execute(req);

        assertEquals(Status.UNAUTHORIZED, test.status);
    }
}
