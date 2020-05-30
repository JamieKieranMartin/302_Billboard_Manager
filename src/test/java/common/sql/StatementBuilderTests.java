package common.sql;

import common.models.Billboard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatementBuilderTests {
    // Test the statements are working correctly for different cases. Testing the prepared statement given back

    @Test
    public void GetStatement() throws Exception {
        String stmt = StatementBuilder.createGetStatement(Billboard.class);
        assertEquals("SELECT * FROM BILLBOARD", stmt);
    }

    @Test
    public void InsertStatement() throws Exception {
        String stmt = StatementBuilder.createInsertStatement(Billboard.class);
        assertEquals("INSERT INTO BILLBOARD (name, message, messageColor, picture, backgroundColor, information, informationColor, locked, userId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", stmt);
    }

    @Test
    public void UpdateStatement() throws Exception {
        String stmt = StatementBuilder.createUpdateStatement(Billboard.class);
        assertEquals("UPDATE BILLBOARD SET name = ?, message = ?, messageColor = ?, picture = ?, backgroundColor = ?, information = ?, informationColor = ?, locked = ?, userId = ? WHERE ID = ?", stmt);
    }

    @Test
    public void DeleteStatement() throws Exception {
        String stmt = StatementBuilder.createDeleteStatement(Billboard.class);
        assertEquals("DELETE FROM BILLBOARD WHERE ID = ?", stmt);
    }

}
