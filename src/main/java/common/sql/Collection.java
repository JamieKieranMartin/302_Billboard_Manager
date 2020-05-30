package common.sql;

import server.services.DataService;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * A Collection class to manage the CRUD updates of a given class.
 *
 * @author Jamie Martin
 * @author Perdana Bailey
 */
public class Collection<T> {
    private final Class<T> className;

    /**
     * Constructor that sets the collection type to the specified class.
     *
     * @param className Specified class to set the collection type to.
     */
    public Collection(Class<T> className) throws Exception {
        this.className = className;

        SchemaBuilder.build(DataService.getConnection(), className);
    }

    /**
     * Get all items from the DataService.
     *
     * @param predicate Used to filter out specific items. Example: x -> true, for all.
     * @return A list of the retrieved collection object type from the database.
     * @throws Exception A pass-through internal server exception.
     */
    public List<T> get(Predicate<T> predicate) throws Exception {
        // Prepares the statement using the StatementBuilder
        PreparedStatement psmt = StatementBuilder.get(DataService.getConnection(), className);

        // Collection object list to be returned.
        List<T> arr = new ArrayList<>();

        // Fetch the collection objects from the database.
        ResultSet result = psmt.executeQuery();

        // Use the result of the database query to create collection object and add it to the returned list.
        while (result.next()) {
            // Convert from SQL result to the collection object.
            T res = fromSQL(result);

            // TODO: Change this to SQL based predicate checks
            // Ensure the result matches the predicate before adding to the list.
            if (predicate.test(res)) {
                arr.add(res);
            }
        }

        // Clean up query.
        psmt.close();

        // Return array list.
        return arr;
    }

    /**
     * Inserts a specified collection object into the DataService.
     *
     * @param object The collection object you want inserted.
     * @throws Exception A pass-through internal server exception.
     */
    public void insert(T object) throws Exception {
        // Prepares the statement using the StatementBuilder
        PreparedStatement psmt = StatementBuilder.insert(DataService.getConnection(), object);

        // Insert the new collection object in the database.
        psmt.executeUpdate();

        // Clean up query.
        psmt.close();
    }

    /**
     * Updates a specified collection object into the DataService
     *
     * @param object The collection object you want updated.
     * @throws Exception A pass-through internal server exception.
     */
    public void update(T object) throws Exception {
        // Prepares the statement using the StatementBuilder
        PreparedStatement psmt = StatementBuilder.update(DataService.getConnection(), object);

        // Update the existing collection object in the database.
        psmt.executeUpdate();

        // Clean up query.
        psmt.close();
    }

    /**
     * Deletes a specified collection object from the DataService.
     *
     * @param object The collection object you want deleted.
     * @throws Exception A pass-through internal server exception.
     */
    public void delete(T object) throws Exception {
        // Prepares the statement using the StatementBuilder
        PreparedStatement psmt = StatementBuilder.delete(DataService.getConnection(), object);

        // Delete the collection object in the database.
        psmt.executeUpdate();

        // Clean up query.
        psmt.close();
    }

    /**
     * Parses the SQL result set and returns the collection object.
     *
     * @param resultSet The result set from an SQL query.
     * @return T The collection object after converting from SQL.
     * @throws SQLException A pass-through internal server exception.
     */
    public T fromSQL(ResultSet resultSet) throws Exception {
        // Create a list of fields from the class reference through reflection.
        List<Field> fields = Arrays.asList(this.className.getDeclaredFields());

        // Use a generic constructor on the class reference to create the collection object.
        T collectionObject = this.className.getConstructor().newInstance();

        // Loops through the available fields from the class reference.
        for (Field field : fields) {
            // Sets the field accessibility.
            field.setAccessible(true);

            // Gets the name of the field.
            String name = field.getName();

            if (StatementBuilder.hasSQLAnnotation(field)) {
                // Fetches the value using name of the field as the key from the result set.
                Object value = resultSet.getObject(name);

                if (field.get(collectionObject) instanceof Boolean) {
                    // Set the field of the collection object with the value fetched.
                    field.set(collectionObject, (int) value == 1);
                } else if (field.get(collectionObject) instanceof Instant) {
                    field.set(collectionObject, Instant.parse((String) value));
                } else {
                    // Set the field of the collection object with the value fetched.
                    field.set(collectionObject, value);
                }
            }
        }

        // Return the collection object
        return collectionObject;
    }
}
