package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.CodeSerie;
import org.roko.erp.model.Setup;
import org.roko.erp.repositories.SetupRepository;

public class SetupServiceTest {

    @Mock
    private CodeSerie codeSerieMock;

    @Mock
    private Setup setupMock;

    @Mock
    private SetupRepository repoMock;

    private SetupService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(setupMock.getSalesOrderCodeSerie()).thenReturn(codeSerieMock);

        when(repoMock.findById("")).thenReturn(Optional.of(setupMock));

        svc = new SetupServiceImpl(repoMock);
    }

    @Test
    public void getReturnsSetup_ifItExists(){
        Setup setup = svc.get();

        assertEquals(setupMock, setup);
    }

    @Test
    public void getCreatesNewSetup_ifItDoesNotExist(){
        when(repoMock.findById("")).thenReturn(Optional.empty());

        Setup setup = svc.get();

        assertNull(setup.getSalesOrderCodeSerie());

        verify(repoMock).save(setup);
    }

    @Test
    public void update_savesSetupToDB(){
        svc.update(setupMock);

        verify(repoMock).save(setupMock);
    }
}
