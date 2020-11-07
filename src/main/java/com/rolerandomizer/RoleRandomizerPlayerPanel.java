package com.rolerandomizer;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Math;
import java.util.Arrays;
import java.util.Random;

@Slf4j
public class RoleRandomizerPlayerPanel extends PluginPanel {

    private static final ColorUIResource DEFAULT_BACKGROUND = new ColorUIResource(192, 192, 192);
    private JButton[] buttons;

    public int playerNumber;
    public String playerName;

    public boolean mainAttack;
    public boolean secondAttack;
    public boolean healer;
    public boolean collector;
    public boolean defender;

    public RoleRandomizerPlayerPanel(int playerNumber) {
        super();
        this.playerNumber = playerNumber;
        this.playerName = "";
        this.mainAttack = false;
        this.secondAttack = false;
        this.healer = false;
        this.collector = false;
        this.defender = false;

        JPanel playerPanel = new JPanel();
        playerPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        playerPanel.setLayout(new GridBagLayout());

        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;

        JLabel playerLabel = new JLabel("Player " + this.playerNumber + " Name");
        playerLabel.setPreferredSize(new Dimension(25, 20));
        this.add(playerLabel);
        c.gridx++;
        JTextField playerTextField = new JTextField();
        playerTextField.setPreferredSize(new Dimension(25, 20));
        this.add(playerTextField);
        c.gridy++;

        c.gridx = 0;
        buttons = this.generatePreferenceButtons(playerPanel, c);

        this.add(playerPanel);
    }

    public int[] getPossibleRoles() {
        int[] possibleRoles = new int[] {-1, -1, -1, -1, -1};
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getBackground().equals(Color.GREEN)) {
                possibleRoles[i] = i;
            }
        }
        return possibleRoles;
    }

    public int calculateRole() {
        int[] possibleRoles = this.getPossibleRoles();
        int rnd = new Random().nextInt(possibleRoles.length);
        return possibleRoles[rnd];
    }

    private JButton[] generatePreferenceButtons(JPanel panel, GridBagConstraints constraints) {
        int gridx = constraints.gridx;
        final JButton[] buttons = new JButton[5];
        String[] names = {"A", "2", "H", "C", "D"};
        for (int i = 0; i < buttons.length; i++) {
            int tempButtonId = i;
            buttons[i] = new JButton(names[i]);
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (buttons[tempButtonId].getBackground().equals(DEFAULT_BACKGROUND)) {
                        buttons[tempButtonId].setBackground(Color.GREEN);
                    } else {
                        buttons[tempButtonId].setBackground(DEFAULT_BACKGROUND);
                    }
                    buttons[tempButtonId].setFocusPainted(false);
                }
            });
            panel.add(buttons[i], constraints);
            constraints.gridx++;
        }
        // we have finished with our row, grow y
        constraints.gridy++;
        // reset constraints gridx
        constraints.gridx = gridx;
        return buttons;
    }
}
