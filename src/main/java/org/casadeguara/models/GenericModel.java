package org.casadeguara.models;

import javafx.collections.ObservableList;

public interface GenericModel<T> {
	int atualizar(T t);

	int cadastrar(T t);

	ObservableList<T> consultar(String column, int resultados);
}
