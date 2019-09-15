package org.casadeguara.testes;

import org.casadeguara.controllers.CadastroAutorController;
import org.casadeguara.entidades.Autor;
import org.casadeguara.listas.DataSourceProvider;
import org.casadeguara.models.AutorModel;
import org.casadeguara.views.CadastroAutorView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CadastroAutorTest {
    
    @Mock
    private CadastroAutorView view;
    
    @Mock
    private AutorModel model;
    
    @Mock
    private DataSourceProvider dataSource;
    
    @InjectMocks
    private CadastroAutorController cadastroAutorController;
    
    @Test
    public void atualizarAutor() {
        cadastroAutorController.setAutorAtual(1);
        
        Autor novo = new Autor(1, "GUSTAVO BONFIM FRAGOSO");
        when(view.getNomeAutor()).thenReturn("GUSTAVO BONFIM FRAGOSO");
        when(model.atualizar(novo)).thenReturn(0);
        
        int exitStatus = cadastroAutorController.atualizarAutor();
        
        verify(view, Mockito.atLeast(1)).getNomeAutor();
        
        assertEquals(0, exitStatus);
    }
    
    @Test
    public void atualizarAutorIdZero() {
        cadastroAutorController.setAutorAtual(0);
        
        when(view.getNomeAutor()).thenReturn("GUSTAVO BONFIM FRAGOSO");
        
        int exitStatus = cadastroAutorController.atualizarAutor();
        
        verify(view, Mockito.atLeast(1)).getNomeAutor();
        
        assertEquals(1, exitStatus);
    }
    
    @Test
    public void atualizarAutorNomeVazio() {
        cadastroAutorController.setAutorAtual(1);
        
        when(view.getNomeAutor()).thenReturn("");
        
        int exitStatus = cadastroAutorController.atualizarAutor();
        
        verify(view, Mockito.atLeast(1)).getNomeAutor();
        
        assertEquals(1, exitStatus);
    }
    
    @Test
    public void cadastrarAutor() {
        Autor novo = new Autor(1, "GUSTAVO BONFIM FRAGOSO");
        when(view.getNomeAutor()).thenReturn("GUSTAVO BONFIM FRAGOSO");
        when(model.cadastrar(novo)).thenReturn(0);
        
        int exitStatus = cadastroAutorController.cadastrarAutor();
        
        verify(view, Mockito.atLeast(1)).getNomeAutor();
        
        assertEquals(0, exitStatus);
    }
    
    @Test
    public void cadastrarAutorNomeVazio() {
        when(view.getNomeAutor()).thenReturn("");
        
        int exitStatus = cadastroAutorController.cadastrarAutor();
        
        verify(view, Mockito.atLeast(1)).getNomeAutor();
        
        assertEquals(1, exitStatus);
    }
    
    @Test
    public void pesquisarAutorExistente() {
        String value = "EMMANUEL (ESPÍRITO)";
        
        when(model.consultarAutor(value)).thenReturn(3);
        
        int exitStatus = cadastroAutorController.pesquisarAutor(value);
        
        verify(model, Mockito.atLeast(1)).consultarAutor(value);
        
        assertEquals(0, exitStatus);
        assertEquals(3, cadastroAutorController.getAutorAtual());
    }
    
    @Test
    public void pesquisarAutorInexistente() {
        String value = "GUSTAVO BONFIM FRAGOSO";
        
        when(model.consultarAutor(value)).thenReturn(0);
        
        int exitStatus = cadastroAutorController.pesquisarAutor(value);
        
        verify(model, Mockito.atLeast(1)).consultarAutor(value);
        
        assertEquals(1, exitStatus);
    }
}
