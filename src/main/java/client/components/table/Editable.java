package client.components.table;

/**
 * Classes that implement this interface use it to determine if the entire object is editable.
 *
 * @author Jamie Martin
 */
public interface Editable {

    /**
     * Returns if the field is editable.
     *
     * @return A boolean that determines whether the displayed field is editable.
     */
    boolean isEditable();
}
