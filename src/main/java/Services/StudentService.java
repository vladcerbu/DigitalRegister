package Services;

import Domain.Student;
import Persistance.StudentRepository;
import Utilities.Observers.Observable;
import Utilities.Observers.Observer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class StudentService implements Observable {
    private StudentRepository stRepo;
    private String gradeBookPath;

    public StudentService(StudentRepository stRepo, String gradeBookPath) { this.stRepo = stRepo; this.gradeBookPath = gradeBookPath; }

    public Student find(int id) { return stRepo.find(id); }

    public Collection<Student> getAll() { return stRepo.getAll(); }

    public Student save(Student st) {
        Student stSaved = stRepo.save(st);
        if(stSaved == null)
            notifyObservers();
        return stSaved;
    }

    public Student delete(int id) {
        Student stDeleted = stRepo.delete(id);
        if(stDeleted != null) {
            File file = new File(gradeBookPath + "\\" + stDeleted.getLastName() + stDeleted.getFirstName() + ".txt");
            //noinspection ResultOfMethodCallIgnored
            file.delete();
            notifyObservers();
        }
        return stDeleted;
    }

    public Student update(Student st) {
        Student stUpdated = stRepo.update(st);
        if(stUpdated == null)
            notifyObservers();
        return stUpdated;
    }

    public ArrayList<Student> findByName(String seq) {
        ArrayList<Student> rez = new ArrayList<>();
        for(Student st : getAll()) {
            if(st.getFullName().contains(seq))
                rez.add(st);
        }
        return rez;
    }

    private ArrayList<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer obs) { observers.add(obs); }

    @Override
    public void removeObserver(Observer obs) { observers.remove(obs); }

    @Override
    public void notifyObservers() { observers.forEach(Observer::update); }
}
