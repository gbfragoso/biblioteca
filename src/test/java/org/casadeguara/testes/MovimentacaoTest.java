package org.casadeguara.testes;

import org.casadeguara.listas.DataSourceProvider;
import org.casadeguara.models.MovimentacaoModel;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MovimentacaoTest {
    
    @Mock
    private DataSourceProvider dataSource;
    
    @InjectMocks
    private MovimentacaoModel model;
}
