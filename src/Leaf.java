package view;

import javax.swing.*;
import java.awt.*;

public class Leaf implements UIComponent {
    private JComponent component;

    public Leaf(JComponent component) {
        this.component = component;
    }

    @Override
    public JComponent render() {
        return component;
    }

    @Override
    public void setSize(Dimension size) {
        component.setPreferredSize(size);
        component.setMinimumSize(size);
        component.setMaximumSize(size);
    }
}
