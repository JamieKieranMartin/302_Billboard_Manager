package common.sql;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * This class initialises the database and it's schema.
 *
 * @author Jamie Martin
 */
public class SchemaBuilder {

    public SchemaBuilder() {}

    /**
     * Entry point to build the schema.
     *
     * @param connection JDBC connection to insert the schema.
     * @param className The class to generate the schema from.
     * @throws Exception Pass through exception that gets handled up stream.
     */
    public static void build(Connection connection, Class<?> className) throws Exception {
        // Create statement.
        Statement sqlStatement = connection.createStatement();

        // Turn class fields into a string schema.
        String sql = tableStringSQL(className);

        // Sqlite doesnt allow foreign keys by default.
        sqlStatement.execute("PRAGMA foreign_keys = ON");

        // Push the schema.
        sqlStatement.executeUpdate(sql);
    }

    /**
     * This function uses the class fields with SQLite annotations to generate the schema as a string.
     *
     * @param classType The class to generate the schema from.
     * @return String Schema of class as a string.
     */
    public static String tableStringSQL(Class classType) {
        // Ensure the table is created.
        var query = new StringBuilder("CREATE TABLE IF NOT EXISTS " + classType.getSimpleName().toUpperCase() + " (");

        // Get field annotations for the class.
        List<Field> fields = Arrays.asList(classType.getFields());
        var classAnnotations = classType.getAnnotationsByType(SQLITE.class);
        var SQLAddition = (SQLITE) Arrays.stream(classAnnotations).findFirst().get();

        // Loop through each field and build the schema based on the field with the correct annotation.
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setAccessible(true);
            var field = fields.get(i);

            // Ensure the field annotation is SQLite.
            var annotations = field.getAnnotationsByType(SQLITE.class);
            var annotation = Arrays.stream(annotations).findFirst();

            // If it is an SQLite field, convert it to schema.
            if (annotation.isPresent()) {
                query.append(fields.get(i).getName() + " " + annotation.get().type());

                if (i != fields.size() - 1 ) {
                    query.append(", ");
                }
            }
        }

        // Ensure anything else gets added that may not be a field.
        if (!SQLAddition.type().equals("")) {
            query.append(", " + SQLAddition.type());
        }

        query.append(")");
        return query.toString();
    }
}
