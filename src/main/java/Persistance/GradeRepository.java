package Persistance;

import Domain.Grade;
import Factories.ValidatorFactory;
import Utilities.EntityType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDate;

public class GradeRepository extends AbstractRepository<String, Grade> {

    public GradeRepository(String fileName) { super(ValidatorFactory.getInstance().createValidator(EntityType.GRADE), fileName, Grade.class); }

    @Override
    protected Grade deserialize(Element element) {
        String id = element.getAttribute("id");
        String date = element.getElementsByTagName("date").item(0).getTextContent();
        String prof = element.getElementsByTagName("prof").item(0).getTextContent();
        String value = element.getElementsByTagName("value").item(0).getTextContent();
        return new Grade(id,LocalDate.parse(date),prof,Double.parseDouble(value));
    }

    @Override
    protected Element serialize(Document document, Grade grade) {
        Element gradeElement = document.createElement("Grade");
        gradeElement.setAttribute("id", grade.getId());

        Element date = document.createElement("date");
        date.setTextContent(grade.getDate().toString());
        gradeElement.appendChild(date);

        Element prof = document.createElement("prof");
        prof.setTextContent(grade.getProf());
        gradeElement.appendChild(prof);

        Element value = document.createElement("value");
        value.setTextContent(((Double) grade.getValue()).toString());
        gradeElement.appendChild(value);

        return gradeElement;
    }

    /*
    @Override
    protected Grade deserialize(String l) {
        String[] data = l.split(",");
        return new Grade(data[0], LocalDate.parse(data[1]),data[2],Float.parseFloat(data[3]));
    }

    @Override
    protected String serialize(Grade gr) {
        return gr.toString();
    }
    */
}
