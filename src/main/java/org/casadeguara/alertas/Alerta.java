package org.casadeguara.alertas;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.casadeguara.conexao.Conexao;
import org.casadeguara.utilitarios.Criptografia;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

/**
 * Esta classe se responsabiliza pela geração de janelas de diálogo para o usuário.
 * @author Gustavo
 */
public class Alerta {

    /**
     * Exibe uma janela de confirmação para determinada ação e retorna qual foi a opção do usuário.
     * @param mensagem Texto da janela
     * @return boolean
     */
    public boolean confirmacao(String mensagem) {
        Alert confirmar =
                new Alert(Alert.AlertType.CONFIRMATION, mensagem, ButtonType.YES, ButtonType.NO);
        confirmar.setHeaderText(null);
        confirmar.showAndWait();
        return (confirmar.getResult() == ButtonType.YES);
    }

    private void gerarAlerta(Alert.AlertType tipo, String mensagem) {
        new Alert(tipo, mensagem, ButtonType.OK).show();
    }
    
    /**
     * Exibe uma janela de atenção para o usuário que irá realizar uma ação que requer cuidado.
     * @param mensagem Texto da mensagem
     */
    public void cuidado(String mensagem) {
        gerarAlerta(Alert.AlertType.WARNING, mensagem);
    }
    
    /**
     * Exibe uma janela de erro.
     * @param mensagem Texto da mensagem
     */
    public void erro(String mensagem) {
        gerarAlerta(Alert.AlertType.ERROR, mensagem);
    }

    /**
     * Exibe um diálogo informativo simples.
     * @param mensagem Informação
     */
    public void informacao(String mensagem) {
        gerarAlerta(Alert.AlertType.INFORMATION, mensagem);
    }

    /**
     * Exibe uma janela de progresso para determinada thread.
     * @param tarefa Tarefa que será acompanhada
     */
    public void progresso(Worker<?> tarefa) {
        Label progressMessage = new Label();
        progressMessage.setWrapText(true);
        progressMessage.textProperty().bind(tarefa.messageProperty());
        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(tarefa.progressProperty());

        VBox content = new VBox(5);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(progressMessage, progressBar);

        Alert progressDialog = new Alert(AlertType.INFORMATION, null, ButtonType.OK);
        progressDialog.setTitle("Carregando");
        progressDialog.setHeaderText("Aguarde a conclusão da operação.");

        DialogPane dialogPane = progressDialog.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.lookupButton(ButtonType.OK).setVisible(false);
        progressDialog.show();

        tarefa.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(State.SUCCEEDED)) {
                progressDialog.setGraphic(new ImageView("/images/success.png"));
                progressBar.setVisible(false);
            } 
            
            if (newValue.equals(State.CANCELLED) || newValue.equals(State.FAILED)){
                progressDialog.setAlertType(AlertType.ERROR);
                progressBar.setVisible(false);
            }
        });
    }
    
    /**
     * Exibe uma janela com uma senha para autorizar o acesso.
     * @param mensagem Mensagem de autorização
     * @return Retorna se foi autorizado ou não
     */
    public boolean autorizacao(String mensagem) {
        Label lb1 = new Label("Chave mestra:");
        PasswordField password = new PasswordField();
        password.setPrefWidth(250);
        
        HBox pane = new HBox(5);
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.getChildren().addAll(lb1, password);
        
        Dialog<Boolean> dialog = new Dialog<>();
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CLOSE);
        dialogPane.setContent(pane);
        
        dialog.setTitle("Autorização requerida");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setHeaderText(mensagem);
        dialog.setResultConverter(button -> {
            
            if (button.equals(ButtonType.OK)) {
                String sql = "select idconf from configuracao where chave = ?";
                try (Connection con = Conexao.abrir();
                     PreparedStatement ps = con.prepareStatement(sql)) {

                    ps.setString(1, new Criptografia().aplicar("SHA-512", password.getText()));

                    try (ResultSet rs = ps.executeQuery()) {
                        return rs.next();
                    }
                } catch (SQLException | NoSuchAlgorithmException ex) {
                }
            }

            return false;
        });
        
        Optional<Boolean> resultado = dialog.showAndWait();
        
        if(resultado.isPresent()) {
            return resultado.get();
        }
        
        return false;
    }

    /**
     * Exibe um diálogo indicando o sucesso de uma operação.
     * @param mensagem Mensagem
     */
    public void sucesso(String mensagem) {
        Dialog<?> dialog = new Dialog<>();
        dialog.setGraphic(new ImageView("/images/success.png"));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.setHeaderText(null);
        dialog.setContentText(mensagem);
        dialog.show();
    }

}
