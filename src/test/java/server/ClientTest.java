package server;

import common.models.*;
import common.router.response.Status;
import common.utils.ClientSocketFactory;
import common.utils.session.HashingFactory;
import common.utils.session.Session;

import java.util.*;

public class ClientTest {

    private String host;
    private int port;

    public static <Response> void main(String[] args) throws Exception {
        String token = null;

        {
            Scanner sc = new Scanner(System.in);

            System.out.println("Username: ");
            String username = sc.nextLine();

            System.out.println("Password: ");
            String password = sc.nextLine();

            HashMap<String, String> test = new HashMap<>();
            test.put("username", username);
            test.put("password", HashingFactory.hashPassword(password));

            System.out.println("Username from test: " + test.get("username"));
            common.router.Response result = new ClientSocketFactory("/login", null, test, null).Connect();

            if (result.status == Status.SUCCESS) {
                System.out.println("Successfully logged in!");
                System.out.println("Your token is: " + ((Session) result.body).token);
                token = ((Session) result.body).token;
            } else {
                System.exit(0);
            }
        }


        // test insert billboard -- Worked
//        {
//            HashMap<String, String> params = null;
//            Billboard bb = Billboard.Random(1);
//            bb.name = "Billboard2";
//            new ClientSocketFactory("/billboard/insert", token, params, bb).Connect();
//        }
        // Test get billboard by name -- Worked
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = new HashMap<>();
//            String billboardName = "BillboardTest";
//            params.put("name", billboardName);
//            IActionResult result = new ClientSocketFactory("/billboard/get/name",token,params,null).Connect();
//            if (result != null && result.body != null) {
//                List<Billboard> bbList = (List<Billboard>)result.body;
//                Billboard resultBB = bbList.get(0);
//                System.out.println(resultBB.name);
//            }
//            System.out.println("Get by bName");
//
//        }

//         Test get all billboard -- Worked
//        {
//            HashMap<String, String> params = null;
//            IActionResult result = new ClientSocketFactory("/billboard/get", token, params, null).Connect();
//            if (result != null && result.body != null) {
//                List<Billboard> billboards = (List<Billboard>) result.body;
//
//                for (Billboard billboard : billboards) {
//                    System.out.println(billboard.name);
//                }
//
//            }
//        }
        // Test update billboard -- Worked
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = null;
//            IActionResult result = new ClientSocketFactory("/billboard/get", token, params, null).Connect();
//            Billboard updated = null;
//            if (result != null && result.body != null) {
//                List<Billboard> billboards = (List<Billboard>) result.body;
//
//                for (Billboard billboard : billboards) {
//                    if (billboard.userId == 1) {
//                        updated = billboard;
//                        break;
//                    }
//                }
//                updated.name = "Something else1";
//                updated.message = "Hello 12345";
//                new ClientSocketFactory("/billboard/update", token, params, updated).Connect();
//
//                System.out.println("test fuck you Audi \n");
//            }
//        }

        // Test delete billboard -- Worked
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = new HashMap<>();
//            params.put("bName", "Existed");
//
//            IActionResult result = new ClientSocketFactory("/billboard/delete", token, params, null).Connect();
//            System.out.println("Deleted billboard");
//        }


        // Test insert user -- Worked
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = null;
//            User testUser = new User("kevin2", HashingFactory.hashPassword("1234"), null);
//            Permissions testPerm = Permissions.Random(testUser.id, testUser.username);
//            UserPermissions temp = new UserPermissions(testUser, testPerm);
//
//            new ClientSocketFactory("/userpermissions/insert", token, params, temp).Connect();
//
//            System.out.println("Inserted user");
//        }
        // Test update password -- Worked
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = new HashMap<>();
//
//            String permUser = "kevin1";
//            String newPassword = HashingFactory.hashPassword("123");
//            params.put("username", permUser);
//            params.put("password", newPassword);
//
//            new ClientSocketFactory("/user/update/password", token, params, null).Connect();
//            System.out.println("Password changed");
//        }
        // Test delete user -- Worked
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = new HashMap<>();
//            params.put("username","audi");
//
//            IActionResult result = new ClientSocketFactory("/user/delete", token, params, null).Connect();
//            System.out.println("Deleted");
//        }

        // Test list users -- Worked
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = null;
//            IActionResult result = new ClientSocketFactory("/permission/get", token, params, null).Connect();
//            if (result != null && result.body != null) {
//                List<Permissions> userList = (List<Permissions>) result.body;
//
//                for (Permissions perm : userList) {
//                    System.out.print(perm.username + " has permission of: ");
//                    if (perm.canEditBillboard) {
//                        System.out.print(" canEditBillboard, ");
//                    }
//                    if (perm.canScheduleBillboard) {
//                        System.out.print(" canScheduleBillboard, ");
//                    }
//                    if (perm.canViewBillboard) {
//                        System.out.print(" canViewBillboard, ");
//                    }
//                    if (perm.canCreateBillboard) {
//                        System.out.print(" canCreateBillboard, ");
//                    }
//                    if (perm.canEditUser) {
//                        System.out.print(" canEditUser.");
//                    }
//                    System.out.println("");
//                }
//            }
//        }

        // Test get permission by username -- Worked
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = new HashMap<>();
//
//            String permUser = "kevin1";
//            params.put("username", permUser);
//            IActionResult result = new ClientSocketFactory("/permission/get/username", token, params, null).Connect();
//
//            if (result != null && result.body != null) {
//                List<Permissions> userList = (List<Permissions>) result.body;
//                Permissions perm = userList.get(0);
//                System.out.print(perm.username + " has permission of: ");
//                if (perm.canEditBillboard) {
//                    System.out.print(" canEditBillboard, ");
//                }
//                if (perm.canScheduleBillboard) {
//                    System.out.print(" canScheduleBillboard, ");
//                }
//                if (perm.canViewBillboard) {
//                    System.out.print(" canViewBillboard, ");
//                }
//                if (perm.canCreateBillboard) {
//                    System.out.print(" canCreateBillboard, ");
//                }
//                if (perm.canEditUser) {
//                    System.out.println(" canEditUser.");
//                }
//            }
//            System.out.println("Get perm by username");
//        }
        // Test update permission
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = new HashMap<>();
//            IActionResult result = new ClientSocketFactory("/permission/get", token, params, null).Connect();
//            Permissions updatePerm = null;
//            if (result != null && result.body != null) {
//                List<Permissions> permList = (List<Permissions>) result.body;
//                for (Permissions perm: permList)
//                {
//                    if (perm.username.equals("kevin1"))
//                    {
//                        updatePerm = perm;
//                        break;
//                    }
//                }
//            }
//            updatePerm.canEditUser = false;
//            new ClientSocketFactory("/permission/update", token, params, updatePerm).Connect();
//            System.out.println("Updated perm");
//        }
        // Test insert schedule
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = new HashMap<>();
//            Schedule temp = Schedule.Random("Something1");
//            new ClientSocketFactory("/schedule/insert", token, params, temp).Connect();
//            System.out.println("Inserted schedule");
//        }


        // Test get all schedule -- Woprked
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = new HashMap<>();
//            IActionResult result = new ClientSocketFactory("/schedule/get", token, params, null).Connect();
//            if (result != null && result.body != null) {
//                List<Schedule> scheduleList = (List<Schedule>) result.body;
//                for (Schedule schedule: scheduleList) {
//                    System.out.println(schedule.billboardName + " starts at " + schedule.startTime);
//                }
//            }
//        }

        // Test get current schedule -- Worked??
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = new HashMap<>();
//            IActionResult result = new ClientSocketFactory("/schedule/get/current", token, params, null).Connect();
//            if (result != null && result.body != null) {
//                Billboard resultBB = (Billboard) result.body;
//                System.out.println(resultBB.name + " with the message: " + resultBB.message);
//            }
//        }
        // Test delete schedule -- Worked
//        {
//            Scanner sc = new Scanner(System.in);
//            sc.nextLine();
//            HashMap<String, String> params = new HashMap<>();
//            Schedule deleted = null;
//            IActionResult result = new ClientSocketFactory("/schedule/get", token, params, null).Connect();
//            if (result != null && result.body != null) {
//                List<Schedule> scheduleList = (List<Schedule>) result.body;
//                for (Schedule schedule : scheduleList) {
//                    if (schedule.billboardName.equals("Something1")) {
//                        deleted = schedule;
//                        break;
//                    }
//                }
//            }
//            result = new ClientSocketFactory("/schedule/delete", token, params, deleted).Connect();
//            System.out.println("Deleted schedule");
//        }

    }

}

