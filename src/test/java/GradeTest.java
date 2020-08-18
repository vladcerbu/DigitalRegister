import Domain.Grade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GradeTest {
    Grade grade;

    @BeforeEach
    void setUp() { grade = new Grade("1_2", LocalDate.parse("01/01/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy")), "Vescan Andreea", 9.75); }

    @Test
    void getDate() { assertEquals(LocalDate.parse("01/01/2019", DateTimeFormatter.ofPattern("dd/MM/yyyy")),grade.getDate()); }

    @Test
    void setDate() {
        grade.setDate(LocalDate.now());
        assertEquals(LocalDate.now(),grade.getDate());
    }

    @Test
    void getProf() { assertEquals("Vescan Andreea",grade.getProf()); }

    @Test
    void setProf() {
        grade.setProf("Eu");
        assertEquals("Eu",grade.getProf());
    }

    @Test
    void getValue() { assertEquals(9.75,grade.getValue()); }

    @Test
    void setValue() {
        grade.setValue(10.5);
        assertEquals(10.5,grade.getValue());
    }

    @Test
    void getStudentId() { assertEquals(1,grade.getStudentId()); }

    @Test
    void getHomeworkId() { assertEquals(2,grade.getHomeworkId()); }

    @Test
    void testEquals() {
        Grade grade2 = new Grade("1_2",LocalDate.now(),"Eu",1);
        assertEquals(grade2,grade);
    }
}