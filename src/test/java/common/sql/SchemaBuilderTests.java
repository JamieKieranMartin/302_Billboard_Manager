package common.sql;

import common.models.Billboard;
import common.models.Permissions;
import common.models.Schedule;
import common.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SchemaBuilderTests {
    // Once dynamic schema builder is built. Test the schema returns pstmt correctly

    @Test
    public void createBillboardTable() throws Exception {
            String result = SchemaBuilder.tableStringSQL(Billboard.class);
            assertEquals("CREATE TABLE IF NOT EXISTS " +
                    "BILLBOARD (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255) NOT NULL UNIQUE, " +
                    "message VARCHAR(255), messageColor VARCHAR(7) DEFAULT \"#000000\", picture BLOB, " +
                    "backgroundColor VARCHAR(7) DEFAULT \"#ffffff\", information VARCHAR(255), " +
                    "informationColor VARCHAR(7) DEFAULT \"#000000\", locked BOOLEAN, userId INTEGER NOT NULL, " +
                    "FOREIGN KEY(userId) REFERENCES User(id))"
                ,result);
    }

    @Test
    public void createUserTable() throws Exception {
        String result = SchemaBuilder.tableStringSQL(User.class);
        assertEquals("CREATE TABLE IF NOT EXISTS " +
            "USER (id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(255) NOT NULL UNIQUE, " +
            "password VARCHAR(255), salt VARCHAR(255))"
            ,result);
    }
    @Test
    public void createScheduleTable() throws Exception{
        String result = SchemaBuilder.tableStringSQL(Schedule.class);
        assertEquals("CREATE TABLE IF NOT EXISTS " +
                "SCHEDULE (id INTEGER PRIMARY KEY AUTOINCREMENT, billboardName VARCHAR(255) NOT NULL, " +
                "dayOfWeek INTEGER NOT NULL, start INTEGER NOT NULL, createTime DATETIME NOT NULL, " +
                "duration INTEGER NOT NULL, interval INTEGER NOT NULL, " +
                "FOREIGN KEY(billboardName) REFERENCES Billboard(name))"
            , result);
    }

    @Test
    public void createPermissionTable() throws Exception {
        String result = SchemaBuilder.tableStringSQL(Permissions.class);
        assertEquals("CREATE TABLE IF NOT EXISTS " +
                "PERMISSIONS (id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR(255) NOT NULL UNIQUE, " +
                "canCreateBillboard BOOLEAN, canEditBillboard BOOLEAN, canScheduleBillboard BOOLEAN, " +
                "canEditUser BOOLEAN, FOREIGN KEY(id) REFERENCES User(id)," +
                " FOREIGN KEY(username) REFERENCES User(username))"
        ,result);
    }
}
