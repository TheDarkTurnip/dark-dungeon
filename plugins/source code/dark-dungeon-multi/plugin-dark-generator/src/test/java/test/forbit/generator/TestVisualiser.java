package test.forbit.generator;

import dev.forbit.generator.Visualiser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sun.awt.ExtendedKeyCodes;

import javax.swing.*;
import java.awt.*;

public class TestVisualiser {

    @Test @DisplayName("Test the Visualiser class") void testVisualiser() throws AWTException {
        Robot robot = new Robot();
        Visualiser.main(new String[]{"10", "nogui"});
    }
}
