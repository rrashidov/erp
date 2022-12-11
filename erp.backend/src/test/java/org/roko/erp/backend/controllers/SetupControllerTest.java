package org.roko.erp.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.Setup;
import org.roko.erp.backend.services.SetupService;
import org.roko.erp.model.dto.SetupDTO;

public class SetupControllerTest {
    
    @Mock
    private SetupService svcMock;

    @Mock
    private Setup setupMock;

    @Mock
    private SetupDTO dtoMock;

    private SetupController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(svcMock.get()).thenReturn(setupMock);
        when(svcMock.fromDTO(dtoMock)).thenReturn(setupMock);

        controller = new SetupController(svcMock);
    }

    @Test
    public void get_delegatesToService() {
        controller.get();

        verify(svcMock).get();
        verify(svcMock).toDTO(setupMock);
    }

    @Test
    public void put_delegatesToService() {
        controller.put(dtoMock);

        verify(svcMock).update(setupMock);
    }
}
