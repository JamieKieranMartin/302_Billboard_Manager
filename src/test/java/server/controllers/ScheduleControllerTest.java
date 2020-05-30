package server.controllers;

import common.models.*;
import common.router.Response;
import common.router.Request;
import common.router.response.Status;
import common.utils.RandomFactory;
import common.utils.session.Session;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScheduleControllerTest {

    @Test
    public void InsertSchedule() throws Exception
    {
        // Generate the billboard data to insert
        Billboard bb = Billboard.Random(1);
        bb.locked = true;

        Schedule schedule = Schedule.Random(bb.name);
        while (schedule.duration < 1) schedule.duration = RandomFactory.Int(15);
        while (schedule.interval < schedule.duration) schedule.interval = RandomFactory.Int(60);

        // Create new request.
        Request req = new Request(null, "blah", null, bb);
        // Generate a session
        req.session = new Session(
            1, "kevin", null
        );
        // Insert the billboard to the database
        Response test = new BillboardController.Insert().execute(req);

        // Create new request and insert schedule
        req = new Request(null, "blah", null, schedule);
        Response result = new ScheduleController.Insert().execute(req);

        assertEquals(Status.SUCCESS, result.status);
    }

    @Test
    public void DeleteSchedule() throws  Exception
    {
        // Generate the billboard data to insert
        Billboard bb = Billboard.Random(1);
        bb.name = "SBD";
        bb.locked = true;

        // Generate schedule data to insert
        Schedule schedule = Schedule.Random(bb.name);
        while (schedule.duration < 1) schedule.duration = RandomFactory.Int(15);
        while (schedule.interval < schedule.duration) schedule.interval = RandomFactory.Int(60);

        // Create new request to insert billboard
        Request req = new Request(null, "blah", null, bb);
        // Generate a session
        req.session = new Session(1, "kevin", null);
        // Insert the billboard to the database
         new BillboardController.Insert().execute(req);

        // Create new request and insert schedule
        req = new Request(null, "blah", null, schedule);
        new ScheduleController.Insert().execute(req);

        //Create new request and get all schedules
        req = new  Request(null,"blah",null,null);
        Response test = new ScheduleController.Get().execute(req);
        List<Schedule> scheduleList = (List<Schedule>) test.body;

        // Get the deleted schedule data
        Schedule deleted = null;
        for (Schedule sche: scheduleList)
        {
            if (sche.billboardName.equals("SBD"))
            {
                deleted = sche;
                break;
            }
        }

        req = new Request(null,"blah",null, deleted);
        Response result = new ScheduleController.Delete().execute(req);
        assertEquals(Status.SUCCESS,result.status);
    }

    @Test
    public void DeleteNonExistedSchedule() throws  Exception
    {
        // Generate a random schedule
        Schedule deleted = Schedule.Random("aRandomName");

        // Create new request and try to delete the schedule
        Request req = new Request(null,"blah",null,deleted);
        Response result = new ScheduleController.Delete().execute(req);

        assertEquals(Status.BAD_REQUEST,result.status);
    }

    @Test
    public void GetAllSchedule() throws  Exception
    {
        // Create new request and try to get all schedule
        Request req = new Request(null,"blah",null,null);
        Response result = new ScheduleController.Get().execute(req);

        assertEquals(Status.SUCCESS,result.status);
    }

    @Test
    public void GetCurrentSchedule() throws  Exception
    {
        Request req = new Request(null,"blah",null,null);
        Response result = new ScheduleController.GetCurrent().execute(req);

        assertEquals(Status.SUCCESS,result.status);
    }

}
