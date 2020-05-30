package common.sql;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This interface that allows the schema builder to automatically generate a schema based on fields.
 *
 * @author Perdana Bailey
 * @author Jamie Martin
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLITE {
    /**
     * Ensure the data types for the SQL.
     *
     * @return A string that is the datatype.
     */
    String type() default "";
}
