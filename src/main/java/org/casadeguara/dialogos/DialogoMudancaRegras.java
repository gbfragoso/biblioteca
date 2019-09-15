package org.casadeguara.dialogos;

import java.util.ArrayList;
import java.util.List;
import org.casadeguara.negocio.Regra;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

public class DialogoMudancaRegras extends Dialog<List<Integer>>{
    
    private int duracaoEmprestimo;
    private int duracaoRenovacao;
    private int limiteEmprestimosPorPessoa;
    private int limiteRenovacoes;
    private int intervaloEntreEmprestimos;
    private TextField txtDuracaoEmprestimo;
    private TextField txtDuracaoRenovacao;
    private TextField txtLimiteEmprestimos;
    private TextField txtLimiteRenovacoes;
    private TextField txtIntervaloEmprestimos;
    
    public DialogoMudancaRegras(Regra regra) {
        this.duracaoEmprestimo = regra.getDuracaoEmprestimo();
        this.duracaoRenovacao = regra.getDuracaoRenovacao();
        this.limiteEmprestimosPorPessoa = regra.getLimiteEmprestimosPorPessoa();
        this.limiteRenovacoes = regra.getLimiteRenovacoes();
        this.intervaloEntreEmprestimos = regra.getIntervaloEntreEmprestimos();
        
        setTitle("Alteração das regras de negócio");
        setHeaderText("As regras passam a valer na próxima inicialização do programa.");
        
        DialogPane dialogPane = getDialogPane();
        dialogPane.setContent(createContent());

        ButtonType alterar = new ButtonType("Alterar", ButtonData.OK_DONE);
        ButtonType sair = new ButtonType("Sair", ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(alterar, sair);
        
        setResultConverter(button -> {
            List<Integer> regras = new ArrayList<>();
            if(button == alterar) {
                regras.add(parseInt(txtDuracaoEmprestimo));
                regras.add(parseInt(txtDuracaoRenovacao));
                regras.add(parseInt(txtLimiteEmprestimos));
                regras.add(parseInt(txtLimiteRenovacoes));
                regras.add(parseInt(txtIntervaloEmprestimos));
            }
            return regras;
        });
    }
    
    private GridPane createContent() {
        Label lblDuracaoEmprestimo = new Label("Duração do empréstimo (dias): ");
        Label lblDuracaoRenovacao = new Label("Duração da renovação (dias): ");
        Label lblLimiteEmprestimosPorPessoa = new Label("Limite de empréstimos ativos por leitor: ");
        Label lblLimiteRenovacoes = new Label("Limite de renovações: ");
        Label lblIntervaloEntreEmprestimos = new Label("Intervalo entre empréstimos (dias): ");
        txtDuracaoEmprestimo = new TextField(parseString(duracaoEmprestimo));
        txtDuracaoRenovacao = new TextField(parseString(duracaoRenovacao));
        txtLimiteEmprestimos = new TextField(parseString(limiteEmprestimosPorPessoa));
        txtLimiteRenovacoes = new TextField(parseString(limiteRenovacoes));
        txtIntervaloEmprestimos = new TextField(parseString(intervaloEntreEmprestimos));
        
        GridPane content = new GridPane();
        content.setVgap(5);
        content.setHgap(5);
        content.add(lblDuracaoEmprestimo, 0, 0);
        content.add(lblDuracaoRenovacao, 0, 1);
        content.add(lblLimiteEmprestimosPorPessoa, 0, 2);
        content.add(lblLimiteRenovacoes, 0, 3);
        content.add(lblIntervaloEntreEmprestimos, 0, 4);
        content.add(txtDuracaoEmprestimo, 1, 0);
        content.add(txtDuracaoRenovacao, 1, 1);
        content.add(txtLimiteEmprestimos, 1, 2);
        content.add(txtLimiteRenovacoes, 1, 3);
        content.add(txtIntervaloEmprestimos, 1, 4);
        
        return content;
    }
    
    private int parseInt(TextField field) {
        return Integer.parseInt(field.getText());
    }
    
    private String parseString(int value) {
        return Integer.toString(value);
    }
}
