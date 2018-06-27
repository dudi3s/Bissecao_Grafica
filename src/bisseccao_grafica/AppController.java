package bisseccao_grafica;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.mariuszgromada.math.mxparser.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    private Button calcular, limpar;

    @FXML
    private Label raiz;

    @FXML
    private LineChart<String, Double> grafico;

    @FXML
    private CheckBox isPolinomial;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monitoraCalcular();
        monitoraLimpar();
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

    private void monitoraLimpar() {
        limpar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                grafico.getData().clear();
                raiz.setText("Raiz = ?");
                funcao.setText("");
                valor_a.setText("");
                valor_b.setText("");
                precisao.setText("");
                iteracoes.setText("");
                isPolinomial.setSelected(false);
                processo.getItems().clear();
            }
        });

    }

    private void monitoraCalcular() {
        calcular.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                grafico.getData().clear();
                grafico.setLegendVisible(false);
                processo.getItems().clear();

                String f = funcao.getText();
                if (f.contains(",")) {
                    f = f.replace(",", ".");
                }

                double a = 0.0;
                double b = 0.0;

                double a_graf = 0.0;
                double b_graf = 0.0;

                double prec = Double.parseDouble(precisao.getText());
                int it = 0;

                if (isPolinomial.isSelected()) {
                    String f_times = "";

                    if (f.contains("*")) {
                        f_times = f.replace("*", "");
                    } else {
                        f_times = f;
                    }

                    ArrayList<Monomio> monomios_func = monomios(f_times);
                    Collections.sort(monomios_func, new monomioComparator());

                    double intervalo = cotaFujiwara(monomios_func);
                    a_graf = (intervalo * (-1.0)) - 1.0;
                    b_graf = intervalo + 1.0;

                    a = (intervalo * (-1.0)) - 0.1;
                    b = intervalo - 0.1;

                    Expression logB_A = new Expression("ln(" + (b - a) + ")");
                    Expression logE = new Expression("ln(" + prec + ")");
                    Expression log2 = new Expression("ln(" + 2 + ")");

                    double aprox_it = Math.ceil((logB_A.calculate() - logE.calculate()) / log2.calculate());
                    it = (int) aprox_it;

                    valor_a.setText(String.valueOf(a));
                    valor_b.setText(String.valueOf(b));
                    iteracoes.setText(String.valueOf(it));

                    //System.out.println(f + " [" + a + "," + b + "] " + " " + prec + " " + aprox_it);
                } else {
                    String valA = valor_a.getText();
                    String valB = valor_b.getText();

                    if (valA.contains(",")) {
                        valA = valA.replace(",", ".");
                    }
                    if (valB.contains(",")) {
                        valB = valB.replace(",", ".");
                    }

                    it = Integer.parseInt(iteracoes.getText());

                    a_graf = a = Double.parseDouble(valA);
                    b_graf = b = Double.parseDouble(valB);

                    //System.out.println(f + " [" + a + "," + b + "] " + it + " " + prec);
                }

                Function fun = new Function("f", f, "x");

                final XYChart.Series<String, Double> series = new XYChart.Series<>();
                for (double x = a_graf; x <= b_graf; x++) {
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
        System.out.println(funcao + " [" + a + "," + b + "] " + iteracoes + " " + precisao);
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

    public ArrayList<Monomio> monomios(String exp) {
        ArrayList<Monomio> ret = new ArrayList<>();
        Pattern pattern = Pattern.compile("([+-]?[^-+]+)");
        Matcher matcher = pattern.matcher(exp);
        int x = 0;
        while (matcher.find()) {
            x = x + 1;

            String monomio = matcher.group(1);
            if (monomio.contains("x")) {
                if (monomio.contains("^")) {
                    ret.add(geraMonomioComGrau(monomio));
                } else {
                    ret.add(geraMonomioComX(monomio));
                }
            } else if (!monomio.contains("x")) {
                ret.add(geraMonomioSemVar(monomio));
            }
        }

        return ret;
    }

    private Monomio geraMonomioComGrau(String m) {
        String coef;
        String grau = m.substring(m.indexOf("^") + 1, m.length());
        if (m.startsWith("x")) {
            coef = String.valueOf(1);
        } else if (m.startsWith("-x")) {
            coef = String.valueOf(-1);
        } else {
            coef = m.substring(0, m.indexOf("^") - 1);
        }

        return new Monomio(Integer.parseInt(grau), Integer.parseInt(coef));

    }

    private Monomio geraMonomioComX(String m) {
        String coef = m.substring(0, m.indexOf("x"));
        return new Monomio(1, Integer.parseInt(coef));
    }

    private Monomio geraMonomioSemVar(String m) {
        return new Monomio(0, Integer.parseInt(m));
    }

    public class monomioComparator implements Comparator<Monomio> {

        @Override
        public int compare(Monomio t, Monomio t1) {
            return t.getGrau() - t1.getGrau();
        }
    }

    private Double cotaFujiwara(ArrayList<Monomio> polinomio) {
        ArrayList<Double> m = new ArrayList<>();

        int maxGrau = polinomio.get(polinomio.size() - 1).getGrau();
        Monomio aMaxGrau = polinomio.get(polinomio.size() - 1);

        for (int i = 1; i <= maxGrau; i++) {
            int coef = 0;
            Monomio aNminusI = getMonomioGrau(maxGrau - i, polinomio);

            if (aNminusI != null) {
                coef = aNminusI.getCoef();
            }

            int resDiv = Math.abs(coef / aMaxGrau.getCoef());

            m.add(Math.pow(resDiv, (1 / i)));
        }

        Collections.sort(m);

        return 2 * m.get(m.size() - 1);
    }

    private Monomio getMonomioGrau(int grau, ArrayList<Monomio> polinomio) {
        for (Monomio m : polinomio) {
            if (m.getGrau() == grau) {
                return m;
            }
        }
        return null;
    }

    private class Monomio {

        private int grau;
        private int coef;

        public Monomio(int grau, int coef) {
            this.grau = grau;
            this.coef = coef;
        }

        public int getGrau() {
            return grau;
        }

        public void setGrau(int grau) {
            this.grau = grau;
        }

        public int getCoef() {
            return coef;
        }

        public void setCoef(int coef) {
            this.coef = coef;
        }

        @Override
        public String toString() {
            return "Polinomio{" + "grau=" + grau + ", coef=" + coef + '}';
        }

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
