package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MainTest {

    @Test
    void testMainInitialization() {

        Main mainApp = new Main();
        assertNotNull(mainApp);

    }
}