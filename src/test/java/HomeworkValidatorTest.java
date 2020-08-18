import Domain.Homework;
import Exceptions.ValidationException;
import Validation.HomeworkValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeworkValidatorTest {
    HomeworkValidator validator = new HomeworkValidator();

    @Test
    void validate0() {
        validator.validate(new Homework(0,"Ce",1,2));
        assert(true);
    }

    @Test
    void validate1() {
        try {
            validator.validate(new Homework(1,"",1,2)); //descriere = null
            assert(false);
        }
        catch (ValidationException e) { assertEquals("\nDescrierea nu poate fi nula! ",e.getErr()); }
    }

    @Test
    void validate2() {
        try {
            validator.validate(new Homework(2,"Ce",2,2)); //startWeek >= deadlineWeek
            assert(false);
        }
        catch (ValidationException e) { assertEquals("\nSaptamana de deadline trebuie sa fie dupa saptamana de start a temei!",e.getErr()); }
    }

    @Test
    void validate3() {
        try {
            validator.validate(new Homework(2,"Ce",3,2)); //startWeek >= deadlineWeek
            assert(false);
        }
        catch (ValidationException e) { assertEquals("\nSaptamana de deadline trebuie sa fie dupa saptamana de start a temei!",e.getErr()); }
    }

    @Test
    void validate4() {
        try {
            validator.validate(new Homework(3,"Ce",1,15)); //deadlineWeek > 14
            assert(false);
        }
        catch (ValidationException e) { assertEquals("\nSaptamana de start si de deadline a temei trebuie sa fie cuprinsa intre 1 si 14! ",e.getErr()); }
    }
}