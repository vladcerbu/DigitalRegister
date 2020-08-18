import Domain.Student;
import Persistance.StudentRepository;
import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentRepositoryTest {
    private StudentRepository stRepo;
    private static String fileName = "E:\\FMI Sem.3\\MAP\\Projects\\ProiectMare\\src\\test\\resources\\TestStudents.xml";

    @BeforeEach
    void setUp() {
        this.stRepo = new StudentRepository(fileName);
        stRepo.save(new Student(0,"Cerbu","Vlad",221,"vladutzcerbu@yahoo.com","Vescan Andreea"));
    }

    @Order(1)
    @Test
    void findTrue() { assertNotNull(stRepo.find(0)); }

    @Order(2)
    @Test
    void findFalse() {
        assertNull(stRepo.find(1));
    }

    @Order(3)
    @Test
    void getAll() {
        Collection<Student> students = (Collection<Student>)stRepo.getAll();
        assertEquals(1, students.size());
    }

    @Order(4)
    @Test
    void saveFalse() { assertNotNull(stRepo.save(new Student(0,"Cerbu","Vlad",221,"vladutzcerbu@yahoo.com","Vescan Andreea"))); }

    @Order(5)
    @Test
    void saveTrue() { assertNull(stRepo.save(new Student(1, "Cerbu", "Vlad", 221, "vladutzcerbu@yahoo.com", "Vescan Andreea"))); }

    @Order(6)
    @Test
    void deleteTrue() { assertNotNull(stRepo.delete(1)); }

    @Order(7)
    @Test
    void deleteFalse() {
        assertNull(stRepo.delete(1));
    }

    @Order(8)
    @Test
    void updateTrue() { assertNull(stRepo.update(new Student(0, "Cerbu", "Vlad", 222, "vladutzcerbu@yahoo.com", "Vescan Andreea"))); }

    @Order(9)
    @Test
    void updateFalse() { assertNotNull(stRepo.update(new Student(1,"Cerbu","Vlad",222,"vladutzcerbu@yahoo.com","Vescan Andreea"))); }
}