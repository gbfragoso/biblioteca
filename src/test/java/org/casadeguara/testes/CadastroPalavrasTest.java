package org.casadeguara.testes;

import org.casadeguara.controllers.CadastroPalavrasController;
import org.casadeguara.entidades.PalavraChave;
import org.casadeguara.models.PalavraChaveModel;
import org.casadeguara.views.CadastroPalavrasView;
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
public class CadastroPalavrasTest {
    
    @Mock
    private CadastroPalavrasView view;
    
    @Mock
    private PalavraChaveModel model;
    
    @InjectMocks
    private CadastroPalavrasController cadastroPalavrasController;
    
    @Test
    public void atualizarPalavraChave() {
        cadastroPalavrasController.setCurrentID(1);
        
        PalavraChave novo = new PalavraChave(1, "AMOR");
        when(view.getPalavraChave()).thenReturn("AMOR");
        when(model.atualizar(novo)).thenReturn(0);
        
        int exitStatus = cadastroPalavrasController.atualizarPalavraChave();
        
        verify(view, Mockito.atLeast(1)).getPalavraChave();
        
        assertEquals(0, exitStatus);
    }
    
    @Test
    public void atualizarPalavrasIdZero() {
        cadastroPalavrasController.setCurrentID(0);
        
        when(view.getPalavraChave()).thenReturn("AMOR");
        
        int exitStatus = cadastroPalavrasController.atualizarPalavraChave();
        
        verify(view, Mockito.atLeast(1)).getPalavraChave();
        
        assertEquals(1, exitStatus);
    }
    
    @Test
    public void atualizarPalavrasNomeVazio() {
        cadastroPalavrasController.setCurrentID(1);
        
        when(view.getPalavraChave()).thenReturn("");
        
        int exitStatus = cadastroPalavrasController.atualizarPalavraChave();
        
        verify(view, Mockito.atLeast(1)).getPalavraChave();
        
        assertEquals(1, exitStatus);
    }
    
    @Test
    public void cadastrarPalavraChave() {
        PalavraChave novo = new PalavraChave(1, "AMOR");
        when(view.getPalavraChave()).thenReturn("AMOR");
        when(model.cadastrar(novo)).thenReturn(1);
        
        int exitStatus = cadastroPalavrasController.cadastrarPalavraChave();
        
        verify(view, Mockito.atLeast(1)).getPalavraChave();
        
        assertEquals(0, exitStatus);
    }
    
    @Test
    public void cadastrarPalavrasNomeVazio() {
        when(view.getPalavraChave()).thenReturn("");
        
        int exitStatus = cadastroPalavrasController.cadastrarPalavraChave();
        
        verify(view, Mockito.atLeast(1)).getPalavraChave();
        
        assertEquals(1, exitStatus);
    }
    
    @Test
    public void pesquisarPalavrasExistente() {
        String value = "AMOR";
        
        when(model.consultarPalavraChave(value)).thenReturn(3);
        
        int exitStatus = cadastroPalavrasController.pesquisarPalavraChave(value);
        
        verify(model, Mockito.atLeast(1)).consultarPalavraChave(value);
        
        assertEquals(0, exitStatus);
        assertEquals(3, cadastroPalavrasController.getCurrentID());
    }
    
    @Test
    public void pesquisarPalavrasInexistente() {
        String value = "Federação Espírita Brasileira";
        
        when(model.consultarPalavraChave(value)).thenReturn(0);
        
        int exitStatus = cadastroPalavrasController.pesquisarPalavraChave(value);
        
        verify(model, Mockito.atLeast(1)).consultarPalavraChave(value);
        
        assertEquals(1, exitStatus);
    }
}

