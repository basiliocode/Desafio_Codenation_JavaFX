package com.thiago.desafio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DesafioApp extends Application {


    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        stage.setScene(new Scene(root));
        stage.show();
    }

    public static class Launcher{
        public static void main(String[] args) { Application.launch(DesafioApp.class,args); }
    }

}
