package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.SetupDTO;
import org.springframework.web.client.RestTemplate;

public class SetupServiceTest {

    @Mock
    private SetupDTO setupMock;

    @Mock
    private RestTemplate restTemplateMock;

    private SetupService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new SetupServiceImpl(restTemplateMock);
    }

    @Test
    public void get_callsBackend() {
        svc.get();

        verify(restTemplateMock).getForObject("/api/v1/setup", SetupDTO.class);
    }

    @Test
    public void update_callsBackend() {
        svc.update(setupMock);

        verify(restTemplateMock).put("/api/v1/setup", setupMock);
    }
}
