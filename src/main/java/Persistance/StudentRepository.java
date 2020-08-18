package Persistance;

import Domain.Student;
import Factories.ValidatorFactory;
import Utilities.EntityType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StudentRepository extends AbstractRepository<Integer, Student> {

    public StudentRepository(String fileName) { super(ValidatorFactory.getInstance().createValidator(EntityType.STUDENT), fileName, Student.class); }

    @Override
    protected Student deserialize(Element element) {
        String id = element.getAttribute("id");
        String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
        String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
        String group = element.getElementsByTagName("group").item(0).getTextContent();
        String email = element.getElementsByTagName("email").item(0).getTextContent();
        String labProf = element.getElementsByTagName("labProf").item(0).getTextContent();
        return new Student(Integer.parseInt(id),lastName,firstName,Integer.parseInt(group),email,labProf);
    }

    @Override
    protected Element serialize(Document document, Student student) {
        Element studentElement = document.createElement("Student");
        studentElement.setAttribute("id", student.getId().toString());

        Element lastName = document.createElement("lastName");
        lastName.setTextContent(student.getLastName());
        studentElement.appendChild(lastName);

        Element firstName = document.createElement("firstName");
        firstName.setTextContent(student.getFirstName());
        studentElement.appendChild(firstName);

        Element group = document.createElement("group");
        group.setTextContent(student.getGroup().toString());
        studentElement.appendChild(group);

        Element email = document.createElement("email");
        email.setTextContent(student.getEmail());
        studentElement.appendChild(email);

        Element labProf = document.createElement("labProf");
        labProf.setTextContent(student.getLabProf());
        studentElement.appendChild(labProf);

        return studentElement;
    }

    /*
    @Override
    protected Student deserialize(String l) {
        String[] data = l.split(",");
        return new Student(Integer.parseInt(data[0]),data[1],data[2],Integer.parseInt(data[3]),data[4],data[5]);
    }

    @Override
    protected String serialize(Student st) { return st.toString(); }
     */
}
