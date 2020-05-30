package server.controllers;

import common.models.*;
import common.router.Response;
import common.router.Request;
import common.router.response.Status;
import common.utils.session.HashingFactory;
import common.utils.session.Session;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PermissionControllerTest {
    @Test
    public void GetAllPermissions() throws Exception {
        // Create new request.
        Request req = new Request(null, "blah", null, null);

        // Get all the permissions from database
        Response result = new PermissionController.Get().execute(req);
        assertEquals(result.status, Status.SUCCESS);
    }

    @Test
    public void GetPermissionsByName() throws Exception {
        // Generate the user data to insert
        User testUser = new User("kevin", HashingFactory.hashPassword("1234"), null);
        Permissions testPerm = Permissions.Random(testUser.id, testUser.username);
        UserPermissions temp = new UserPermissions(testUser, testPerm);

        // Create new request and insert the user data
        Request req = new Request(null, "blah", null, temp);
        new UserPermissionsController.Insert().execute(req);

        // Create params to store username
        HashMap<String, String> params = new HashMap<>();
        String permUser = "kevin";
        params.put("username", permUser);

        // Create new request.
        req = new Request(null, "blah", params, null);

        // Get all the permissions from database
        Response result = new PermissionController.GetByUsername().execute(req);
        assertEquals(result.status, Status.SUCCESS);
    }

    @Test
    public void GetPermissionsOfUnknownUser() throws Exception {
        // Create params to store username
        HashMap<String, String> params = new HashMap<>();
        String permUser = "aRandomName";
        params.put("username", permUser);

        // Create new request.
        Request req = new Request(null, "blah", params, null);

        // Get all the permissions from database
        Response result = new PermissionController.GetByUsername().execute(req);
        assertEquals(result.status, Status.BAD_REQUEST);
    }

    @Test
    public void GetPermissionsWithEmptyParameters() throws Exception {
        // Create params to store username
        HashMap<String, String> params = new HashMap<>();

        // Create new request.
        Request req = new Request(null, "blah", params, null);

        // Get all the permissions from database
        Response result = new PermissionController.GetByUsername().execute(req);
        assertEquals(result.status, Status.BAD_REQUEST);
    }

    @Test
    public void UpdatePermissions() throws Exception {
        // Generate the user data to insert
        User testUser = new User("testUpdatePerm", HashingFactory.hashPassword("1234"), null);
        Permissions testPerm = Permissions.Random(testUser.id, testUser.username);
        // Set permission Schedule Billboard to false
        testPerm.canScheduleBillboard = false;
        UserPermissions temp = new UserPermissions(testUser, testPerm);

        // Create new request and insert the user data
        Request req = new Request(null, "blah", null, temp);
        new UserPermissionsController.Insert().execute(req);

        // Create params
        HashMap<String, String> params = new HashMap<>();
        String permUser = "testUpdatePerm";
        params.put("username", permUser);

        // Create new request.
        req = new Request(null, "blah",params, null);

        // Get all the permissions from database
        Response result = new PermissionController.GetByUsername().execute(req);
        Permissions perm = ((List<Permissions>) result.body).get(0);
        perm.canScheduleBillboard = !perm.canScheduleBillboard;

        req = new Request(null, "blah", null, perm);

        req.session = new Session(
            0, "kevin", perm
        );

        result = new PermissionController.Update().execute(req);
        assertEquals(result.status, Status.SUCCESS);
    }

    @Test
    public void RemoveOwnEditUser() throws  Exception
    {
        User testUser = new User("admin", HashingFactory.hashPassword("1234"), null);
        Permissions testPerm = new Permissions(0,testUser.username,true,true,true,true );
        UserPermissions temp = new UserPermissions(testUser, testPerm);

        Request newreq = new Request(null,"blah",null,temp);
        Response insertUser = new UserPermissionsController.Insert().execute(newreq);


        HashMap<String, String> params = new HashMap<>();
        String permUser = "admin";
        params.put("username", permUser);

        // Create new request.
        Request req = new Request(null, "blah",params, null);

        // Get all the permissions from database
        Response result = new PermissionController.GetByUsername().execute(req);
        Permissions perm = ((List<Permissions>) result.body).get(0);
        perm.canEditUser = false;

        req = new Request(null, "blah", null, perm);

        req.session = new Session(
            0, "admin", perm
        );

        result = new PermissionController.Update().execute(req);
        assertEquals(result.status, Status.BAD_REQUEST);
    }
}
