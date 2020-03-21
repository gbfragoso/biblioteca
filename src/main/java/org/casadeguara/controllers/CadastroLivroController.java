package org.casadeguara.controllers;

import java.util.List;
import java.util.Optional;

import org.casadeguara.alertas.Alerta;
import org.casadeguara.dialogos.DialogoAdicionarExemplar;
import org.casadeguara.dialogos.ListViewDialog;
import org.casadeguara.entidades.Autor;
import org.casadeguara.entidades.Editora;
import org.casadeguara.entidades.Exemplar;
import org.casadeguara.entidades.Livro;
import org.casadeguara.entidades.PalavraChave;
import org.casadeguara.models.AutorModel;
import org.casadeguara.models.EditoraModel;
import org.casadeguara.models.LivroModel;
import org.casadeguara.models.PalavraChaveModel;
import org.casadeguara.views.CadastroLivroView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

public class CadastroLivroController implements GenericController{
    
    private CadastroLivroView view;
    private LivroModel model;
    private Livro livroAtual;
    
    private ObservableList<Autor> listaAutores;
    private ObservableList<Exemplar> listaExemplares;
    private ObservableList<PalavraChave> listaPalavrasChave;
    
    public CadastroLivroController(CadastroLivroView view) {
        this.view = view;
        this.model = new LivroModel();
        
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
        view.acaoPesquisarLivro(event -> pesquisarLivro());
        
        view.setAutoCompleteEditora(new EditoraModel());
        view.setAutoCompleteLivro(model);
    }
    
    public void adicionarAutores() {
        ListViewDialog<Autor> dialogoAdicionarAutor = new ListViewDialog<>(new AutorModel(), listaAutores);
        
        Optional<List<Autor>> resultado = dialogoAdicionarAutor.showAndWait();
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
        ListViewDialog<PalavraChave> dialogoAdicionarAssunto = new ListViewDialog<>(new PalavraChaveModel(), listaPalavrasChave);
        
        Optional<List<PalavraChave>> resultado = dialogoAdicionarAssunto.showAndWait();
        if(resultado.isPresent()) {
            listaPalavrasChave.setAll(resultado.get());
            view.quantidadePalavrasChave(listaPalavrasChave.size());
        }
    }

    public int alterarLivro() {
        String titulo = view.getTituloLivro();
        String tombo = view.getTomboLivro();
        Editora editora = view.getEditora();

        Livro livro = getLivroAtual();
        
		if (livro != null && editora != null && !titulo.isEmpty() && !tombo.isEmpty()) {
            livro.setTitulo(titulo);
            livro.setTombo(tombo);
            livro.setEditora(editora);
            
            Task<Void> atualizandoLivro = new Task<Void>() {

                @Override
                protected Void call() throws Exception {
                    updateMessage("Atualizando as informações do livro.");
                    
                    if(model.atualizar(livro) == 0) {
                        int idlivro = livro.getId();
                        updateMessage("Atualizando lista de autores.");
                        model.atualizarListaAutores(idlivro, listaAutores);
                        updateMessage("Atualizando lista de exemplares.");
                        model.atualizarListaExemplares(idlivro, listaExemplares);
                        updateMessage("Atualizando lista de palavras-chave.");
                        model.atualizarListaPalavrasChave(idlivro, listaPalavrasChave);
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
        Editora editora = view.getEditora();
        
        if(model.verificaTombo(tombo)) {
        	view.mensagemInformativa("Tombo em uso por outro livro");
        	return 1;
        }

        if (!titulo.isEmpty() && !tombo.isEmpty() && editora != null) {
            Livro novoLivro = new Livro(0, tombo, titulo);
            novoLivro.setEditora(editora);
            
            if (!listaAutores.isEmpty() ||
                !listaExemplares.isEmpty() ||
                !listaPalavrasChave.isEmpty()) {
                
                Task<Void> cadastrandoLivro = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        updateMessage("Cadastrando as informações do livro.");
                        int idlivro = model.cadastrar(novoLivro);

                        if(idlivro > 0) {
                            updateMessage("Cadastrando lista de autores.");
                            model.atualizarListaAutores(idlivro, listaAutores);
                            updateMessage("Cadastrando lista de exemplares.");
                            model.atualizarListaExemplares(idlivro, listaExemplares);
                            updateMessage("Cadastrando lista de palavras-chave.");
                            model.atualizarListaPalavrasChave(idlivro, listaPalavrasChave);
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
        setLivroAtual(null);
        listaAutores.clear();
        listaExemplares.clear();
        listaPalavrasChave.clear();
        view.limparCampos();
    }
    
    public void pesquisarLivro() {
    	Livro livro = view.getTermoPesquisado();
    	
        if(livro!= null) {
        	setLivroAtual(livro);
            
            view.estaCadastrando(false);

            view.setTituloLivro(livro.getTitulo());
            view.setTomboLivro(livro.getTombo());
            view.setEditora(livro.getEditora());

            int idlivro = livro.getId();
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
    
    public void sugerirTombo() {
        view.setTomboLivro(model.consultarUltimoTombo());
    }

	public Livro getLivroAtual() {
		return livroAtual;
	}

	public void setLivroAtual(Livro livroAtual) {
		this.livroAtual = livroAtual;
	}
}
