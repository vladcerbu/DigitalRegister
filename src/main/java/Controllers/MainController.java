package Controllers;

import Application.MainApp;
import javafx.stage.WindowEvent;

public class MainController {

    public void handleTeme() {
        MainApp.primaryStage.setTitle("Aplicatie Facultate - Teme");
        MainApp.primaryStage.setScene(MainApp.hwScene);
    }

    public void handleStudent() {
        MainApp.primaryStage.setTitle("Aplicatie Facultate - Studenti");
        MainApp.primaryStage.setScene(MainApp.stScene);
    }

    public void handleNote() {
        MainApp.primaryStage.setTitle("Aplicatie Facultate - Note");
        MainApp.primaryStage.setScene(MainApp.grScene);
    }

    public void handleReports() {
        MainApp.primaryStage.setTitle("Aplicatie Facultate - Rapoarte");
        MainApp.primaryStage.setScene(MainApp.repScene);
    }

    public void handleExit() {
        MainApp.primaryStage.fireEvent(new WindowEvent(MainApp.primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
