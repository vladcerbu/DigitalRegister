package Services;

import Domain.Grade;
import Persistance.GradeRepository;
import Utilities.Observers.Observable;
import Utilities.Observers.Observer;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class GradeService implements Observable {
    private GradeRepository grRepo;
    private String gradeBookPath;

    public GradeService(GradeRepository grRepo, String gradeBookPath) { this.grRepo = grRepo; this.gradeBookPath = gradeBookPath; }

    public Grade find(String id) { return grRepo.find(id); }

    public Collection<Grade> getAll() {
        return grRepo.getAll();
    }

    public Grade save(Grade gr, String lastName, String firstName, int startWeek, int deadlineWeek, int currentWeek, String feedback) {
        Grade grSaved = grRepo.save(gr);
        if(grSaved == null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(gradeBookPath + "\\" + lastName + firstName + ".txt",true))) {
                bw.write("Tema numarul: " + startWeek + '\n');
                bw.write("Nota: " + gr.getValue() + '\n');
                bw.write("Predata in saptamana: " + currentWeek + '\n');
                bw.write("Deadline: " + deadlineWeek + '\n');
                bw.write("Feedback: " + feedback + '\n' + '\n');
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("\nEroare la scriere!");
            }
            notifyObservers();
        }
        return grSaved;
    }

    public Grade delete(String id) {
        Grade grDeleted = grRepo.delete(id);
        if(grDeleted != null)
            notifyObservers();
        return grDeleted;
    }

    public Grade update(Grade gr, String lastName, String firstName, int startWeek, int deadlineWeek, int currentWeek, String feedback) {
        Grade grUpdated = grRepo.update(gr);
        if(grUpdated == null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(gradeBookPath + "\\" + lastName + firstName + ".txt",true))) {
                bw.write("Tema numarul: " + startWeek + " (Nota Modificata)" + '\n');
                bw.write("Nota: " + gr.getValue() + '\n');
                bw.write("Predata in saptamana: " + currentWeek + '\n');
                bw.write("Deadline: " + deadlineWeek + '\n');
                bw.write("Feedback: " + feedback + '\n' + '\n');
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println("\nEroare la scriere!");
            }
            notifyObservers();
        }
        return grUpdated;
    }

    public double calculateGrade(double gradeValue, int currentWeek, int deadlineWeek, int motivations) {
        if(motivations < 0) motivations = 0;
        int penalty = currentWeek - deadlineWeek - motivations;
        if(penalty < 0) penalty = 0;
        gradeValue = gradeValue - penalty;
        if(gradeValue < 1) gradeValue = 1;
        return gradeValue;
    }

    private ArrayList<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer obs) { observers.add(obs); }

    @Override
    public void removeObserver(Observer obs) { observers.remove(obs); }

    @Override
    public void notifyObservers() { observers.forEach(Observer::update); }
}
