package Controllers;

import Application.MainApp;
import Domain.Grade;
import Domain.Homework;
import Domain.Student;
import Exceptions.ValidationException;
import Services.GradeService;
import Services.HomeworkService;
import Services.StudentService;
import UI.ConfirmBox;
import Utilities.Calendar.UniversityYear;
import Utilities.Observers.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;

public class GradeController implements Observer {

    private int currentWeek;
    private UniversityYear universityYear = UniversityYear.getInstance();
    private StudentService stSrv;
    private ObservableList<Student> studentObservableList = FXCollections.observableArrayList();
    private HomeworkService hwSrv;
    private ObservableList<Homework> homeworkObservableList = FXCollections.observableArrayList();
    private GradeService grSrv;
    private ObservableList<Grade> gradeObservableList = FXCollections.observableArrayList();

    @FXML
    TableView<Grade> gradeTableView;
    @FXML
    TableColumn<Grade,String> idColumn;
    @FXML
    TableColumn<Grade,LocalDateTime> dateColumn;
    @FXML
    TableColumn<Grade,String> profColumn;
    @FXML
    TableColumn<Grade,Double> gradeColumn;

    @FXML
    private TextField profTextField;
    @FXML
    private TextField gradeTextField;
    @FXML
    private TextArea feedbackTextField;
    @FXML
    private Label wrongGradeLabel;
    @FXML
    private Label motivationsLabel;

    @FXML
    private ComboBox<Student> stComboBox;
    private TextField stComboText;
    @FXML
    private ComboBox<Homework> hwComboBox;
    @FXML
    private ComboBox<Integer> motivationsComboBox;

    @FXML
    public void initialize() {
        currentWeek = universityYear.getCurrentSemester(LocalDate.now(ZoneId.of("Europe/Bucharest"))).getCurrentWeek(LocalDate.now(ZoneId.of("Europe/Bucharest")));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        profColumn.setCellValueFactory(new PropertyValueFactory<>("prof"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        gradeTableView.setItems(gradeObservableList);

        stComboBox.setItems(studentObservableList);
        hwComboBox.setItems(homeworkObservableList);
        motivationsComboBox.setItems(FXCollections.observableArrayList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14));
    }

    public void init(StudentService stSrv, HomeworkService hwSrv, GradeService grSrv) {
        this.stSrv = stSrv;
        this.hwSrv = hwSrv;
        this.grSrv = grSrv;
        this.stSrv.addObserver(this);
        this.hwSrv.addObserver(this);
        this.grSrv.addObserver(this);
        loadItems();
        stComboText = stComboBox.getEditor();
        stComboText.setOnKeyTyped(event -> handleStudentSearch());
    }

    @Override
    public void update() {
        loadItems();
    }

    @FXML
    public void loadItems() {
        Collection<Student> students = stSrv.getAll();
        studentObservableList.setAll(students);
        Collection<Homework> homeworks = hwSrv.getAll();
        hwComboBox.setValue(null);
        homeworkObservableList.setAll(homeworks);
        Collection<Grade> grades = grSrv.getAll();
        gradeObservableList.setAll(grades);

        if(hwSrv.findByWeek(currentWeek).size() != 0)
            hwComboBox.setValue(hwSrv.findByWeek(currentWeek).get(0));

        stComboBox.setVisibleRowCount(stSrv.getAll().size());
        hwComboBox.setVisibleRowCount(hwSrv.getAll().size());
        motivationsComboBox.setValue(0);
        motivationsComboBox.setVisibleRowCount(15);

        handleHwComboBox();
    }

    private void clearTextFields() {
        stComboText.clear();
        profTextField.clear();
        gradeTextField.clear();
    }

    public void handleSaveGrade() {
        try {
            Homework hw = hwComboBox.getValue();
            if(hw == null) {
                Alert message = new Alert(Alert.AlertType.ERROR,"Nu ati selectat nicio tema!");
                message.setTitle("Eroare!");
                message.showAndWait();
                return;
            }
            int idHw = hw.getId();

            String str = stComboText.getText();
            if(str == null) {
                Alert message = new Alert(Alert.AlertType.ERROR,"Nu ati selectat niciun student!");
                message.setTitle("Eroare!");
                message.showAndWait();
                return;
            }
            String[] values = str.split(",");
            Student st = new Student(Integer.parseInt(values[0]),values[1],values[2],Integer.parseInt(values[3]),values[4],values[5]);
            int idSt = st.getId();
            int motivations = motivationsComboBox.getValue();
            LocalDate date = LocalDate.now(ZoneId.of("Europe/Bucharest"));
            if(motivations != 0) {
                int realWeek = currentWeek - motivations;
                if(realWeek <= hw.getStartWeek()) realWeek = hw.getDeadlineWeek();
                date = universityYear.getCurrentSemester(date).getCurrentDate(realWeek);
            }
            double maxGrade = grSrv.calculateGrade(10, currentWeek, hw.getDeadlineWeek(), motivations);
            if(maxGrade < 8) {
                Alert message = new Alert(Alert.AlertType.ERROR, "Nu se poate da nota deoarece ar insemna ca studentul sa aiba o penalizare mai mare de 2 puncte!");
                message.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                message.setTitle("Eroare perioada de gratie!");
                message.showAndWait();
                return;
            }
            String prof = profTextField.getText();
            String gradeStr = gradeTextField.getText();
            double gradeValue = Double.parseDouble(gradeStr);
            DecimalFormat df = new DecimalFormat("#.####");
            gradeValue = Double.parseDouble(df.format(gradeValue));
            gradeValue = grSrv.calculateGrade(gradeValue, currentWeek, hw.getDeadlineWeek(), motivations);
            String feedback = feedbackTextField.getText();
            if(!ConfirmBox.display("Verificare Adaugare Tema",
                    "Tema: " + hw.getStartWeek() +
                            "\nStudentul: " + st.getFullName() +
                            "\nNota finala: " + gradeValue +
                            "\nFeedback: " + feedback)) {
                return;
            }
            Grade grAdd = grSrv.save(new Grade(idSt,idHw,date,prof,gradeValue), st.getLastName(), st.getFirstName(), hw.getStartWeek(), hw.getDeadlineWeek(), universityYear.getCurrentSemester(date).getCurrentWeek(date), feedback);
            if (grAdd == null) {
                Alert message = new Alert(Alert.AlertType.INFORMATION,"Nota a fost adaugata cu succes!");
                message.setTitle("Adaugare reusita");
                message.showAndWait();
                clearTextFields();
            }
            else {
                Alert message = new Alert(Alert.AlertType.ERROR, "Nu s-a adaugat nota! Acest student deja a primit nota la aceasta tema!");
                message.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
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

    public void handleDeleteGrade() {
        Grade selected = gradeTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Grade deleted = grSrv.delete(selected.getId());
            if (null != deleted) {
                Alert message = new Alert(Alert.AlertType.INFORMATION,"Nota a fost stearsa cu succes!");
                message.setTitle("Stergere reusita");
                message.showAndWait();
            }
        }
        else {
            Alert message = new Alert(Alert.AlertType.ERROR, "Nu ati selectat nicio nota!");
            message.setTitle("Eroare!");
            message.showAndWait();
        }
    }

    public void handleUpdateGrade() {
        //TODO
    }

    public void handleSearchGrade() {
        try {
            Homework hw = hwComboBox.getValue();
            if(hw == null) {
                Alert message = new Alert(Alert.AlertType.ERROR,"Nu ati selectat nicio tema!");
                message.setTitle("Eroare!");
                message.showAndWait();
                return;
            }
            String idHw = hw.getId().toString();

            String str = stComboText.getText();
            if(str == null) {
                Alert message = new Alert(Alert.AlertType.ERROR,"Nu ati selectat niciun student!");
                message.setTitle("Eroare!");
                message.showAndWait();
                return;
            }
            String[] values = str.split(",");
            String idSt = values[0];

            Grade grFind = grSrv.find(idSt + "_" + idHw);
            if (grFind == null) {
                Alert message = new Alert(Alert.AlertType.INFORMATION,"Nota nu a fost gasita!");
                message.setTitle("Cautare Nota");
                message.showAndWait();
            }
            else gradeObservableList.setAll(grFind);
        }
        catch (NumberFormatException e) {
            Alert message = new Alert(Alert.AlertType.ERROR,e.getMessage() + "\nAti introdus caractere unde este nevoie de numar!");
            message.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            message.setTitle("Eroare!");
            message.showAndWait();
        }
        clearTextFields();
    }

    public void handleHwComboBox() {
        if(hwComboBox.getValue() != null) {
            if (currentWeek > hwComboBox.getValue().getDeadlineWeek())
                motivationsLabel.setText("Deadline-ul temei a fost depasit. Verificati daca studentul are motivari si selectati in dreapta numarul acestora.");
            else motivationsLabel.setText("");
            int difference = currentWeek - hwComboBox.getValue().getDeadlineWeek() - motivationsComboBox.getValue();
            if (difference < 0) difference = 0;
            if (difference > 2) difference = 2;
            feedbackTextField.setText("- Nota a fost diminuata cu " + difference + " puncte din cauza intarzierilor.");
        }
    }

    public void handleGradeValue() {
        String txt = gradeTextField.getText();
        if(txt.equals("")) wrongGradeLabel.setText("");
        else {
            try {
                double txtnr = Double.parseDouble(txt);
                if (txtnr < 1 || txtnr > 10) throw new NumberFormatException();
                else wrongGradeLabel.setText("");
            } catch (NumberFormatException e) {
                wrongGradeLabel.setText("Introduceti un numar real cuprins intre 1 si 10!");
            }
        }
    }

    public void handleStudentSearch() {
        String txt = stComboText.getText();
        if(txt.equals("")) {
            Collection<Student> students = stSrv.getAll();
            studentObservableList.setAll(students);
            stComboBox.setVisibleRowCount(stSrv.getAll().size());
            stComboBox.hide();
        }
        else {
            Collection<Student> students = stSrv.findByName(txt);
            studentObservableList.setAll(students);
            stComboBox.setVisibleRowCount(stSrv.findByName(txt).size());
            stComboBox.show();
        }
    }

    public void handleBack() {
        MainApp.primaryStage.setTitle("Aplicatie Facultate");
        MainApp.primaryStage.setScene(MainApp.primaryScene);
    }
}
