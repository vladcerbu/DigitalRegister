package Domain;

import java.util.Objects;

/**
 * Student Object extends an Entity
 * <lastName> String - Student's last name </lastName>
 * <firstName> String - Student's first name </firstName>
 * <group> int - Student's group </group>
 * <email> String - Student's email </email>
 * <labProf> String - Student's laboratory advisor </labProf>
 */
public class Student extends Entity<Integer> {
    private String lastName;
    private String firstName;
    private Integer group;
    private String email;
    private String labProf;

    public Student(Integer id, String lastName, String firstName, Integer group, String email, String labProf) {
        super.setId(id);
        this.lastName = lastName;
        this.firstName = firstName;
        this.group = group;
        this.email = email;
        this.labProf = labProf;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getFullName() { return lastName + " " + firstName; }

    public Integer getGroup() { return group; }

    public void setGroup(Integer group) { this.group = group; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getLabProf() { return labProf; }

    public void setLabProf(String labProf) { this.labProf = labProf; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return super.getId().equals(student.getId());
    }

    @Override
    public int hashCode() { return Objects.hash(super.hashCode(),lastName,firstName,group,email); }

    @Override
    public String toString() { return super.toString() + lastName + ',' + firstName + ',' + group + ',' + email + ',' + labProf; }
}
