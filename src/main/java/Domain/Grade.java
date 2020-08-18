package Domain;

import java.time.LocalDate;
import java.util.Objects;

public class Grade extends Entity<String> {
    private LocalDate date;
    private String prof;
    private double value;

    public Grade(Integer stID, Integer hwID, LocalDate date, String prof, double value) {
        super.setId(stID.toString() + '_' + hwID.toString());
        this.date = date;
        this.prof = prof;
        this.value = value;
    }

    public Grade(String id, LocalDate date, String prof, double value) {
        super.setId(id);
        this.date = date;
        this.prof = prof;
        this.value = value;
    }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public String getProf() { return prof; }

    public void setProf(String prof) { this.prof = prof; }

    public double getValue() { return value; }

    public void setValue(double value) { this.value = value; }

    public int getStudentId() {
        String[] ids = super.getId().split("_");
        return Integer.parseInt(ids[0]);
    }

    public int getHomeworkId() {
        String[] ids = super.getId().split("_");
        return Integer.parseInt(ids[1]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return super.getId().equals(grade.getId());
    }

    @Override
    public int hashCode() { return Objects.hash(super.hashCode(),date,prof,value); }

    @Override
    public String toString() { return super.toString() + date + ',' + prof + ',' + value; }
}
