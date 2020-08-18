package Controllers;

import Application.MainApp;
import Domain.Grade;
import Domain.Homework;
import Exceptions.InvalidDateException;
import Exceptions.ValidationException;
import Services.GradeService;
import Services.HomeworkService;
import Utilities.Calendar.UniversityYear;
import Utilities.Observers.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;

public class HomeworkController implements Observer {

    private int startWeek;
    private UniversityYear universityYear = UniversityYear.getInstance();
    private HomeworkService hwSrv;
    private GradeService grSrv;
    private ObservableList<Homework> homeworkObservableList = FXCollections.observableArrayList();

    @FXML
    TableView<Homework> homeworkTableView;
    @FXML
    TableColumn<Homework,Integer> idColumn;
    @FXML
    TableColumn<Homework,String> descriptionColumn;
    @FXML
    TableColumn<Homework,Integer> startWeekColumn;
    @FXML
    TableColumn<Homework,Integer> deadlineWeekColumn;

    @FXML
    private TextField idTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField deadlineWeekTextField;
    @FXML
    private Label startWeekLabel;

    @FXML
    public void initialize() {
        startWeek = universityYear.getCurrentSemester(LocalDate.now(ZoneId.of("Europe/Bucharest"))).getCurrentWeek(LocalDate.now(ZoneId.of("Europe/Bucharest")));
        if(startWeek == 14)
            startWeekLabel.setText("\tNu se mai pot da teme deoarece ne aflam in ultima saptamana din semestru.");
        else if(startWeek == 0)
            startWeekLabel.setText("\tNu se mai pot da teme deoarece ne aflam in vacanta intersemestriala/sesiune.");
        else
            startWeekLabel.setText("\tSaptamana numarul: " + startWeek + "\n\tSaptamana de deadline trebuie sa fie cuprinsa intre " + (startWeek+1) + " si 14.");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startWeekColumn.setCellValueFactory(new PropertyValueFactory<>("startWeek"));
        deadlineWeekColumn.setCellValueFactory(new PropertyValueFactory<>("deadlineWeek"));
        homeworkTableView.setItems(homeworkObservableList);
    }

    public void init(HomeworkService hwSrv, GradeService grSrv) {
        this.hwSrv = hwSrv;
        this.grSrv = grSrv;
        this.hwSrv.addObserver(this);
        this.grSrv.addObserver(this);
        loadItems();
    }

    @FXML
    private void loadItems() {
        Collection<Homework> homeworks = hwSrv.getAll();
        homeworkObservableList.setAll(homeworks);
    }

    @Override
    public void update() {
        loadItems();
    }

    private void clearTextFields() {
        idTextField.clear();
        descriptionTextField.clear();
        deadlineWeekTextField.clear();
    }

    public void handleSaveHomework() {
        try {
            String strId = idTextField.getText();
            if(strId.equals("")) {
                Alert message = new Alert(Alert.AlertType.ERROR,"Nu ati introdus niciun ID!");
                message.setTitle("Eroare!");
                message.showAndWait();
                return;
            }
            int id = Integer.parseInt(strId);
            String description = descriptionTextField.getText();
            String deadlineWeekStr = deadlineWeekTextField.getText();
            int deadlineWeek = Integer.parseInt(deadlineWeekStr);
            Homework savedHw = hwSrv.save(new Homework(id,description,startWeek,deadlineWeek));
            if (savedHw == null) {
                Alert message = new Alert(Alert.AlertType.INFORMATION,"Tema a fost adaugata cu succes!");
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

    public void handleDeleteHomework() {
        Homework selected = homeworkTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Homework deleted = hwSrv.delete(selected.getId());
            if (deleted != null) {
                for(Grade gr : grSrv.getAll()) {
                    if(gr.getHomeworkId() == deleted.getId())
                        grSrv.delete(gr.getId());
                }
                Alert message = new Alert(Alert.AlertType.INFORMATION,"Tema a fost stearsa cu succes!");
                message.setTitle("Stergere reusita");
                message.showAndWait();
            }
        }
        else {
            Alert message = new Alert(Alert.AlertType.ERROR, "Nu ati selectat nicio tema!");
            message.setTitle("Eroare!");
            message.showAndWait();
        }
    }

    public void handleUpdateHomework() {
        try {
            String strId = idTextField.getText();
            if(strId.equals("")) {
                Alert message = new Alert(Alert.AlertType.ERROR,"Nu ati introdus niciun ID!");
                message.setTitle("Eroare!");
                message.showAndWait();
                return;
            }
            int id = Integer.parseInt(strId);
            String description = descriptionTextField.getText();
            String deadlineWeekStr = deadlineWeekTextField.getText();
            int deadlineWeek = Integer.parseInt(deadlineWeekStr);
            Homework updatedHw = hwSrv.update(new Homework(id,description,startWeek,deadlineWeek));
            if (updatedHw == null) {
                Alert message = new Alert(Alert.AlertType.INFORMATION,"Tema a fost modificata cu succes!");
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
            Alert message = new Alert(Alert.AlertType.ERROR, e.getErr());
            message.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            message.setTitle("Eroare de Validare!");
            message.showAndWait();
        }
        catch (InvalidDateException e) {
            Alert message = new Alert(Alert.AlertType.ERROR, e.getErr());
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

    public void handleSearchHomework() {
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
            Homework hwFind = hwSrv.find(id);
            if (hwFind == null) {
                Alert message = new Alert(Alert.AlertType.INFORMATION,"Tema nu a fost gasita!");
                message.setTitle("Cautare Tema");
                message.showAndWait();
            }
            else homeworkObservableList.setAll(hwFind);
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
