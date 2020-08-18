package Controllers;

import Application.MainApp;
import Domain.HelpfulDTO;
import Services.GradeService;
import Services.HomeworkService;
import Services.ReportService;
import Services.StudentService;
import Utilities.Observers.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class ReportController implements Observer {

    public ReportService repSrv;
    public StudentService stSrv;
    public HomeworkService hwSrv;
    public GradeService grSrv;
    private ObservableList<HelpfulDTO> rep1ObservableList = FXCollections.observableArrayList();
    private ObservableList<HelpfulDTO> rep2ObservableList = FXCollections.observableArrayList();
    private ObservableList<String> rep4ObservableList = FXCollections.observableArrayList();

    @FXML
    TableView<HelpfulDTO> rap1TableView;
    @FXML
    TableColumn<HelpfulDTO,Integer> id1Column;
    @FXML
    TableColumn<HelpfulDTO,String> nume1Column;
    @FXML
    TableColumn<HelpfulDTO,Integer> grupa1Column;
    @FXML
    TableColumn<HelpfulDTO,Double> medie1Column;
    @FXML
    TableView<HelpfulDTO> rap2TableView;
    @FXML
    TableColumn<HelpfulDTO,Integer> id2Column;
    @FXML
    TableColumn<HelpfulDTO,String> nume2Column;
    @FXML
    TableColumn<HelpfulDTO,Integer> grupa2Column;
    @FXML
    TableColumn<HelpfulDTO,Double> medie2Column;
    @FXML
    ListView<String> rap4ListView;
    @FXML
    private Label hardHwLabel;
    @FXML
    private Label hardGrLabel;

    @FXML
    public void initialize() {
        id1Column.setCellValueFactory(new PropertyValueFactory<>("idSt"));
        nume1Column.setCellValueFactory(new PropertyValueFactory<>("numeSt"));
        grupa1Column.setCellValueFactory(new PropertyValueFactory<>("grSt"));
        medie1Column.setCellValueFactory(new PropertyValueFactory<>("medie"));
        medie1Column.setSortType(TableColumn.SortType.DESCENDING);
        rap1TableView.setItems(rep1ObservableList);
        rap1TableView.getSortOrder().add(medie1Column);
        id2Column.setCellValueFactory(new PropertyValueFactory<>("idSt"));
        nume2Column.setCellValueFactory(new PropertyValueFactory<>("numeSt"));
        grupa2Column.setCellValueFactory(new PropertyValueFactory<>("grSt"));
        medie2Column.setCellValueFactory(new PropertyValueFactory<>("medie"));
        grupa2Column.setSortType(TableColumn.SortType.ASCENDING);
        rap2TableView.setItems(rep2ObservableList);
        rap2TableView.getSortOrder().add(grupa2Column);
        rap4ListView.setItems(rep4ObservableList);
    }

    public void init(StudentService stSrv, HomeworkService hwSrv, GradeService grSrv) {
        this.stSrv = stSrv;
        this.hwSrv = hwSrv;
        this.grSrv = grSrv;
        this.repSrv = new ReportService(stSrv,hwSrv,grSrv);
        this.stSrv.addObserver(this);
        this.hwSrv.addObserver(this);
        this.grSrv.addObserver(this);
        loadData();
    }

    @Override
    public void update() {
        loadData();
    }

    public void loadData() {
        ArrayList<HelpfulDTO> list1 = repSrv.mediiStudenti();
        List<HelpfulDTO> list2 = repSrv.mediiStudenti5();
        ArrayList<String> list4 = repSrv.predateBine();
        rep1ObservableList.setAll(list1);
        rep2ObservableList.setAll(list2);
        rep4ObservableList.setAll(list4);
        HelpfulDTO nota = repSrv.notaMica();
        hardHwLabel.setText("Cea mai grea tema: Lab " + nota.getLabNr());
        hardGrLabel.setText("Nota: " + nota.getMedie());
    }

    public void handleBack() {
        MainApp.primaryStage.setTitle("Aplicatie Facultate");
        MainApp.primaryStage.setScene(MainApp.primaryScene);
    }
}
