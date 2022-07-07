package org.casadeguara.dialogos;

import java.util.ArrayList;
import java.util.List;

import org.casadeguara.componentes.AutoCompleteTextField;
import org.casadeguara.componentes.MaskedTextField;
import org.casadeguara.models.AcervoModel;
import org.casadeguara.movimentacao.Acervo;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DialogoAlterarEmprestimo extends Dialog<List<Integer>> {

	private MaskedTextField txtEmprestimo;
	private AutoCompleteTextField<Acervo> cbbExemplar;

	public DialogoAlterarEmprestimo(AcervoModel model) {
		setTitle("Alterar empréstimo ativo");
		setHeaderText("Coloque o ID do empréstimo que deve ser modificado e selecione o exemplar");

		DialogPane dialogPane = getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		dialogPane.setContent(createContent(model));

		setResultConverter(button -> {
			List<Integer> dados = new ArrayList<>();
			if (button == ButtonType.OK) {
				int idemprestimo = Integer.parseInt(txtEmprestimo.getPlainText());
				int idexemplar = cbbExemplar.getResult().getId();
				dados.add(idemprestimo);
				dados.add(idexemplar);
			}
			return dados;
		});

	}

	private VBox createContent(AcervoModel model) {
		Label lb1 = new Label("Id do empréstimo:");
		Label lb2 = new Label("Id do exemplar:");
		txtEmprestimo = new MaskedTextField("#####", ' ');
		cbbExemplar = new AutoCompleteTextField<>(model, 5);

		VBox content = new VBox(5);
		content.setAlignment(Pos.CENTER);
		content.getChildren().addAll(lb1, txtEmprestimo, lb2, cbbExemplar);

		return content;
	}
}
