package common.sql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;

/**
 * A class to generate SQL statements given a class type.
 *
 * @author Jamie Martin
 * @author Perdana Bailey
 */
public class StatementBuilder {

    /**
     * Creates a SELECT SQL statement based on given class type.
     *
     * @param conn The JDBC connection information.
     * @param className The provided class type.
     * @return PreparedStatement The SELECT SQL statement.
     * @throws Exception A pass-through internal server exception.
     */
    public static PreparedStatement get(Connection conn, Class<?> className) throws Exception {
        return conn.prepareStatement(createGetStatement(className));
    }

    /**
     * Creates a SELECT SQL statement string based on given class type.
     *
     * @param className The provided class type.
     * @return String The SELECT SQL statement string.
     */
    public static String createGetStatement(Class<?> className) {
        return "SELECT * FROM " + className.getSimpleName().toUpperCase();
    }


    /**
     * Creates the INSERT SQL Statement based on a given object type.
     *
     * @param conn The JDBC connection information.
     * @param object The object being inserted.
     * @return PreparedStatement The INSERT SQL statement.
     * @throws Exception A pass-through internal server exception.
     */
    public static PreparedStatement insert(Connection conn, Object object) throws Exception {
        // Get the objects class and its fields then create the statement base.
        Class className = object.getClass();
        var fields = getFields(className);
        PreparedStatement pstmt = conn.prepareStatement(createInsertStatement(className));

        // Match the objects fields with the statements base.
        int i = 1;
        for (Object obj : fields) {
            var field = (Field) obj;
            if (!field.getName().equals("id")) {
                Object value = field.get(object);
                pstmt.setObject(i, value);

                i++;
            }
        }
        return pstmt;
    }

    /**
     * Creates an INSERT SQL statement string based on given class type.
     *
     * @param className The provided class type.
     * @return String The SELECT SQL statement string.
     */
    public static String createInsertStatement(Class<?> className) {
        // Get the class fields and its length.
        var fields = getFields(className);
        int lastField = fields.length - 1;

        // Create the statement base.
        var names = new StringBuilder("INSERT INTO " + className.getSimpleName().toUpperCase() + " (");
        var values = new StringBuilder(" VALUES (");

        // Loop through the fields and create the statement.
        int i = 1;
        for (Object obj : fields) {
            var field = (Field) obj;
            if (!field.getName().equals("id")) {
                field.setAccessible(true);
                if (i == lastField) {
                    names.append(field.getName()).append(")");
                    values.append("?)");
                } else {
                    names.append(field.getName()).append(", ");
                    values.append("?, ");
                }
                i++;
            }
        }

        return names.toString() + values.toString();
    }

    /**
     * Creates the UPDATE SQL Statement based on a given object type.
     *
     * @param conn The JDBC connection information.
     * @param object The object being updated.
     * @return PreparedStatement The UPDATE SQL statement.
     * @throws Exception A pass-through internal server exception.
     */
    public static PreparedStatement update(Connection conn, Object object) throws Exception {
        // Get the objects class and its fields then create the statement base.
        Class className = object.getClass();
        var fields = getFields(className);
        String statement = createUpdateStatement(className);

        PreparedStatement pstmt = conn.prepareStatement(statement);

        // Save ID for later.
        int id = className.getDeclaredField("id").getInt(object);

        // Match the objects fields with the statements base.
        int i = 1;
        for (Object obj : fields) {
            var field = (Field) obj;
            if (!field.getName().equals("id")) {
                Object value = field.get(object);
                pstmt.setObject(i, value);

                i++;
            }
        }

        // Set ID.
        pstmt.setObject(i, id);

        return pstmt;
    }

    /**
     * Creates an UPDATE SQL statement string based on given class type.
     *
     * @param className The provided class type.
     * @return String The SELECT SQL statement string.
     */
    public static String createUpdateStatement(Class<?> className) {
        // Get the class fields and its length.
        var fields = getFields(className);
        int lastField = fields.length - 1;

        // Create the statement base.
        StringBuilder names = new StringBuilder("UPDATE " + className.getSimpleName().toUpperCase() + " SET ");

        // Loop through the fields and create the statement.
        int i = 1;
        for (Object obj : fields) {
            var field = (Field) obj;

            if (!field.getName().equals("id")) {
                field.setAccessible(true);

                if (i == lastField)
                    names.append(field.getName()).append(" = ?");
                else
                    names.append(field.getName()).append(" = ?, ");

                i++;
            }
        }

        return names.append(" WHERE ID = ?").toString();
    }

    /**
     * Creates a DELETE SQL statement based on given object.
     *
     * @param conn The JDBC connection information.
     * @param object The provided object.
     * @return PreparedStatement The DELETE SQL statement.
     * @throws Exception A pass-through internal server exception.
     */
    public static PreparedStatement delete(Connection conn, Object object) throws Exception {
        // Check class, get ID field, get base statement, match ID to base statement ID.
        Class className = object.getClass();
        int id = className.getDeclaredField("id").getInt(object);
        PreparedStatement pstmt = conn.prepareStatement(createDeleteStatement(className));
        pstmt.setObject(1, id);

        return pstmt;
    }

    /**
     * Creates a DELETE SQL statement string based on given class type.
     *
     * @param className The provided class type.
     * @return String The SELECT SQL statement string.
     */
    public static String createDeleteStatement(Class<?> className) {
        return "DELETE FROM " + className.getSimpleName().toUpperCase() + " WHERE ID = ?";
    }

    /* HELPER FUNCTIONS */

    /**
     *  Gets the fields of the class and returns and array of fields.
     *
     * @param className The class name with the requested files.
     * @return A list of objects which are the fields.
     */
    private static Object[] getFields(Class<?> className) {
        return Arrays.asList(className.getDeclaredFields()).stream().filter(f -> hasSQLAnnotation(f) && !f.getName().equals("serialVersionUID")).toArray();
    }

    /**
     *  Ensures the field has the annotation to be parsed.
     *
     * @param field The field that needs to sure it is annotated.
     * @return A boolean that determines the annotation status.
     */
    public static boolean hasSQLAnnotation(Field field) {
        return field.getAnnotationsByType(SQLITE.class).length > 0;
    }
}
