package common.sql;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to generate Collections given a class type.
 *
 * @author Jamie Martin
 * @author Perdana Bailey
 */
public class CollectionFactory {
    // Class Name, Collection Key Value Pair
    public static Map<Class, Collection> collections = new HashMap<>();

    /**
     * Returns an instance of a Collection<T> given the class.
     * If there isn't a Collection, it creates one.
     *
     * @param className The Class you want to manage.
     * @param <T> T the type of the Class you want to manage.
     * @return Collection<T> The collection instance.
     * @throws Exception A pass-through internal server exception.
     */
    public static <T> Collection<T> getInstance(Class<T> className) throws Exception {

        // Try get the collection from the collection map using class name as the key.
        Object instance = collections.get(className);

        // If the collection is null create a new collection from the class and insert it into the map.
        if (instance == null){
            synchronized (className) {
                instance = new Collection(className);
                collections.put(className, (Collection) instance);
            }
        }

        // Return the collection instance.
        return (Collection<T>) instance;
    }
}
