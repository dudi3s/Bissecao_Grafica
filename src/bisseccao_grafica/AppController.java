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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author eduar
 */
public class AppController implements Initializable {

    @FXML
    private TextField funcao, valor_a, valor_b, precisao, iteracoes;

    @FXML
    private TableView<Result> processo;

    @FXML
    private TableColumn<Result, String> it, a, b, media, fa, fb, sinal, erro;

    @FXML
    private Button calcular;

    @FXML
    private Label raiz;

    @FXML
    private LineChart<String, Double> grafico;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monitoraCalcular();
        inicializaTabelView();
    }

    private void inicializaTabelView() {

        it.setCellValueFactory(
                new PropertyValueFactory<>("iteracao"));
        a.setCellValueFactory(
                new PropertyValueFactory<>("a"));
        b.setCellValueFactory(
                new PropertyValueFactory<>("b"));
        media.setCellValueFactory(
                new PropertyValueFactory<>("media"));
        fa.setCellValueFactory(
                new PropertyValueFactory<>("f_a"));
        fb.setCellValueFactory(
                new PropertyValueFactory<>("f_b"));
        sinal.setCellValueFactory(
                new PropertyValueFactory<>("sinal"));
        erro.setCellValueFactory(
                new PropertyValueFactory<>("erro"));

    }

    private void monitoraCalcular() {
        calcular.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                grafico.getData().clear();
                grafico.setLegendVisible(false);
                processo.getItems().clear();

                Function fun = new Function("f", funcao.getText(), "x");
                String f = funcao.getText();
                double a = Double.parseDouble(valor_a.getText());
                double b = Double.parseDouble(valor_b.getText());
                double prec = Double.parseDouble(precisao.getText());
                int it = Integer.parseInt(iteracoes.getText());

                final XYChart.Series<String, Double> series = new XYChart.Series<>();
                for (double x = a; x <= b; x++) {
                    Expression y = new Expression("f(" + x + ")", fun);
                    series.getData().add(new XYChart.Data<>(String.valueOf((int) x), y.calculate()));
                }
                grafico.getData().add(series);

                double result = metodo_bissecao(a, b, prec, it, f);
                raiz.setText("Raíz: " + String.valueOf(result));
            }
        });
    }

    private double metodo_bissecao(double a, double b, double precisao, int iteracoes, String funcao) {
        Function fun = new Function("f", funcao, "x");
        double Erro = 1;
        int Cont = 0;
        double Raiz = a;
        double Temp;

        while (Erro > precisao && Cont < iteracoes) {
            Temp = Raiz;
            Raiz = (a + b) / 2;

            Erro = Math.abs((Raiz - Temp) / Raiz);
            Expression eA = new Expression("f(" + a + ")", fun);
            Expression eB = new Expression("f(" + Raiz + ")", fun);

            double auxA = eA.calculate();
            double auxB = eB.calculate();

            if (auxA * auxB < 0) {
                b = Raiz;
                Result resultado_Iteracao = new Result(String.valueOf(Cont), String.valueOf(a), String.valueOf(b), String.valueOf(Raiz),
                        String.valueOf(auxA), String.valueOf(auxB), "-", String.valueOf(Erro));
                processo.getItems().add(resultado_Iteracao);
            } else {
                a = Raiz;
                Result resultado_Iteracao = new Result(String.valueOf(Cont), String.valueOf(a), String.valueOf(b), String.valueOf(Raiz),
                        String.valueOf(auxA), String.valueOf(auxB), "+", String.valueOf(Erro));
                processo.getItems().add(resultado_Iteracao);
            }
            Cont++;
        }
        return Raiz;
    }

    public static class Result {

        private SimpleStringProperty iteracao = new SimpleStringProperty("");
        private SimpleStringProperty a = new SimpleStringProperty("");
        private SimpleStringProperty b = new SimpleStringProperty("");
        private SimpleStringProperty media = new SimpleStringProperty("");
        private SimpleStringProperty f_a = new SimpleStringProperty("");
        private SimpleStringProperty f_b = new SimpleStringProperty("");
        private SimpleStringProperty sinal = new SimpleStringProperty("");
        private SimpleStringProperty erro = new SimpleStringProperty("");

        public Result(String iteracao, String a, String b, String media, String f_a, String f_b, String sinal, String erro) {
            this.iteracao.set(iteracao);
            this.a.set(a);
            this.b.set(b);
            this.media.set(media);
            this.f_a.set(f_a);
            this.f_b.set(f_b);
            this.sinal.set(sinal);
            this.erro.set(erro);
        }

        public String getIteracao() {
            return this.iteracao.get();
        }

        public String getA() {
            return this.a.get();
        }

        public String getB() {
            return this.b.get();
        }

        public String getMedia() {
            return this.media.get();
        }

        public String getF_a() {
            return this.f_a.get();
        }

        public String getF_b() {
            return this.f_b.get();
        }

        public String getSinal() {
            return this.sinal.get();
        }

        public String getErro() {
            return this.erro.get();
        }

        public void setIteracao(String iteracao) {
            this.iteracao.set(iteracao);
        }

        public void setA(String a) {
            this.a.set(a);
        }

        public void setB(String b) {
            this.b.set(b);
        }

        public void setMedia(String media) {
            this.media.set(media);
        }

        public void setF_a(String f_a) {
            this.f_a.set(f_a);
        }

        public void setF_b(String f_b) {
            this.f_b.set(f_b);
        }

        public void setSinal(String sinal) {
            this.sinal.set(sinal);
        }

        public void setErro(String erro) {
            this.erro.set(erro);
        }

        @Override
        public String toString() {
            return "Result{" + "iteracao=" + iteracao + ", a=" + a + ", b=" + b + ", media=" + media + ", f_a=" + f_a + ", f_b=" + f_b + ", sinal=" + sinal + ", erro=" + erro + '}';
        }
    }
}
