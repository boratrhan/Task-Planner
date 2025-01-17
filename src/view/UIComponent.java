package view;

import javax.swing.*;
import java.awt.*;

public interface UIComponent {
    JComponent render();

    void setSize(Dimension size);
}
