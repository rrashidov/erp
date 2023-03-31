package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HealthcheckControllerTest {

    private HealthcheckController controller;

    @BeforeEach
    public void setup() {
        controller = new HealthcheckController();
    }

    @Test
    public void getReturnsProperResponse() {
        assertEquals("OK", controller.get());
    }
}
