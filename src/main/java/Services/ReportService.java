package Services;

import Domain.Grade;
import Domain.Homework;
import Domain.HelpfulDTO;
import Domain.Student;
import Utilities.Calendar.UniversityYear;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService {
    private UniversityYear universityYear = UniversityYear.getInstance();
    private StudentService studentService;
    private HomeworkService homeworkService;
    private GradeService gradeService;

    public ReportService(StudentService studentService, HomeworkService homeworkService, GradeService gradeService) {
        this.studentService = studentService;
        this.homeworkService = homeworkService;
        this.gradeService   = gradeService;
    }

    public ArrayList<HelpfulDTO> mediiStudenti() {
        ArrayList<HelpfulDTO> rez = new ArrayList<>();
        for (Student st : studentService.getAll()) {
            rez.add(new HelpfulDTO(st.getId(),st.getFullName(),st.getGroup(),0));
        }
        for(HelpfulDTO n : rez) {
            int totalWeeks = 0;
            for(Homework hw : homeworkService.getAll()) {
                int pondere = hw.getDeadlineWeek() - hw.getStartWeek();
                totalWeeks += pondere;
                Grade gr = gradeService.find(n.getIdSt() + "_" + hw.getId());
                if(gr == null)
                    n.setMedie(n.getMedie() + pondere);
                else
                    n.setMedie(n.getMedie() + pondere * gr.getValue());
            }
            double medieFinala = n.getMedie()/totalWeeks;
            DecimalFormat df = new DecimalFormat("#.####");
            medieFinala = Double.parseDouble(df.format(medieFinala));
            n.setMedie(medieFinala);
        }
        return rez;
    }

    public List<HelpfulDTO> mediiStudenti5() {
        return mediiStudenti()
                .stream()
                .filter(helpfulDTO -> helpfulDTO.getMedie() >= 5)
                .collect(Collectors.toList());
    }

    public ArrayList<String> predateBine() {
        Collection<Student> students = studentService.getAll();
        ArrayList<String> rez = new ArrayList<>();
        boolean gasit;
        for(Student st : students) {
            rez.add(st.getId().toString() + " " + st.getFullName());
            gasit = false;
            for(Grade gr : gradeService.getAll()) {
                if (st.getId() == gr.getStudentId()) {
                    gasit = true;
                    Homework hw = homeworkService.find(gr.getHomeworkId());
                    if (hw != null)
                        if (universityYear.getCurrentSemester(gr.getDate()).getCurrentWeek(gr.getDate()) > hw.getDeadlineWeek())
                                rez.remove(st.getId().toString() + " " + st.getFullName());
                }
            }
            if(!gasit) rez.remove(st.getId().toString() + " " + st.getFullName());
        }
        return rez;
    }

    public HelpfulDTO notaMica() {
        ArrayList<HelpfulDTO> note = new ArrayList<>();
        for (Homework hw : homeworkService.getAll()) {
            if(hw.getDeadlineWeek() <= universityYear.getCurrentSemester(LocalDate.now()).getCurrentWeek(LocalDate.now()))
                note.add(new HelpfulDTO(hw.getStartWeek(), 0));
        }
        for(HelpfulDTO n : note) {
            int nrLab = n.getLabNr();
            int predate = 0;
            for (Grade gr : gradeService.getAll()) {
                Homework hw = homeworkService.find(gr.getHomeworkId());
                if (hw != null && hw.getStartWeek() == nrLab) {
                    n.setMedie(n.getMedie() + gr.getValue());
                    ++predate;
                }
            }
            if(predate < studentService.getAll().size())
                n.setMedie(n.getMedie() + studentService.getAll().size() - predate);
            double medieFinala = n.getMedie() / studentService.getAll().size();
            DecimalFormat df = new DecimalFormat("#.####");
            medieFinala = Double.parseDouble(df.format(medieFinala));
            n.setMedie(medieFinala);
        }
        HelpfulDTO rez = new HelpfulDTO(0,11);
        for(HelpfulDTO n : note)
            if(n.getMedie() < rez.getMedie()) {
                rez.setMedie(n.getMedie());
                rez.setLabNr(n.getLabNr());
            }
        return rez;
    }
}
