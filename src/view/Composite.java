package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Composite implements UIComponent {
    private JPanel panel;
    private List<UIComponent> components;

    public Composite(LayoutManager layout) {
        this.panel = new JPanel(layout);
        this.components = new ArrayList<>();
    }

    public void addComponent(UIComponent component) {
        components.add(component);
        panel.add(component.render());
    }

    public void addComponent(UIComponent component, Object constraint) {
        components.add(component);
        panel.add(component.render(), constraint);
    }

    @Override
    public JComponent render() {
        return panel;
    }

    @Override
    public void setSize(Dimension size) {
        panel.setPreferredSize(size);
        panel.setMinimumSize(size);
        panel.setMaximumSize(size);
    }
}
