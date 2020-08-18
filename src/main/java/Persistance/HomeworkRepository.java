package Persistance;

import Domain.Homework;
import Factories.ValidatorFactory;
import Utilities.EntityType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HomeworkRepository extends AbstractRepository<Integer, Homework> {

    public HomeworkRepository(String fileName) { super(ValidatorFactory.getInstance().createValidator(EntityType.HOMEWORK), fileName, Homework.class); }

    @Override
    protected Homework deserialize(Element element) {
        String id = element.getAttribute("id");
        String description = element.getElementsByTagName("description").item(0).getTextContent();
        String startWeek = element.getElementsByTagName("startWeek").item(0).getTextContent();
        String deadlineWeek = element.getElementsByTagName("deadlineWeek").item(0).getTextContent();
        return new Homework(Integer.parseInt(id),description,Integer.parseInt(startWeek),Integer.parseInt(deadlineWeek));
    }

    @Override
    protected Element serialize(Document document, Homework homework) {
        Element homeworkElement = document.createElement("Homework");
        homeworkElement.setAttribute("id", homework.getId().toString());

        Element description = document.createElement("description");
        description.setTextContent(homework.getDescription());
        homeworkElement.appendChild(description);

        Element startWeek = document.createElement("startWeek");
        startWeek.setTextContent(homework.getStartWeek().toString());
        homeworkElement.appendChild(startWeek);

        Element deadlineWeek = document.createElement("deadlineWeek");
        deadlineWeek.setTextContent(homework.getDeadlineWeek().toString());
        homeworkElement.appendChild(deadlineWeek);

        return homeworkElement;
    }

    /*
    @Override
    protected Homework deserialize(String l) {
        String[] data = l.split(",");
        return new Homework(Integer.parseInt(data[0]),data[1],Integer.parseInt(data[2]),Integer.parseInt(data[3]));
    }

    @Override
    protected String serialize(Homework hw) { return hw.toString(); }
     */
}
