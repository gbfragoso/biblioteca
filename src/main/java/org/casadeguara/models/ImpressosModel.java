package org.casadeguara.models;

import org.casadeguara.listas.DataSourceProvider;
import javafx.collections.ObservableList;

/**
 * Esta classe gerencia as informações necessárias para impressão de relatórios.
 * @author Gustavo
 */
public class ImpressosModel {
    
    private final DataSourceProvider dataSource;

    public ImpressosModel(DataSourceProvider dataSource) {
        this.dataSource = dataSource;
    }
    
    public ObservableList<String> getListaAutores() {
        return dataSource.getListaAutores();
    }
    
    public ObservableList<String> getListaLeitores() {
        return dataSource.getListaLeitores();
    }
    
}
