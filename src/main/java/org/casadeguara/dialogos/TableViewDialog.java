package org.casadeguara.dialogos;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.casadeguara.negocio.Cobranca;

import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

@SuppressWarnings("unchecked")
public class TableViewDialog extends Dialog<List<Cobranca>> {
	
	private TableView<Cobranca> table;
	
	public TableViewDialog(ObservableList<Cobranca> lista) {
		table = new TableView<Cobranca>(lista);
		
		TableColumn<Cobranca, String> leitor = new TableColumn<>("Leitor");
		TableColumn<Cobranca, String> email = new TableColumn<>("Email");
		TableColumn<Cobranca, String> tombo = new TableColumn<>("Tombo");
		TableColumn<Cobranca, String> titulo = new TableColumn<>("Titulo");
		TableColumn<Cobranca, Integer> numero = new TableColumn<>("N°");
		TableColumn<Cobranca, Date> dataEmprestimo = new TableColumn<>("Data empréstimo");
		TableColumn<Cobranca, Date> dataDevolucao = new TableColumn<>("Data devolução");
		TableColumn<Cobranca, Timestamp> cobranca = new TableColumn<>("Cobrança");
		
		vincularColunaAtributo(leitor, "leitor");
		vincularColunaAtributo(email, "email");
		vincularColunaAtributo(tombo, "tombo");
		vincularColunaAtributo(titulo, "titulo");
		vincularColunaAtributo(numero, "numero");
		vincularColunaAtributo(dataEmprestimo, "dataEmprestimo");
		vincularColunaAtributo(dataDevolucao, "dataDevolucao");
		vincularColunaAtributo(cobranca, "cobranca");
		
		table.getColumns().addAll(leitor, email, tombo, titulo, numero, dataEmprestimo, dataDevolucao, cobranca);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		DialogPane dialogPane = getDialogPane();
		dialogPane.setContent(table);
		dialogPane.getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CLOSE);
		
		setResizable(true);
		
		setResultConverter(button -> {
			if(button != null && button.equals(ButtonType.FINISH)) {
				ObservableList<Cobranca> cobrancas = table.getSelectionModel().getSelectedItems();
				return cobrancas.stream().filter(c -> c.getEmail() != null).collect(Collectors.toList());
			}
			return null;
		});
	}
	
	private void vincularColunaAtributo(TableColumn<?, ?> coluna, String atributo) {
        coluna.setCellValueFactory(new PropertyValueFactory<>(atributo));
    }
}
