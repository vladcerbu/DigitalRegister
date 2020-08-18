package Controllers;

import Application.MainApp;
import Domain.Grade;
import Domain.Student;
import Exceptions.ValidationException;
import Services.GradeService;
import Services.StudentService;
import Utilities.Observers.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import java.util.Collection;

public class StudentController implements Observer {
    private StudentService stSrv;
    private GradeService grSrv;
    private ObservableList<Student> studentObservableList = FXCollections.observableArrayList();

    @FXML
    TableView<Student> studentTableView;
    @FXML
    TableColumn<Student,Integer> idColumn;
    @FXML
    TableColumn<Student,String> lastNameColumn;
    @FXML
    TableColumn<Student,String> firstNameColumn;
    @FXML
    TableColumn<Student,Integer> groupColumn;
    @FXML
    TableColumn<Student,String> emailColumn;
    @FXML
    TableColumn<Student,String> profColumn;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField groupTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField profTextField;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        profColumn.setCellValueFactory(new PropertyValueFactory<>("labProf"));
        studentTableView.setItems(studentObservableList);
    }

    public void init(StudentService stSrv, GradeService grSrv) {
        this.stSrv = stSrv;
        this.grSrv = grSrv;
        this.stSrv.addObserver(this);
        this.grSrv.addObserver(this);
        loadItems();
    }

    @FXML
    private void loadItems() {
        Collection<Student> students = stSrv.getAll();
        studentObservableList.setAll(students);
    }

    @Override
    public void update() {
        loadItems();
    }

    private void clearTextFields() {
        idTextField.clear();
        lastNameTextField.clear();
        firstNameTextField.clear();
        groupTextField.clear();
        emailTextField.clear();
        profTextField.clear();
    }

    public void handleSaveStudent() {
        try {
            String strId = idTextField.getText();
            if(strId.equals("")) {
                Alert message = new Alert(Alert.AlertType.ERROR,"Nu ati introdus niciun ID!");
                message.setTitle("Eroare!");
                message.showAndWait();
                return;
            }
            int id = Integer.parseInt(strId);
            String lastName = lastNameTextField.getText();
            String firstName = firstNameTextField.getText();
            String strGr = groupTextField.getText();
            int group = Integer.parseInt(strGr);
            String email = emailTextField.getText();
            String prof = profTextField.getText();
            Student savedSt = stSrv.save(new Student(id,lastName,firstName,group,email,prof));
            if (savedSt == null) {
                Alert message = new Alert(Alert.AlertType.INFORMATION,"Studentul a fost adaugat cu succes!");
                message.setTitle("Adaugare reusita");
                message.showAndWait();
                clearTextFields();
            }
            else {
                Alert message = new Alert(Alert.AlertType.ERROR, "Acest ID deja exista!");
                message.setTitle("Eroare ID deja existent!");
                message.showAndWait();
            }
        }
        catch (ValidationException e) {
            Alert message = new Alert(Alert.AlertType.ERROR,e.getErr());
            message.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            message.setTitle("Eroare de Validare!");
            message.showAndWait();
        }
        catch (NumberFormatException e) {
            Alert message = new Alert(Alert.AlertType.ERROR,e.getMessage() + "\nAti introdus caractere unde este nevoie de numar!");
            message.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            message.setTitle("Eroare!");
            message.showAndWait();
        }
    }

    public void handleDeleteStudent() {
        Student selected = studentTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Student deleted = stSrv.delete(selected.getId());
            if (deleted != null) {
                for(Grade gr : grSrv.getAll()) {
                    if(gr.getStudentId() == deleted.getId())
                        grSrv.delete(gr.getId());
                }
                Alert message = new Alert(Alert.AlertType.INFORMATION,"Studentul a fost sters cu succes!");
                message.setTitle("Stergere reusita");
                message.showAndWait();
            }
        }
        else {
            Alert message = new Alert(Alert.AlertType.ERROR, "Nu ati selectat niciun student!");
            message.setTitle("Eroare!");
            message.showAndWait();
        }
    }

    public void handleUpdateStudent() {
        try {
            String strId = idTextField.getText();
            if(strId.equals("")) {
                Alert message = new Alert(Alert.AlertType.ERROR,"Nu ati introdus niciun ID!");
                message.setTitle("Eroare!");
                message.showAndWait();
                return;
            }
            int id = Integer.parseInt(strId);
            String lastName = lastNameTextField.getText();
            String firstName = firstNameTextField.getText();
            String strGr = groupTextField.getText();
            int group = Integer.parseInt(strGr);
            String email = emailTextField.getText();
            String prof = profTextField.getText();
            Student updatedSt = stSrv.update(new Student(id,lastName,firstName,group,email,prof));
            if (updatedSt == null) {
                Alert message = new Alert(Alert.AlertType.INFORMATION,"Studentul a fost modificat cu succes!");
                message.setTitle("Modificare reusita");
                message.showAndWait();
                clearTextFields();
            }
            else {
                Alert message = new Alert(Alert.AlertType.ERROR,"Acest ID nu exista!");
                message.setTitle("Eroare ID inexistent!");
                message.showAndWait();
            }
        }
        catch (ValidationException e) {
            Alert message = new Alert(Alert.AlertType.ERROR,e.getErr());
            message.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            message.setTitle("Eroare de Validare!");
            message.showAndWait();
        }
        catch (NumberFormatException e) {
            Alert message = new Alert(Alert.AlertType.ERROR,e.getMessage() + "\nAti introdus caractere unde este nevoie de numar!");
            message.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            message.setTitle("Eroare!");
            message.showAndWait();
        }
    }

    public void handleSearchStudent() {
        try {
            String strId = idTextField.getText();
            if(strId.equals("")) {
                Alert message = new Alert(Alert.AlertType.ERROR,"Nu ati introdus niciun ID!");
                message.setTitle("Eroare!");
                message.showAndWait();
                clearTextFields();
                return;
            }
            int id = Integer.parseInt(strId);
            Student stFind = stSrv.find(id);
            if (stFind == null) {
                Alert message = new Alert(Alert.AlertType.INFORMATION,"Studentul nu a fost gasit!");
                message.setTitle("Cautare Student");
                message.showAndWait();
            }
            else studentObservableList.setAll(stFind);
        }
        catch (NumberFormatException e) {
            Alert message = new Alert(Alert.AlertType.ERROR,e.getMessage() + "\nAti introdus caractere unde este nevoie de numar!");
            message.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            message.setTitle("Eroare!");
            message.showAndWait();
        }
        clearTextFields();
    }

    public void handleBack() {
        MainApp.primaryStage.setTitle("Aplicatie Facultate");
        MainApp.primaryStage.setScene(MainApp.primaryScene);
    }
}
