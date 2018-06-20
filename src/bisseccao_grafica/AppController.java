package bisseccao_grafica;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.mariuszgromada.math.mxparser.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author eduar
 */
public class AppController implements Initializable {

    @FXML
    private TextField funcao, valor_a, valor_b, erro, iteracoes;

    @FXML
    private TableView processo;

    @FXML
    private Button calcula;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        calcula.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String f = funcao.getText();
                String a = valor_a.getText();
                String b = valor_b.getText();
                String err = erro.getText();
                String it = iteracoes.getText();
                System.out.println(f_x(f, a));
                System.out.println(f_x(f, b));

            }
        });

    }

    private class Result {

        private SimpleStringProperty iteracao = new SimpleStringProperty("");
        private SimpleStringProperty a = new SimpleStringProperty("");
        private SimpleStringProperty b = new SimpleStringProperty("");
        private SimpleStringProperty media = new SimpleStringProperty("");
        private SimpleStringProperty f_a = new SimpleStringProperty("");
        private SimpleStringProperty f_b = new SimpleStringProperty("");
        private SimpleStringProperty sinal = new SimpleStringProperty("");
        private SimpleStringProperty erro = new SimpleStringProperty("");

    }

    private double f_x(String funcao, String argumento) {
        Function fun = new Function("f", funcao, "x");
        Expression e = new Expression("f(" + argumento + ")", fun);
        return e.calculate();
    }

}
