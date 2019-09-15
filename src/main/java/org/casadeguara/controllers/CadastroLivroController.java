package org.casadeguara.controllers;

import java.util.List;
import java.util.Optional;
import org.casadeguara.dialogos.DialogoAdicionarExemplar;
import org.casadeguara.dialogos.DialogoListaItens;
import org.casadeguara.entidades.Exemplar;
import org.casadeguara.entidades.Livro;
import org.casadeguara.models.IdentificadorModel;
import org.casadeguara.models.LivroModel;
import org.casadeguara.views.CadastroLivroView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class CadastroLivroController implements GenericController{
    
    private CadastroLivroView view;
    private LivroModel model;
    private Livro livroAtual;
    
    private ObservableList<String> listaAutores;
    private ObservableList<Exemplar> listaExemplares;
    private ObservableList<String> listaPalavrasChave;
    private IdentificadorModel consultar;
    
    public CadastroLivroController(CadastroLivroView view, LivroModel model) {
        this.view = view;
        this.model = model;
        
        consultar = new IdentificadorModel();
        
        listaAutores = FXCollections.observableArrayList();
        listaExemplares = FXCollections.observableArrayList();
        listaPalavrasChave = FXCollections.observableArrayList();
        
        configureView();
    }
    
    @Override
    public void configureView() {
        view.acaoBtnAdicionarAutor(event -> adicionarAutores());
        view.acaoBtnAdicionarExemplar(event -> adicionarExemplares());
        view.acaoBtnAdicionarPalavra(event -> adicionarPalavrasChave());
        view.acaoBtnAlterar(event -> alterarLivro());
        view.acaoBtnCadastrar(event -> cadastrarLivro());
        view.acaoBtnLimpar(event -> limparCampos());
        view.acaoBtnSugerir(event -> sugerirTombo());
        view.acaoPesquisarLivro((observable, oldValue, newValue) -> pesquisarLivro(newValue));
        
        view.setListaSugestoesEditoras(model.getListaEditoras());
        view.setListaSugestoesLivros(model.getListaLivros());
    }
    
    public void adicionarAutores() {
        DialogoListaItens dialogoAdicionarAutor = new DialogoListaItens(listaAutores);
        dialogoAdicionarAutor.setListaSugestoes(model.getListaAutores());
        
        Optional<List<String>> resultado = dialogoAdicionarAutor.showAndWait();
        if(resultado.isPresent()) {
            listaAutores.setAll(resultado.get());
            view.quantidadeAutores(listaAutores.size());
        }
    }
    
    public void adicionarExemplares() {
        DialogoAdicionarExemplar dialogoAdicionarExemplar = new DialogoAdicionarExemplar(listaExemplares);
        dialogoAdicionarExemplar.showAndWait().ifPresent(lista -> {
            model.atualizar(lista);
            listaExemplares.setAll(lista);
            view.quantidadeExemplares(lista.size());
        });
    }
    
    public void adicionarPalavrasChave() {
        DialogoListaItens dialogoAdicionarAssunto = new DialogoListaItens(listaPalavrasChave);
        dialogoAdicionarAssunto.setListaSugestoes(model.getListaPalavras());
        
        Optional<List<String>> resultado = dialogoAdicionarAssunto.showAndWait();
        if(resultado.isPresent()) {
            listaPalavrasChave.setAll(resultado.get());
            view.quantidadePalavrasChave(listaPalavrasChave.size());
        }
    }

    public int alterarLivro() {
        String titulo = view.getTituloLivro();
        String tombo = view.getTomboLivro();
        String editora = view.getEditora();

        if (livroAtual != null && editora != null && !titulo.isEmpty() && !tombo.isEmpty()) {
            livroAtual.setTitulo(titulo);
            livroAtual.setTombo(tombo);
            livroAtual.setEditora(editora);
            
            Task<Void> atualizandoLivro = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    int ideditora = consultar.idEditora(editora);
                    
                    updateMessage("Atualizando as informações do livro.");
                    
                    if(model.atualizar(livroAtual, ideditora) == 0) {
                        int idlivro = livroAtual.getId();
                        updateMessage("Atualizando lista de autores.");
                        model.atualizarListaAutores(idlivro, listaAutores);
                        updateMessage("Atualizando lista de exemplares.");
                        model.atualizarListaExemplares(idlivro, listaExemplares);
                        updateMessage("Atualizando lista de palavras-chave.");
                        model.atualizarListaPalavrasChave(idlivro, listaPalavrasChave);
                        updateMessage("Atualizando lista de livros.");
                        model.atualizarListaLivros();
                        updateMessage("Livro atualizado com sucesso.");
                    } else {
                        updateMessage("Livro não foi atualizado. Verifique os dados e tente novamente.");
                        cancel();
                    }
                    return null;
                }
            };
            
            view.mensagemProgresso(atualizandoLivro);
            new Thread(atualizandoLivro).start();
            
            return 0;
        } else {
            view.mensagemInformativa("Informações obrigatórias não foram preenchidas.");
        }
        return 1;
    }
    
    public int cadastrarLivro() {
        String titulo = view.getTituloLivro();
        String tombo = view.getTomboLivro();
        String editora = view.getEditora();

        if (!titulo.isEmpty() && !tombo.isEmpty() && editora != null) {
            Livro novoLivro = new Livro(0, tombo, titulo, editora);
            
            if (!listaAutores.isEmpty() ||
                !listaExemplares.isEmpty() ||
                !listaPalavrasChave.isEmpty()) {
                
                Task<Void> cadastrandoLivro = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        int ideditora = consultar.idEditora(editora);

                        updateMessage("Cadastrando as informações do livro.");
                        int idlivro = model.cadastrar(novoLivro, ideditora);

                        if(idlivro > 0) {
                            updateMessage("Cadastrando lista de autores.");
                            model.atualizarListaAutores(idlivro, listaAutores);
                            updateMessage("Cadastrando lista de exemplares.");
                            model.atualizarListaExemplares(idlivro, listaExemplares);
                            updateMessage("Cadastrando lista de palavras-chave.");
                            model.atualizarListaPalavrasChave(idlivro, listaPalavrasChave);
                            updateMessage("Atualizando lista de livros.");
                            model.atualizarListaLivros();
                            updateMessage("Livro cadastrado com sucesso.");
                        } else {
                            updateMessage("Livro não cadastrado. Verifique se o tombo já está em uso.");
                            cancel();
                        }
                        return null;
                    }
                };

                view.mensagemProgresso(cadastrandoLivro);
                new Thread(cadastrandoLivro).start();
                
                return 0;
            } else {
                view.mensagemInformativa("Adicione ao menos 1 autor, 1 palavra-chave e 1 exemplar.");
            }
        } else {
            view.mensagemInformativa("Preencha todos os campos.");
        }
        return 1;
    }
    
    public void limparCampos() {
        livroAtual = null;
        listaAutores.clear();
        listaExemplares.clear();
        listaPalavrasChave.clear();
        view.limparCampos();
    }
    
    public void pesquisarLivro(String pesquisa) {
        if(pesquisa!= null && !pesquisa.isEmpty()) {
            livroAtual = model.consultar(pesquisa.substring(10));

            if (livroAtual != null) {
                view.estaCadastrando(false);

                view.setTituloLivro(livroAtual.getTitulo());
                view.setTomboLivro(livroAtual.getTombo());
                view.setEditora(livroAtual.getEditora());

                int idlivro = livroAtual.getId();
                listaAutores.setAll(model.consultarAutores(idlivro));
                listaExemplares.setAll(model.consultarExemplares(idlivro));
                listaPalavrasChave.setAll(model.consultarPalavrasChave(idlivro));

                view.quantidadeAutores(listaAutores.size());
                view.quantidadeExemplares(listaExemplares.size());
                view.quantidadePalavrasChave(listaPalavrasChave.size());
            } else {
                view.mensagemInformativa("Livro não encontrado.");
            }
        }
    }
    
    public void sugerirTombo() {
        view.setTomboLivro(model.consultarUltimoTombo());
    }
}
