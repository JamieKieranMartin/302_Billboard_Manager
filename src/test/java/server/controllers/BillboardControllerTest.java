package server.controllers;

import common.models.*;
import common.router.Response;
import common.router.Request;
import common.router.response.Status;
import common.utils.session.Session;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BillboardControllerTest {
    @Test
    public void NormalInsert() throws Exception {
        // Generate the billboard data to insert.
        Billboard bb = Billboard.Random(1);
        // Create new request.
        Request req = new Request(null, "blah", null, bb);

        // Generate a session
        req.session = new Session(
            1, "kevin", null
        );

        // Insert the billboard to the database
        Response test = new BillboardController.Insert().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void InsertExisted() throws Exception {
        // Generate the billboard data to insert
        Billboard bb = Billboard.Random(1);
        bb.name = "Existed";

        // Create new request.
        Request req = new Request(null, "blah", null, bb);
        // Generate a session
        req.session = new Session(
            1, "kevin", null
        );
        // Insert the billboard to the database
       new BillboardController.Insert().execute(req);

        // Generate another billboard data with existed name
        bb = Billboard.Random(2);
        bb.name = "Existed";

        // Create new request.
        req = new Request(null, "blah", null, bb);

        // Generate a session
        req.session = new Session(
            1, "kevin", null
        );
        // Insert the billboard to the database
        Response test = new BillboardController.Insert().execute(req);
        assertEquals(Status.BAD_REQUEST, test.status);
    }

    @Test
    public void InsertBillboardWithNoName() throws Exception {
        // Generate the billboard data to insert.
        Billboard bb = Billboard.Random(1);
        bb.name = "";
        // Create new request.
        Request req = new Request(null, "blah", null, bb);

        // Generate a session
        req.session = new Session(
            1, "kevin", null
        );

        // Insert the billboard to the database
        Response test = new BillboardController.Insert().execute(req);
        assertEquals(Status.BAD_REQUEST, test.status);
        assertEquals(test.message,"Billboard name must not be empty.");
    }

    @Test
    public void DeleteNotScheduled() throws Exception {
        // Generate the billboard data to insert
        Billboard bb = Billboard.Random(1);
        bb.name = "Deleted";
        bb.locked = false;
        // Create new request.
        Request req = new Request(null, "blah", null, bb);
        // Generate a session
        req.session = new Session(
            1, "kevin", null
        );
        // Insert the billboard to the database
        new BillboardController.Insert().execute(req);

        // Make a params with billboard name
        HashMap<String, String> params = new HashMap<>();
        params.put("bName", "Deleted");
        // Create new request.
        req = new Request(null, "blah", params, null);

        // Delete the billboard from the database
        Response test = new BillboardController.Delete().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void DeleteScheduled() throws Exception {
        // Generate the billboard data to insert
        Billboard bb = Billboard.Random(1);
        bb.name = "DeletedScheduled";
        bb.locked = true;

        Schedule schedule = Schedule.Random(bb.name);
        // Create new request.
        Request req = new Request(null, "blah", null, bb);
        // Generate a session
        req.session = new Session(
            1, "kevin", null
        );
        // Insert the billboard to the database
        new BillboardController.Insert().execute(req);

        // Create new request and insert schedule
        req = new Request(null, "blah", null, schedule);
        new ScheduleController.Insert().execute(req);

        // Make a params with billboard name
        HashMap<String, String> params = new HashMap<>();
        params.put("bName", "DeletedScheduled");
        // Create new request.
        req = new Request(null, "blah", params, null);

        // Delete the billboard from the database
        Response test = new BillboardController.Delete().execute(req);
        assertEquals(Status.SUCCESS, test.status);
    }

    @Test
    public void DeleteNonExisted() throws Exception {
        // Make a params with billboard name
        HashMap<String, String> params = new HashMap<>();
        params.put("bName", "RandomName");
        // Create new request.
        Request req = new Request(null, "blah", params, null);
        // Generate a session
        req.session = new Session(
            1, "kevin", null
        );
        // Delete the billboard from the database
        Response test = new BillboardController.Delete().execute(req);
        assertEquals(Status.BAD_REQUEST, test.status);
    }

    @Test
    public void GetAll() throws Exception {
        // Create new request.
        Request req = new Request(null, "blah", null, null);
        // Get all the billboard from database
        Response result = new BillboardController.Get().execute(req);
        assertEquals(Status.SUCCESS, result.status);
    }

    @Test
    public void GetByName() throws Exception {
        // Insert a billboard with the name "BillboardTest"
        Billboard bb = Billboard.Random(1);
        bb.name = "BillboardTest";
        // Create new request.
        Request req = new Request(null, "blah", null, bb);

        // Generate a session
        req.session = new Session(
            1, "kevin", null
        );
        // Insert the billboard to the database
        Response test = new BillboardController.Insert().execute(req);

        HashMap<String, String> params = new HashMap<>();
        String billboardName = "BillboardTest";
        params.put("name", billboardName);
        // Create new request.
        req = new Request(null, "blah", params, null);

        // Get all the billboard from database
        Response result = new BillboardController.GetByName().execute(req);
        assertEquals(Status.SUCCESS, result.status);
    }


    @Test
    public void UpdateBillboard() throws Exception {
        // Insert a billboard with the name "BillboardTest"
        Billboard bb = Billboard.Random(1);
        bb.name = "TestUpdate";
        // Create new request.
        Request req = new Request(null, "blah", null, bb);

        // Generate a session
        req.session = new Session(
            1, "kevin", null
        );
        // Insert the billboard to the database
        Response test = new BillboardController.Insert().execute(req);

        // Create new request.
        req = new Request(null, "blah", null, null);
        // Get all the billboard from database
        Response result = new BillboardController.Get().execute(req);
        List<Billboard> bbList = (List<Billboard>) result.body;

        Billboard temp = null;
        for (Billboard i : bbList) {
            if (i.name.equals("TestUpdate")) {
                temp = i;
                break;
            }
        }
        temp.message = "Updated message";
        // Create new request.
        req = new Request(null, "blah", null, temp);
        // Get all the billboard from database
        result = new BillboardController.Update().execute(req);
        assertEquals(Status.SUCCESS, result.status);
    }
}

