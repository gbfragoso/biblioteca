package org.casadeguara.controllers;

import org.casadeguara.consultas.ConsultaEmprestimo;
import org.casadeguara.models.ConsultaEmprestimoModel;
import org.casadeguara.views.ConsultaEmprestimoView;

import javafx.collections.ObservableList;

public class ConsultaEmprestimoController implements GenericController {

	private ConsultaEmprestimoView view;
	private ConsultaEmprestimoModel model;

	public ConsultaEmprestimoController(ConsultaEmprestimoView view) {
		this.view = view;
		this.model = new ConsultaEmprestimoModel();

		configureView();
	}

	@Override
	public void configureView() {
		view.acaoPesquisar(event -> pesquisar());
	}

	public void pesquisar() {
		String termoPesquisado = view.getTermoPesquisado();
		verificarResultados(model.pesquisar("%" + termoPesquisado + "%"));
	}

	private void verificarResultados(ObservableList<ConsultaEmprestimo> resultados) {
		if (!resultados.isEmpty()) {
			view.setResultadosConsulta(resultados);
		}
	}
}
