package Persistance;

import Domain.Entity;
import Validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @param <ID> id type of the entity
 * @param <E> entity type
 */
public abstract class AbstractRepository<ID, E extends Entity<ID>> implements CrudRepository<ID, E> {
    protected Validator<E> validator;
    protected Map<ID,E> entities;
    private String fileName;
    private Class entityClass;

    /**
     * Constructor
     * @param validator - the specific validator for the entity present in the repository
     */
    AbstractRepository(Validator<E> validator, String fileName, Class entityClass) {
        this.validator = validator;
        this.entities = new HashMap<>();
        this.fileName = fileName;
        this.entityClass = entityClass;
        loadFromFile();
    }

    /**
     * Finds the entity with the specified id
     * @param id - the id of the entity to be returned, id must not be null
     * @return the entity that has that id
     */
    @Override
    public E find(ID id) {
        if (id == null)
            throw new IllegalArgumentException("Id must not be null!");
        return entities.get(id);
    }

    /**
     * Gets all the entities
     * @return an interable with all the entities in the repository
     */
    @Override
    public Collection<E> getAll() {
        return entities.values();
    }

    /**
     * Saves the entity
     * @param entity - entity must be not null
     * @return entity if the id already exists in the repository or null if the entity was saved successfully
     */
    @Override
    public E save(E entity)  {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null! ");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) { return entity; }
        entities.put(entity.getId(),entity);
        writeToFile();
        return null;
    }

    /**
     * Deletes the entity with the specified id
     * @param id - id must be not null
     * @return the entity that was removed or null if it doesn't exist
     */
    @Override
    public E delete(ID id) {
        E rmEntity = entities.remove(id);
        writeToFile();
        return rmEntity;
    }

    /**
     * Updates the entity if found
     * @param entity - entity must not be null
     * @return null if the entity was updated successfully or entity if the id of the entity to update doesn't exist in the repository
     */
    @Override
    public E update(E entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null! ");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            entities.put(entity.getId(),entity);
            writeToFile();
            return null;
        }
        return entity;
    }

    /**
     * Loads all entities from a file into the Map
     */
    private void loadFromFile() {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.fileName);
            Element root = document.getDocumentElement();
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node element = children.item(i);
                if (element instanceof Element) {
                    E en = deserialize((Element) element);
                    entities.put(en.getId(), en);
                }
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all entities from the Map into a file
     */
    private void writeToFile() {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = document.createElement(this.entityClass.getSimpleName());
            document.appendChild(root);
            entities.values().forEach(g -> {
                Element newElem = serialize(document, g);
                root.appendChild(newElem);
            });

            //write Document to file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Source source = new DOMSource(document);
            transformer.transform(source, new StreamResult(fileName));
        }
        catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serializeaza entitatea pentru scriere in fisier
     * @param document - documentul in care se pune elementul
     * @param entity - entitatea de transformat
     * @return elementul creat
     */
    protected abstract Element serialize(Document document, E entity);

    /**
     * Deserializeaza un element intr-o entitate pentru a fi salvata in aplicatie
     * @param element - elementul de transformat
     * @return entitatea dorita
     */
    protected abstract E deserialize(Element element);
}
