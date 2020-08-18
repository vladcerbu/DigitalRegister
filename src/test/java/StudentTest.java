import Domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentTest {
    Student student;

    @BeforeEach
    void setUp() {
        student = new Student(1,"Cerbu","Vlad",221,"vladc@gmail.com","Maria");
    }

    @Test
    void getLastName() { assertEquals("Cerbu",student.getLastName()); }

    @Test
    void setLastName() {
        student.setLastName("Sfarghiu");
        assertEquals("Sfarghiu",student.getLastName());
    }

    @Test
    void getFirstName() { assertEquals("Vlad",student.getFirstName()); }

    @Test
    void setFirstName() {
        student.setFirstName("Ion");
        assertEquals("Ion",student.getFirstName());
    }

    @Test
    void getGroup() { assertEquals(221,student.getGroup()); }

    @Test
    void setGroup() {
        student.setGroup(222);
        assertEquals(222,student.getGroup());
    }

    @Test
    void getEmail() { assertEquals("vladc@gmail.com",student.getEmail()); }

    @Test
    void setEmail() {
        student.setEmail("altemail@gmail.com");
        assertEquals("altemail@gmail.com",student.getEmail());
    }

    @Test
    void getLabProf() { assertEquals("Maria",student.getLabProf()); }

    @Test
    void setLabProf() {
        student.setLabProf("Ioana");
        assertEquals("Ioana",student.getLabProf());
    }

    @Test
    void equals() {
        Student student2 = new Student(1,"Buciu","Stefan",222,"stefanel@yahoo.ro","Maria");
        assertEquals(student2,student);
    }
}