package client.components.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This interface that allows the GUI to display objects using
 * class information automatically using the help of annotations.
 *
 * @author Jamie Martin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DisplayAs {
    /**
     * The value of the field to display as a string.
     *
     * @return The string of the value.
     */
    String value();

    /**
     * The index location of the field.
     *
     * @return An integer that represents the index location of the field.
     */
    int index();

    /**
     * Returns if the field is editable.
     *
     * @return A boolean that determines whether the displayed field is editable.
     */
    boolean editable() default false;
}
