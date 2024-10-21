package storage;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Exclusion strategy that excludes class attributes with the {@code @ExcludeInSerialization} annotation
 */
class ExcludeInSerializationAnnotationExclusionStrategy implements ExclusionStrategy {
    public boolean shouldSkipClass(Class<?> clazz) {
        return clazz.getAnnotation(ExcludeInSerializationAnnotation.class) != null;
    }

    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        return fieldAttributes.getAnnotation(ExcludeInSerializationAnnotation.class) != null;
    }
}
