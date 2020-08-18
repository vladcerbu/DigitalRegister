package Application;

import AppConfig.ApplicationContext;
import Controllers.*;
import Persistance.GradeRepository;
import Persistance.HomeworkRepository;
import Persistance.StudentRepository;
import Services.GradeService;
import Services.HomeworkService;
import Services.StudentService;
import UI.ConfirmBox;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {

    public static Stage primaryStage;
    public static Scene primaryScene;
    public static Scene stScene;
    public static Scene hwScene;
    public static Scene grScene;
    public static Scene repScene;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws IOException {
        MainApp.primaryStage = primaryStage;

        String fileN2 = ApplicationContext.getProperties().getProperty("data.domain.homeworks");
        HomeworkRepository hwRepo = new HomeworkRepository(fileN2);
        HomeworkService hwSrv = new HomeworkService(hwRepo);

        String fileN3 = ApplicationContext.getProperties().getProperty("data.domain.grades");
        String fileN4 = ApplicationContext.getProperties().getProperty("data.gradeBook");
        GradeRepository grRepo = new GradeRepository(fileN3);
        GradeService grSrv = new GradeService(grRepo,fileN4);

        String fileN1 = ApplicationContext.getProperties().getProperty("data.domain.students");
        StudentRepository stRepo = new StudentRepository(fileN1);
        StudentService stSrv = new StudentService(stRepo,fileN4);

        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/StudentView.fxml"));
        HBox root1 = loader1.load();
        StudentController stCtrl = loader1.getController();
        stCtrl.init(stSrv, grSrv);
        stScene = new Scene(root1, 800, 500);

        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/HomeworkView.fxml"));
        HBox root2 = loader2.load();
        HomeworkController hwCtrl = loader2.getController();
        hwCtrl.init(hwSrv, grSrv);
        hwScene = new Scene(root2, 800, 500);

        FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/GradeView.fxml"));
        HBox root3 = loader3.load();
        GradeController grCtrl = loader3.getController();
        grCtrl.init(stSrv,hwSrv,grSrv);
        grScene = new Scene(root3, 800, 500);

        FXMLLoader loader4 = new FXMLLoader(getClass().getResource("/ReportView.fxml"));
        AnchorPane root4 = loader4.load();
        ReportController repCtrl = loader4.getController();
        repCtrl.init(stSrv,hwSrv,grSrv);
        repScene = new Scene(root4, 900, 600);

        FXMLLoader loader5 = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        AnchorPane root = loader5.load();
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            if(ConfirmBox.display("Iesire din aplicatie","Esti sigur ca vrei sa pleci? :("))
                primaryStage.close();
        });
        primaryScene = new Scene(root, 300, 350);
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("Aplicatie Facultate");
        primaryStage.show();
    }
}
