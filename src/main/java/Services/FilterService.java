package Services;

import Domain.Grade;
import Domain.Homework;
import Domain.Student;

import java.util.List;
import java.util.stream.Collectors;

public class FilterService {
    private StudentService studentService;
    private HomeworkService homeworkService;
    private GradeService gradeService;

    public FilterService(StudentService studentService, HomeworkService homeworkService, GradeService gradeService) {
        this.studentService = studentService;
        this.homeworkService = homeworkService;
        this.gradeService   = gradeService;
    }

    /**
     * @param group - integer
     * @return all students that are part of that group
     */
    public List<Student> studentsByGroup(int group) {
        return studentService.getAll()
                .stream()
                .filter(student -> student.getGroup() == group)
                .collect(Collectors.toList());
    }

    /**
     * @param homeworkId - int
     * @return all students that are graded at that certain homework
     */
    public List<Student> studentsByHomework(int homeworkId) {
        return gradeService.getAll()
                .stream()
                .filter(grade -> grade.getHomeworkId() == homeworkId)
                .map(grade -> studentService.find(grade.getStudentId()))
                .collect(Collectors.toList());
    }

    /**
     * @param homeworkId - int
     * @param prof - String
     * @return all students that are graded at that certain homework by a certain teacher
     */
    public List<Student> studentsByHomeworkAndProfessor(int homeworkId, String prof) {
        return gradeService.getAll()
                .stream()
                .filter(grade -> grade.getHomeworkId() == homeworkId
                        && grade.getProf().equals(prof))
                .map(grade -> studentService.find(grade.getStudentId()))
                .collect(Collectors.toList());
    }

    /**
     * @param homeworkId - int
     * @param week       - int
     * @return all grades for a certain homework graded in a certain week
     */
    public List<Grade> gradesByHomework(int homeworkId, int week) {
        return gradeService.getAll()
                .stream()
                .filter(grade -> grade.getHomeworkId() == homeworkId
                        && homeworkService.find(homeworkId).getStartWeek() == week)
                .collect(Collectors.toList());
    }

    public List<Homework> homeworksByWeek(int startWeek) {
        return homeworkService.getAll()
                .stream()
                .filter(homework -> homework.getStartWeek() == startWeek)
                .collect(Collectors.toList());
    }
}
