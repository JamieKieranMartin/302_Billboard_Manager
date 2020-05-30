package client.services;

/**
 * This class adds a specific type for table updating in java.
 *
 * @author Jamie Martin
 */
public abstract class DataService<T> {

    /**
     * Class function for updating a class in the data service.
     *
     * @param typeClass The class type for the data service.
     * @return A boolean that represents whether the class object was updated.
     */
   public abstract Boolean update(T typeClass);
}
