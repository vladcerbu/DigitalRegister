import Domain.Student;
import Exceptions.ValidationException;
import Validation.StudentValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentValidatorTest {
    StudentValidator validator = new StudentValidator();

    @Test
    void validate0() {
        validator.validate(new Student(0,"Cerbu","Vlad",221,"vladutzcerbu@yahoo.com","Vescan Andreea"));
        assert(true);
    }

    @Test
    void validate1() {
        try {
            validator.validate(new Student(1,"cerbu","Vlad",221,"vladutzcerbu@yahoo.com","Vescan Andreea")); //Numele cu litera mica
            assert(false);
        }
        catch (ValidationException e) { assertEquals("\nNumele trebuie sa inceapa cu litera mare! ",e.getErr()); }
    }

    @Test
    void validate2() {
        try {
            validator.validate(new Student(1,"Cerbu","vlad",221,"vladutzcerbu@yahoo.com","Vescan Andreea")); //Prenumele cu litera mica
            assert(false);
        }
        catch (ValidationException e) { assertEquals("\nPrenumele trebuie sa inceapa cu litera mare! ",e.getErr()); }
    }

    @Test
    void validate3() {
        try {
            validator.validate(new Student(1,"Cerbu","Vlad",1000,"vladutzcerbu@yahoo.com","Vescan Andreea")); //Grupa nu e intre 100 si 999
            assert(false);
        }
        catch (ValidationException e) { assertEquals("\nGrupa trebuie sa fie cuprinsa intre 100 si 999! ",e.getErr()); }
    }

    @Test
    void validate4() {
        try {
            validator.validate(new Student(1,"Cerbu","Vlad",221,"vladutzcerbu@yahoocom","Vescan Andreea")); //Email incorect
            assert(false);
        }
        catch (ValidationException e) { assertEquals("\nEmailul trebuie sa fie de tipul 'a...a@a...a.aaaa'. Se pot folosi litere, cifre, ., _ si -. ",e.getErr()); }
    }

    @Test
    void validate5() {
        try {
            validator.validate(new Student(1,"Cerbu","Vlad",221,"vladutzcerbu@yahoo.com","andreea")); //Numele si prenumele profesorului cu litera mica
            assert(false);
        }
        catch (ValidationException e) { assertEquals("\nNumele indrumatorului de laborator trebuie sa inceapa cu litera mare!",e.getErr()); }
    }
}