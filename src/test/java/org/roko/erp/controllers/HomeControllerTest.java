package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HomeControllerTest {
    
    private static final String HOME_TEMPLATE_NAME = "index.html";
    
    private HomeController controller;

    @BeforeEach
    public void setup(){
        controller = new HomeController();
    }

    @Test
    public void homeReturnsProperTemplate(){
        String homeTemplate = controller.home();

        assertEquals(HOME_TEMPLATE_NAME, homeTemplate);
    }
}
