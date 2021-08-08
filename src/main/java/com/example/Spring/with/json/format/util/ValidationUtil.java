package com.example.Spring.with.json.format.util;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidationUtil {
     <E> Set<ConstraintViolation<E>> violations(E entity);
}
