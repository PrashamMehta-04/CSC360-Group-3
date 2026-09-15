package com.csc360;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testAppInstantiation() {
        App app = new App();
        assertNotNull(app);
    }
}
