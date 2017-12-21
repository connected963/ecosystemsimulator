package main.java.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.Application;

public class Menu extends Application {

    @FXML
    private TextField numeroTamanduas;

    @FXML
    private TextField numeroFormigas;

    @FXML
    private TextField numeroCalorias;

    @FXML
    private TextField tempoDecrementoCalorias;

    @FXML
    private TextField caloriasReproducaoFormigas;

    @FXML
    private TextField caloriasRemovidasReproducaoFormigas;

    @FXML
    private TextField tempoGeracaoAcucares;

    @FXML
    private TextField movimentosPorDirecao;

    @FXML
    private TextField incrementoPorIngestao;

    @FXML
    private TextField intervaloReproducaoTamandua;

    @FXML
    private Button simular;

    public Menu() {
    }

    public void efetuarSimulacao() {
        updateParameters();
        GameScreen gameScreen = new GameScreen();

        Stage stage = new Stage();
        try {
            gameScreen.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateParameters() {
        main.java.common.Parameters.getInstance().setCaloriasRemovidasReproducaoFormigas(Integer.valueOf(caloriasRemovidasReproducaoFormigas.getText()));
        main.java.common.Parameters.getInstance().setCaloriasReproducaoFormigas(Integer.valueOf(caloriasReproducaoFormigas.getText()));
        main.java.common.Parameters.getInstance().setNumeroCalorias(Integer.valueOf(numeroCalorias.getText()));
        main.java.common.Parameters.getInstance().setNumeroFormigas(Integer.valueOf(numeroFormigas.getText()));
        main.java.common.Parameters.getInstance().setNumeroTamanduas(Integer.valueOf(numeroTamanduas.getText()));
        main.java.common.Parameters.getInstance().setMovimentosPorDirecao(Integer.valueOf(movimentosPorDirecao.getText()));
        main.java.common.Parameters.getInstance().setIncrementoPorIngestao(Integer.valueOf(incrementoPorIngestao.getText()));

        final Double tempoCaloriasDouble = Double.valueOf(tempoDecrementoCalorias.getText()) * 1000;
        main.java.common.Parameters.getInstance().setTempoDecrementoCalorias(tempoCaloriasDouble.intValue());

        final Double tempoGeracaoAcucaresDouble = Double.valueOf(tempoGeracaoAcucares.getText());
        main.java.common.Parameters.getInstance().setTempoGeracaoAcucares(tempoGeracaoAcucaresDouble.intValue());

        final Double intervaloReproducaoTamanduaDouble = Double.valueOf(intervaloReproducaoTamandua.getText());
        main.java.common.Parameters.getInstance().setIntervaloReproducaoTamandua(intervaloReproducaoTamanduaDouble.intValue());
    }
}
