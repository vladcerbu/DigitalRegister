package Validation;

import Exceptions.ValidationException;

/**
 * Validator Object - Responsible with validation of Entities
 * @param <E> - Entity's type
 */
public interface Validator<E> {
    void validate(E entity) throws ValidationException;
}
