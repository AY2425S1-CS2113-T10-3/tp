package storage;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to be used to indicate that a particular class attribute should not be serialized by <code>Gson</code>
 */
@Retention(RetentionPolicy.RUNTIME) // makes the annotation available at runtime
public @interface ExcludeInSerializationAnnotation {
}
