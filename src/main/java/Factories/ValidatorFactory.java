package Factories;

import Utilities.EntityType;
import Validation.GradeValidator;
import Validation.HomeworkValidator;
import Validation.StudentValidator;
import Validation.Validator;

/**
 * ValidatorFactory - Singleton
 */
public class ValidatorFactory {
    private static ValidatorFactory instance = new ValidatorFactory();
    private ValidatorFactory(){}
    public static ValidatorFactory getInstance() { return instance; }

    /**
     * Responsible with the creation of Validators of the specified entityType
     * @param entityType - The entityType of the Validator
     * @return a new Validator for the type specified
     */
    @SuppressWarnings("rawtypes")
    public Validator createValidator(EntityType entityType) {
        switch(entityType) {
            case STUDENT: return new StudentValidator();
            case HOMEWORK: return new HomeworkValidator();
            case GRADE: return new GradeValidator();
            //case PROFESSOR: return null;
            default: return null;
        }
    }
}
