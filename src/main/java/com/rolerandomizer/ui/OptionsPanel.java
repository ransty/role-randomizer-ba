package com.rolerandomizer.ui;

import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class OptionsPanel extends JPanel {

    private final RoleRandomizerPluginPanel panel;
    private final RoleRandomizerPanel main;
    private static final ImageIcon ON_SWITCH;
    private static final ImageIcon OFF_SWITCH;

    static
    {
        BufferedImage onSwitch = ImageUtil.loadImageResource(OptionsPanel.class, "green_egg.png");
        onSwitch = ImageUtil.resizeImage(onSwitch, 15, 20);
        BufferedImage offSwitch = ImageUtil.loadImageResource(OptionsPanel.class, "red_egg.png");
        offSwitch = ImageUtil.resizeImage(offSwitch, 15, 20);
        ON_SWITCH = new ImageIcon(onSwitch);
        OFF_SWITCH = new ImageIcon(offSwitch);
    }

    private void updateKeepRoleConfig(JToggleButton button)
    {
        button.setToolTipText(button.isSelected() ? "Disable" : "Enable");
        if (button.isSelected()) {
            main.keepPreviousRolesSelected = true;
        } else {
            main.keepPreviousRolesSelected = false;
        }
    }

    protected OptionsPanel(RoleRandomizerPluginPanel panel, RoleRandomizerPanel main)
    {
        super();

        this.panel = panel;
        this.main = main;
        setLayout(new BorderLayout());

        final JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBorder(new EmptyBorder(0, 5, 5, 5));

        JToggleButton button = new JToggleButton(OFF_SWITCH);
        button.setSelectedIcon(ON_SWITCH);
        button.setPreferredSize(new Dimension(25, 50));
        SwingUtil.removeButtonDecorations(button);
        button.addItemListener(l -> updateKeepRoleConfig(button));

        JLabel optionLabel = new JLabel("Keep previous roles selected");
        optionLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
        container.add(optionLabel, BorderLayout.WEST);
        container.add(button, BorderLayout.EAST);

        add(container, BorderLayout.SOUTH);
    }
}
