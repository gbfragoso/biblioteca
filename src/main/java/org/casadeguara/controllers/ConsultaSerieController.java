package org.casadeguara.controllers;

import org.casadeguara.consultas.ConsultaSerie;
import org.casadeguara.models.ConsultaSerieModel;
import org.casadeguara.views.ConsultaSerieView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConsultaSerieController implements GenericController {

	private ConsultaSerieView view;
	private ConsultaSerieModel model;

	public ConsultaSerieController(ConsultaSerieView view) {
		this.view = view;
		this.model = new ConsultaSerieModel();

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

	private void verificarResultados(ObservableList<ConsultaSerie> resultados) {
		if (!resultados.isEmpty()) {
			view.setResultadosConsulta(resultados);
		} else {
			view.setResultadosConsulta(FXCollections.emptyObservableList());
		}
	}
}
