/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bisseccao_grafica;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author eduar
 */
public class Bisseccao_Grafica extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/FXML_App.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Método da Bisseção");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/math.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {      
        launch(args);
    }

}
