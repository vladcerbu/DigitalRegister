import Domain.Homework;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeworkTest {
    Homework homework;

    @BeforeEach
    void setUp() {
        homework = new Homework(1,"ceva",1,2);
    }

    @Test
    void getDescription() { assertEquals("ceva", homework.getDescription()); }

    @Test
    void setDescription() {
        homework.setDescription("altceva");
        assertEquals("altceva",homework.getDescription());
    }

    @Test
    void getStartWeek() { assertEquals(1,homework.getStartWeek()); }

    @Test
    void setStartWeek() {
        homework.setStartWeek(2);
        assertEquals(2,homework.getStartWeek());
    }

    @Test
    void getDeadlineWeek() { assertEquals(2, homework.getDeadlineWeek()); }

    @Test
    void setDeadlineWeek() {
        homework.setDeadlineWeek(4);
        assertEquals(4,homework.getDeadlineWeek());
    }

    @Test
    void testEquals() {
        Homework homework2 = new Homework(1,"cv",2,5);
        assertEquals(homework2,homework);
    }
}