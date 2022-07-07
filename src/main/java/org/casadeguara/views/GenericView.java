package org.casadeguara.views;

import org.casadeguara.alertas.Alerta;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Button;

public interface GenericView {

	Node getRoot();

	default void aplicarEstilo(Node node, String estilo) {
		node.getStyleClass().add(estilo);
	}

	default void configurarTamanhoBotao(Button button) {
		button.setPrefSize(130, 32);
	}

	default void mensagemInformativa(String mensagem) {
		new Alerta().informacao(mensagem);
	}

	default boolean mensagemConfirmacao(String mensagem) {
		return new Alerta().confirmacao(mensagem);
	}

	default void mensagemProgresso(Task<?> tarefa) {
		new Alerta().progresso(tarefa);
	}

	default void mensagemSucesso(String mensagem) {
		new Alerta().sucesso(mensagem);
	}
}
