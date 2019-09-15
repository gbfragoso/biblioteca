package org.casadeguara.listas;

import org.casadeguara.movimentacao.Item;
import javafx.collections.ObservableList;

public class DataSourceProvider {
    
    private DataSource listaAutores;
    private DataSource listaEditoras;
    private ListaExemplares listaExemplares;
    private DataSource listaLeitores;
    private ListaLivros listaLivros;
    private DataSource listaPalavras;
    private ListaUsuarios listaUsuarios;
    
    public DataSourceProvider() {
        listaAutores = new DataSource("select nome from autor order by nome");
        listaEditoras = new DataSource("select nome from editora order by nome");
        listaExemplares = new ListaExemplares();
        listaLeitores = new DataSource("select nome from leitor order by nome");
        listaLivros = new ListaLivros("select tombo, titulo from livro order by titulo");
        listaPalavras = new DataSource("select chave from keyword order by chave");
        listaUsuarios = new ListaUsuarios(null);
    }
    
    private void atualizar(DataSource data) {
        data.update();
    }
    
    public void atualizarListaAutores() {
        atualizar(listaAutores);
    }

    public void atualizarListaEditoras() {
        atualizar(listaEditoras);
    }
    
    public void atualizarListaExemplares() {
        listaExemplares.update();
    }
    
    public void atualizarListaLeitores() {
        atualizar(listaLeitores);
    }
    
    public void atualizarListaLivros() {
        atualizar(listaLivros);
    }
    
    public void atualizarListaPalavras() {
        atualizar(listaPalavras);
    }
    
    public void atualizarListaUsuarios() {
        atualizar(listaUsuarios);
    }
    
    private ObservableList<String> get(DataSource data) {
        return data.get();
    }
    
    public ObservableList<String> getListaAutores() {
        return get(listaAutores);
    }

    public ObservableList<String> getListaEditoras() {
        return get(listaEditoras);
    }
    
    public ObservableList<Item> getListaExemplares() {
        return listaExemplares.get();
    }
    
    public ObservableList<String> getListaLeitores() {
        return get(listaLeitores);
    }
    
    public ObservableList<String> getListaLivros() {
        return get(listaLivros);
    }
    
    public ObservableList<String> getListaPalavras() {
        return get(listaPalavras);
    }
    
    public ObservableList<String> getListaUsuarios() {
        return get(listaUsuarios);
    }

    public void removerExemplares(ObservableList<Item> exemplaresParaEmprestimo) {
        listaExemplares.remover(exemplaresParaEmprestimo);
    }
}
