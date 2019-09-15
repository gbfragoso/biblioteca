package org.casadeguara.testes;

import org.casadeguara.controllers.CadastroEditoraController;
import org.casadeguara.entidades.Editora;
import org.casadeguara.models.EditoraModel;
import org.casadeguara.views.CadastroEditoraView;
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
public class CadastroEditoraTest {
    
    @Mock
    private CadastroEditoraView view;
    
    @Mock
    private EditoraModel model;
    
    @InjectMocks
    private CadastroEditoraController cadastroEditoraController;
    
    @Test
    public void atualizarEditora() {
        cadastroEditoraController.setCurrentID(1);
        
        Editora novo = new Editora(1, "FEDERAÇÃO ESPÍRITA BRASILEIRA");
        when(view.getNomeEditora()).thenReturn("FEDERAÇÃO ESPÍRITA BRASILEIRA");
        when(model.atualizar(novo)).thenReturn(0);
        
        int exitStatus = cadastroEditoraController.atualizarEditora();
        
        verify(view, Mockito.atLeast(1)).getNomeEditora();
        
        assertEquals(0, exitStatus);
    }
    
    @Test
    public void atualizarEditoraIdZero() {
        cadastroEditoraController.setCurrentID(0);
        
        when(view.getNomeEditora()).thenReturn("FEDERAÇÃO ESPÍRITA BRASILEIRA");
        
        int exitStatus = cadastroEditoraController.atualizarEditora();
        
        verify(view, Mockito.atLeast(1)).getNomeEditora();
        
        assertEquals(1, exitStatus);
    }
    
    @Test
    public void atualizarEditoraNomeVazio() {
        cadastroEditoraController.setCurrentID(1);
        
        when(view.getNomeEditora()).thenReturn("");
        
        int exitStatus = cadastroEditoraController.atualizarEditora();
        
        verify(view, Mockito.atLeast(1)).getNomeEditora();
        
        assertEquals(1, exitStatus);
    }
    
    @Test
    public void cadastrarEditora() {
        Editora novo = new Editora(1, "FEDERAÇÃO ESPÍRITA BRASILEIRA");
        when(view.getNomeEditora()).thenReturn("FEDERAÇÃO ESPÍRITA BRASILEIRA");
        when(model.cadastrar(novo)).thenReturn(0);
        
        int exitStatus = cadastroEditoraController.cadastrarEditora();
        
        verify(view, Mockito.atLeast(1)).getNomeEditora();
        
        assertEquals(0, exitStatus);
    }
    
    @Test
    public void cadastrarEditoraNomeVazio() {
        when(view.getNomeEditora()).thenReturn("");
        
        int exitStatus = cadastroEditoraController.cadastrarEditora();
        
        verify(view, Mockito.atLeast(1)).getNomeEditora();
        
        assertEquals(1, exitStatus);
    }
    
    @Test
    public void pesquisarEditoraExistente() {
        String value = "FEDERAÇÃO ESPÍRITA BRASILEIRA";
        
        when(model.consultarEditora(value)).thenReturn(3);
        
        int exitStatus = cadastroEditoraController.pesquisarEditora(value);
        
        verify(model, Mockito.atLeast(1)).consultarEditora(value);
        
        assertEquals(0, exitStatus);
        assertEquals(3, cadastroEditoraController.getCurrentID());
    }
    
    @Test
    public void pesquisarEditoraInexistente() {
        String value = "Federação Espírita Brasileira";
        
        when(model.consultarEditora(value)).thenReturn(0);
        
        int exitStatus = cadastroEditoraController.pesquisarEditora(value);
        
        verify(model, Mockito.atLeast(1)).consultarEditora(value);
        
        assertEquals(1, exitStatus);
    }
}
