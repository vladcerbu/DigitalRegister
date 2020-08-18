package Domain;

import java.util.Objects;

/**
 * Homework Object extends an Entity
 * <description> String - Homework's description </description>
 * <startWeek> int - Homework's begin week </startWeek>
 * <deadlineWeek> int - Homework's deadline week </deadlineWeek>
 */
public class Homework extends Entity<Integer> {
    private String description;
    private Integer startWeek;
    private Integer deadlineWeek;

    public Homework(Integer id, String description, Integer startWeek, Integer deadlineWeek) {
        super.setId(id);
        this.description = description;
        this.startWeek = startWeek;
        this.deadlineWeek = deadlineWeek;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Integer getStartWeek() { return startWeek; }

    public void setStartWeek(Integer startWeek) { this.startWeek = startWeek; }

    public Integer getDeadlineWeek() { return deadlineWeek; }

    public void setDeadlineWeek(Integer deadlineWeek) { this.deadlineWeek = deadlineWeek; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Homework homework = (Homework) o;
        return super.getId().equals(homework.getId());
    }

    @Override
    public int hashCode() { return Objects.hash(super.hashCode(), description, startWeek, deadlineWeek); }

    @Override
    public String toString() { return super.toString() + description + ',' + startWeek + ',' + deadlineWeek; }
}
