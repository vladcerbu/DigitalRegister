package UI;

import AppConfig.ApplicationContext;
import Domain.Grade;
import Domain.Homework;
import Domain.Student;
import Exceptions.InvalidDateException;
import Exceptions.ValidationException;
import Persistance.GradeRepository;
import Persistance.HomeworkRepository;
import Persistance.StudentRepository;
import Services.FilterService;
import Services.GradeService;
import Services.HomeworkService;
import Services.StudentService;
import Utilities.Calendar.UniversityYear;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class Console {
    private StudentService stSrv;
    private HomeworkService hwSrv;
    private GradeService grSrv;
    private FilterService flSrv;
    private BufferedReader reader;
    private UniversityYear universityYear;

    public Console() {
        String fileN2 = ApplicationContext.getProperties().getProperty("data.domain.homeworks");
        HomeworkRepository hwRepo = new HomeworkRepository(fileN2);
        hwSrv = new HomeworkService(hwRepo);

        String fileN3 = ApplicationContext.getProperties().getProperty("data.domain.grades");
        String fileN4 = ApplicationContext.getProperties().getProperty("data.gradeBook");
        GradeRepository grRepo = new GradeRepository(fileN3);
        grSrv = new GradeService(grRepo,fileN4);

        String fileN1 = ApplicationContext.getProperties().getProperty("data.domain.students");
        StudentRepository stRepo = new StudentRepository(fileN1);
        stSrv = new StudentService(stRepo,fileN4);

        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.universityYear = UniversityYear.getInstance();
        this.flSrv = new FilterService(stSrv,hwSrv,grSrv);
    }

    public void run() {
        while(true) {
            showMenu();
            try {
                System.out.println("Selectati un numar: ");
                int choice = Integer.parseInt(reader.readLine());
                switch(choice){
                    case 0: return;
                    case 1: showStudents(); break;
                    case 2: addStudent(); break;
                    case 3: delStudent(); break;
                    case 4: searchStudent(); break;
                    case 5: modStudent(); break;
                    case 6: showHomeworks(); break;
                    case 7: addHomework(); break;
                    case 8: delHomework(); break;
                    case 9: searchHomework(); break;
                    case 10: modHomework(); break;
                    case 11: showGrades(); break;
                    case 12: addGrade(); break;
                    case 13: delGrade(); break;
                    case 14: searchGrade(); break;
                    case 15: modGrade(); break;
                    case 16: filterMenu(); break;
                    default: System.out.println("\nAceasta optiune nu exista! Selectati doar optiuni din lista!");
                }
            }
            catch(IOException e) {
                System.out.println(e.getMessage());
            }
            catch (NumberFormatException e){
                System.out.println("\nEroare: Trebuie sa introduceti un numar!");
            }
            try { Thread.sleep(3000); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    private void showMenu() {
        System.out.println("\n---APLICATIE GESTIUNE FACULTATE---\n");
        System.out.println("1. Afisati toti studentii");
        System.out.println("2. Adaugati un student");
        System.out.println("3. Stergeti un student");
        System.out.println("4. Cautati un student dupa ID");
        System.out.println("5. Modificati datele unui student");
        System.out.println("6. Afisati toate temele");
        System.out.println("7. Adaugati o tema");
        System.out.println("8. Stergeti o tema");
        System.out.println("9. Cautati o tema dupa ID");
        System.out.println("10. Modificati datele unei teme");
        System.out.println("11. Afisati toate notele");
        System.out.println("12. Dati o nota");
        System.out.println("13. Stergeti o nota");
        System.out.println("14. Cautati o nota dupa ID");
        System.out.println("15. Modificati o nota");
        System.out.println("16. Filtrari");
        System.out.println("0. EXIT\n");
    }

    private void filterMenu() {
        System.out.println("\nMENIU FILTRARI\n");
        System.out.println("1. Afisati toti studentii dintr-o grupa");
        System.out.println("2. Afisati toti studentii care au predat o anumita tema");
        System.out.println("3. Afisati toti studentii care au predat o anumita tema unui anumit profesor");
        System.out.println("4. Afisati toate notele de la o anumita tema, identificata dupa numarul saptamanii de inceput");
        System.out.println("0. Inapoi");
        try {
            System.out.println("Selectati un numar: ");
            int choice = Integer.parseInt(reader.readLine());
            switch(choice) {
                case 0: return;
                case 1: groupFilter(); break;
                case 2: homeworkFilter(); break;
                case 3: homeworkAndProfFilter(); break;
                case 4: gradeFilter(); break;
                default: System.out.println("\nAceasta optiune de filtrare nu exista!");
            }
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        catch (NumberFormatException e) {
            System.out.println("\nEroare: Trebuie sa introduceti un numar!");
        }
    }

    private void groupFilter() throws IOException, NumberFormatException {
        System.out.println("\nDati grupa: ");
        int group = Integer.parseInt(reader.readLine());
        List<Student> stList = flSrv.studentsByGroup(group);
        if(stList.size() == 0) {
            System.out.println("\nNu sunt studenti in grupa " + group);
            return;
        }
        System.out.println("\nLEGENDA: ID,Nume,Prenume,Grupa,Email,Profesor Laborator");
        for(Student st : stList)
            System.out.println(st);
    }

    private void homeworkFilter() throws IOException, NumberFormatException {
        showHomeworks();
        System.out.println("\nDati id-ul temei: ");
        int hwId = Integer.parseInt(reader.readLine());
        List<Student> stList = flSrv.studentsByHomework(hwId);
        if(stList.size() == 0) {
            System.out.println("\nNiciun student nu a predat tema cu id-ul " + hwId);
            return;
        }
        System.out.println("\nLEGENDA: ID,Nume,Prenume,Grupa,Email,Profesor Laborator");
        for(Student st : stList)
            System.out.println(st);
    }

    private void homeworkAndProfFilter() throws IOException, NumberFormatException {
        showHomeworks();
        System.out.println("\nDati id-ul temei: ");
        int hwId = Integer.parseInt(reader.readLine());
        System.out.println("\nDati numele profesorului: ");
        String name = reader.readLine();
        List<Student> stList = flSrv.studentsByHomeworkAndProfessor(hwId,name);
        if(stList.size() == 0) {
            System.out.println("\nNiciun student nu a predat tema cu id-ul " + hwId + " profesorului " + name);
            return;
        }
        System.out.println("\nLEGENDA: ID,Nume,Prenume,Grupa,Email,Profesor Laborator");
        for(Student st : stList)
            System.out.println(st);
    }

    private void gradeFilter() throws IOException, NumberFormatException {
        showHomeworks();
        System.out.println("\nDati saptamana de inceput a temei: ");
        int startWeek = Integer.parseInt(reader.readLine());
        List<Homework> hwList = flSrv.homeworksByWeek(startWeek);
        if(hwList.size() == 0) {
            System.out.println("\nNu exista teme cu saptamana de inceput " + startWeek);
        }
        else if(hwList.size() == 1) {
            int hwId = hwList.get(0).getId();
            List<Grade> grList = flSrv.gradesByHomework(hwId,startWeek);
            if(grList.size() == 0) {
                System.out.println("\nNu sunt note pentru tema din saptamana " + startWeek);
                return;
            }
            System.out.println("\nLEGENDA: ID,Descriere,Saptamana Start,Saptamana Deadline");
            for(Grade gr : grList)
                System.out.println(gr);
        }
        else {
            System.out.println("\nLEGENDA: ID,Descriere,Saptamana Start,Saptamana Deadline");
            for(Homework hw : hwList)
                System.out.println(hw);
            System.out.println("\nSunt mai multe teme care incep in saptamana " + startWeek + ". Dati id-ul temei dorite: ");
            int hwId = Integer.parseInt(reader.readLine());
            List<Grade> grList = flSrv.gradesByHomework(hwId,startWeek);
            if(grList.size() == 0) {
                System.out.println("\nNu sunt note pentru tema din saptamana " + startWeek);
                return;
            }
            System.out.println("\nLEGENDA: ID,Descriere,Saptamana Start,Saptamana Deadline");
            for(Grade gr : grList)
                System.out.println(gr);
        }
    }

    private void showStudents() {
        if(stSrv.getAll().size() == 0) {
            System.out.println("\nNu s-a introdus niciun student!");
            return;
        }
        System.out.println("\nLEGENDA: ID,Nume,Prenume,Grupa,Email,Profesor Laborator");
        for(Student st : stSrv.getAll())
            System.out.println(st);
    }

    private void addStudent() {
        try {
            System.out.println("\nDati id-ul studentului: ");
            Integer id = Integer.parseInt(reader.readLine());
            System.out.println("Dati numele de familie: ");
            String lastName = reader.readLine();
            System.out.println("Dati prenumele: ");
            String firstName = reader.readLine();
            System.out.println("Dati grupa: ");
            Integer group = Integer.parseInt(reader.readLine());
            System.out.println("Dati emailul: ");
            String email = reader.readLine();
            System.out.println("Dati numele profesorului de laborator: ");
            String prof = reader.readLine();
            Student stAdd = stSrv.save(new Student(id, lastName, firstName, group, email, prof));
            if (stAdd != null)
                System.out.println("\nNu s-a adaugat studentul! Id-ul deja exista!");
            else
                System.out.println("\nStudentul a fost adaugat cu succes!");
        }
        catch (NumberFormatException e) {
            System.out.println("\nEroare: Trebuie sa introduceti un numar! Revenire la meniul principal!");
        }
        catch (ValidationException e) {
            System.out.println(e.getErr());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void delStudent() {
        try {
            System.out.println("\nDati id-ul studentului: ");
            int id = Integer.parseInt(reader.readLine());
            Student stDel = stSrv.delete(id);
            if(stDel == null)
                System.out.println("\nNu exista student cu acest id!");
            else
                System.out.println("\nStudentul a fost sters cu succes!");
        }
        catch (NumberFormatException e) {
            System.out.println("\nEroare: Trebuie sa introduceti un numar! Revenire la meniul principal!");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void searchStudent() {
        try {
            System.out.println("\nDati id-ul studentului: ");
            int id = Integer.parseInt(reader.readLine());
            Student stFind = stSrv.find(id);
            if (stFind == null)
                System.out.println("\nNu exista student cu acest id!");
            else {
                System.out.println("\nLEGENDA: ID,Nume,Prenume,Grupa,Email,Profesor Laborator");
                System.out.println(stFind);
            }
        }
        catch (NumberFormatException e) {
            System.out.println("\nEroare: Trebuie sa introduceti un numar! Revenire la meniul principal!");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void modStudent() {
        try {
            System.out.println("\nDati id-ul studentului: ");
            Integer id = Integer.parseInt(reader.readLine());
            System.out.println("Dati noul nume de familie: ");
            String lastName = reader.readLine();
            System.out.println("Dati noul prenume: ");
            String firstName = reader.readLine();
            System.out.println("Dati noua grupa: ");
            Integer group = Integer.parseInt(reader.readLine());
            System.out.println("Dati noul email: ");
            String email = reader.readLine();
            System.out.println("Dati noul profesor de laborator: ");
            String prof = reader.readLine();
            Student stMod = stSrv.update(new Student(id, lastName, firstName, group, email, prof));
            if (stMod != null)
                System.out.println("\nNu s-a modificat studentul! Id-ul specificat nu exista!");
            else
                System.out.println("\nStudentul a fost modificat cu succes!");
        }
        catch (NumberFormatException e) {
            System.out.println("\nEroare: Trebuie sa introduceti un numar! Revenire la meniul principal!");
        }
        catch (ValidationException e) {
            System.out.println(e.getErr());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showHomeworks() {
        if(hwSrv.getAll().size() == 0) {
            System.out.println("\nNu s-a introdus nicio tema!");
            return;
        }
        System.out.println("\nLEGENDA: ID,Descriere,Saptamana Start,Saptamana Deadline");
        for(Homework hw : hwSrv.getAll())
            System.out.println(hw);
    }

    private void addHomework() {
        try {
            System.out.println("\nDati id-ul temei: ");
            Integer id = Integer.parseInt(reader.readLine());
            System.out.println("Dati descrierea temei: ");
            String desc = reader.readLine();
            int startWeek = universityYear.getCurrentSemester(LocalDate.now(ZoneId.of("Europe/Bucharest"))).getCurrentWeek(LocalDate.now(ZoneId.of("Europe/Bucharest")));
            System.out.println("Saptamana de start este cea cu numarul " + startWeek
                    + ". Saptamana de deadline trebuie sa fie cuprinsa intre " + startWeek + " si 14.");
            System.out.println("Dati saptamana de deadline: ");
            Integer deadlineWeek = Integer.parseInt(reader.readLine());
            Homework hwAdd = hwSrv.save(new Homework(id, desc, startWeek, deadlineWeek));
            if (hwAdd != null)
                System.out.println("\nNu s-a adaugat tema! Id-ul deja exista!");
            else
                System.out.println("\nTema a fost adaugata cu succes!");
        }
        catch (InvalidDateException e) {
            System.out.println(e.getErr());
        }
        catch (NumberFormatException e) {
            System.out.println("\nEroare: Trebuie sa introduceti un numar! Revenire la meniul principal!");
        }
        catch (ValidationException e) {
            System.out.println(e.getErr());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void delHomework() {
        try {
            System.out.println("\nDati id-ul temei: ");
            int id = Integer.parseInt(reader.readLine());
            Homework hwDel = hwSrv.delete(id);
            if(hwDel == null)
                System.out.println("\nNu exista tema cu acest id!");
            else
                System.out.println("\nTema a fost stearsa cu succes!");
        }
        catch (NumberFormatException e) {
            System.out.println("\nEroare: Trebuie sa introduceti un numar! Revenire la meniul principal!");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void searchHomework() {
        try {
            System.out.println("\nDati id-ul temei: ");
            int id = Integer.parseInt(reader.readLine());
            Homework hwFind = hwSrv.find(id);
            if (hwFind == null)
                System.out.println("\nNu exista tema cu acest id!");
            else {
                System.out.println("\nLEGENDA: ID,Descriere,Saptamana Start,Saptamana Deadline");
                System.out.println(hwFind);
            }
        }
        catch (NumberFormatException e) {
            System.out.println("\nEroare: Trebuie sa introduceti un numar! Revenire la meniul principal!");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void modHomework() {
        try {
            System.out.println("\nDati id-ul temei: ");
            Integer id = Integer.parseInt(reader.readLine());
            System.out.println("Dati noua descriere: ");
            String desc = reader.readLine();
            System.out.println("Dati noua saptamana de deadline: ");
            Integer deadlineWeek = Integer.parseInt(reader.readLine());
            Homework hwMod = hwSrv.update(new Homework(id, desc, universityYear.getCurrentSemester(LocalDate.now(ZoneId.of("Europe/Bucharest"))).getCurrentWeek(LocalDate.now(ZoneId.of("Europe/Bucharest"))), deadlineWeek));
            if (hwMod != null)
                System.out.println("\nId-ul acesta nu exista!");
            else
                System.out.println("\nTema a fost modificata cu succes!");
        }
        catch (InvalidDateException e){
            System.out.println(e.getErr());
        }
        catch (NumberFormatException e) {
            System.out.println("\nEroare: Trebuie sa introduceti un numar! Revenire la meniul principal!");
        }
        catch (ValidationException e) {
            System.out.println(e.getErr());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showGrades() {
        if(grSrv.getAll().size() == 0) {
            System.out.println("\nNu s-au dat note!");
            return;
        }
        System.out.println("\nLEGENDA: ID,Data de acordare a notei,Profesorul evaluator,Nota");
        for(Grade gr : grSrv.getAll())
            System.out.println(gr);
    }

    private void addGrade() {
        try {
            if(stSrv.getAll().size() == 0 || hwSrv.getAll().size() == 0) {
                System.out.println("\nNu sunt studenti sau teme inregistrate. Nu se pot acorda note!");
                return;
            }
            showStudents();
            showHomeworks();
            System.out.println("\nDati id-ul studentului: ");
            int idSt = Integer.parseInt(reader.readLine());
            Student st = stSrv.find(idSt);
            if(st == null) {
                System.out.println("\nNu exista student cu acest id!");
                return;
            }
            System.out.println("Dati id-ul temei: ");
            int idHw = Integer.parseInt(reader.readLine());
            Homework hw = hwSrv.find(idHw);
            if(hw == null) {
                System.out.println("\nNu exista tema cu acest id!");
                return;
            }
            int week = universityYear.getCurrentSemester(LocalDate.now(ZoneId.of("Europe/Bucharest"))).getCurrentWeek(LocalDate.now(ZoneId.of("Europe/Bucharest")));
            LocalDate date = LocalDate.now(ZoneId.of("Europe/Bucharest"));
            System.out.println("Dati numele profesorului evaluator: ");
            String prof = reader.readLine();
            int motivations = 0;
            if(week - hw.getDeadlineWeek() > 0) {
                System.out.println("A trecut deadline-ul temei. Dati numarul de saptamani pentru care studentul este motivat: ");
                motivations = Integer.parseInt(reader.readLine());
            }
            double maxGrade = grSrv.calculateGrade(10, week, hw.getDeadlineWeek(), motivations);
            if(maxGrade < 8) {
                System.out.println("\nStudentul are o penalizare mai mare de 2 saptamani, nu mai poate preda tema!");
                return;
            }
            System.out.println("Nota maxima pentru aceasta tema dupa calcularea intarzierilor este: " + maxGrade);
            System.out.println("Dati nota: ");
            double gradeValue = Double.parseDouble(reader.readLine());
            gradeValue = grSrv.calculateGrade(gradeValue, week, hw.getDeadlineWeek(), motivations);
            System.out.println("Nota dupa calcularea intarzierilor este: " + gradeValue);
            System.out.println("Dati feedback: ");
            String feedback = reader.readLine();
            Grade grAdd = grSrv.save(new Grade(idSt,idHw,date,prof,gradeValue), st.getLastName(), st.getFirstName(), hw.getStartWeek(), hw.getDeadlineWeek(), motivations, feedback);
            if (grAdd != null)
                System.out.println("\nNu s-a adaugat nota! Acest student deja a primit nota la aceasta tema!");
            else
                System.out.println("\nNota a fost adaugata cu succes!");
        }
        catch (InvalidDateException e) {
            System.out.println(e.getErr());
        }
        catch (NumberFormatException e) {
            System.out.println("\nEroare: Trebuie sa introduceti un numar! Revenire la meniul principal!");
        }
        catch (ValidationException e) {
            System.out.println(e.getErr());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void delGrade() {
        try {
            System.out.println("\nDati id-ul notei: ");
            String id = reader.readLine();
            Grade grDel = grSrv.delete(id);
            if(grDel == null)
                System.out.println("\nNu exista nota cu acest id!");
            else
                System.out.println("\nNota a fost stearsa cu succes!");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void searchGrade() {
        try {
            System.out.println("\nDati id-ul notei: ");
            String id = reader.readLine();
            Grade grFind = grSrv.find(id);
            if (grFind == null)
                System.out.println("\nNu exista nota cu acest id!");
            else {
                System.out.println("\nLEGENDA: ID,Data de acordare a notei,Profesorul evaluator,Nota");
                System.out.println(grFind);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void modGrade() {
        try {
            System.out.println("\nDati id-ul notei: ");
            String id = reader.readLine();
            Grade grFind = grSrv.find(id);
            if (grFind == null) {
                System.out.println("\nId-ul acesta nu exista!");
                return;
            }
            String[] ids = id.split("_");
            int idSt = Integer.parseInt(ids[0]);
            Student st = stSrv.find(idSt);
            if(st == null) {
                System.out.println("\nStudentul caruia i s-a dat nota nu mai exista!");
                return;
            }
            int idHw = Integer.parseInt(ids[1]);
            Homework hw = hwSrv.find(idHw);
            if(hw == null) {
                System.out.println("\nTema pentru care s-a dat nota nu mai exista!");
                return;
            }
            int week = universityYear.getCurrentSemester(grFind.getDate()).getCurrentWeek(grFind.getDate());
            System.out.println("Dati numele noului profesor evaluator: ");
            String prof = reader.readLine();
            int motivations = 0;
            if(week - hw.getDeadlineWeek() > 0) {
                System.out.println("Studentul a predat tema dupa deadline. Dati numarul de saptamani pentru care studentul este motivat: ");
                motivations = Integer.parseInt(reader.readLine());
            }
            double maxGrade;
            maxGrade = grSrv.calculateGrade(10, week, hw.getDeadlineWeek(), motivations);
            if (maxGrade >= 8) {
                System.out.println("Nota maxima pentru aceasta tema dupa calcularea intarzierilor este: " + maxGrade);
                System.out.println("Dati noua nota: ");
                double gradeValue = Double.parseDouble(reader.readLine());
                gradeValue = grSrv.calculateGrade(gradeValue, week, hw.getDeadlineWeek(), motivations);
                System.out.println("Nota dupa calcularea intarzierilor este: " + gradeValue);
                System.out.println("Dati feedback: ");
                String feedback = reader.readLine();
                Grade grMod = grSrv.update(new Grade(id, grFind.getDate(), prof, gradeValue), st.getLastName(), st.getFirstName(), hw.getStartWeek(), hw.getDeadlineWeek(), week, feedback);
                if (grMod != null)
                    System.out.println("\nNu s-a modificat nota! Acest id nu exista!");
                else
                    System.out.println("\nNota a fost modificata cu succes!");
            }
            else System.out.println("\nNu se poate modifica nota deoarece ar insemna ca studentul are o penalizare mai mare de 2 saptamani!");
        }
        catch (InvalidDateException e) {
            System.out.println(e.getErr());
        }
        catch (NumberFormatException e) {
            System.out.println("\nEroare: Trebuie sa introduceti un numar! Revenire la meniul principal!");
        }
        catch (ValidationException e) {
            System.out.println(e.getErr());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
