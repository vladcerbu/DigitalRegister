import Domain.Grade;
import Exceptions.ValidationException;
import Validation.GradeValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GradeValidatorTest {
    GradeValidator validator = new GradeValidator();

    @Test
    void validate0() {
        validator.validate(new Grade("1_1",LocalDate.now(),"Vescan Andreea",9.5));
        assert(true);
    }

    @Test
    void validate1() {
        try {
            validator.validate(new Grade("1_1",LocalDate.now(),"vescan Andreea",10)); //Nume profesor cu litera mica
            assert(false);
        }
        catch (ValidationException e) { assertEquals("\nNumele profesorului trebuie sa inceapa cu litera mare! ",e.getErr()); }
    }

    @Test
    void validate2() {
        try {
            validator.validate(new Grade("1_1",LocalDate.now(),"",10)); //Nume profesor null
            assert(false);
        }
        catch (ValidationException e) { assertEquals("\nNumele profesorului trebuie sa inceapa cu litera mare! ",e.getErr()); }
    }

    @Test
    void validate3() {
        try {
            validator.validate(new Grade("1_1",LocalDate.now(),"Vescan Andreea",11)); //Nota nu e intre 1 si 10
            assert(false);
        }
        catch (ValidationException e) { assertEquals("\nNota trebuie sa fie o valoare cuprinsa intre 1 si 10!",e.getErr()); }
    }

}