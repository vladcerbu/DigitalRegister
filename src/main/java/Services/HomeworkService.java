package Services;

import Domain.Homework;
import Exceptions.InvalidDateException;
import Persistance.HomeworkRepository;
import Utilities.Observers.Observable;
import Utilities.Observers.Observer;

import java.util.ArrayList;
import java.util.Collection;

public class HomeworkService implements Observable {
    private HomeworkRepository hwRepo;

    public HomeworkService(HomeworkRepository hwRepo) { this.hwRepo = hwRepo; }

    public Homework find(int id) { return hwRepo.find(id); }

    public Collection<Homework> getAll() {
        return hwRepo.getAll();
    }

    public Homework save(Homework hw) {
        Homework hwSaved = hwRepo.save(hw);
        if(hwSaved == null)
            notifyObservers();
        return hwSaved;
    }

    public Homework delete(int id) {
        Homework hwDeleted = hwRepo.delete(id);
        if(hwDeleted != null)
            notifyObservers();
        return hwDeleted;
    }

    public Homework update(Homework hw) {
        Homework realHw = find(hw.getId());
        if(realHw == null)
            return hw;
        if(realHw.getDeadlineWeek() < hw.getStartWeek())
            throw new InvalidDateException("\nTema nu poate fi modificata deoarece deadline-ul a trecut! ");
        hw.setStartWeek(realHw.getStartWeek());
        Homework hwUpdated = hwRepo.update(hw);
        notifyObservers();
        return hwUpdated;
    }

    public ArrayList<Homework> findByWeek(int deadlineWeek) {
        ArrayList<Homework> rez = new ArrayList<>();
        for(Homework hw : getAll()) {
            if(hw.getDeadlineWeek().equals(deadlineWeek))
                rez.add(hw);
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
