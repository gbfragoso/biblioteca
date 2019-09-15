package org.casadeguara.dialogos;

import org.casadeguara.componentes.MaskedTextField;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DialogoRecuperarEmprestimo extends Dialog<Integer>{
    
    private MaskedTextField txtMovimentacao;

    public DialogoRecuperarEmprestimo() {
        setTitle("Recuperar empréstimo devolvido");
        setHeaderText("Recupere um empréstimo a partir do log de movimentação.");
        
        DialogPane dialogPane = getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialogPane.setContent(createContent());

        setResultConverter(button -> {
            if(button == ButtonType.OK) {
                return Integer.parseInt(txtMovimentacao.getPlainText());
            }
            return 0;
        });
    }
    
    private GridPane createContent() {
        Label textoJanela = new Label("Id da movimentação:");
        txtMovimentacao = new MaskedTextField("######", ' ');
        
        GridPane content = new GridPane();
        content.setHgap(5);
        content.setVgap(5);
        content.add(textoJanela, 0, 0);
        content.add(txtMovimentacao, 1, 0);
        
        return content;
    }
}
