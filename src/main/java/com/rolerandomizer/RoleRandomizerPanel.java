package com.rolerandomizer;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.PluginPanel;

import javax.management.relation.Role;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

@Slf4j
public class RoleRandomizerPanel extends PluginPanel {

    private RoleRandomizerPlayerPanel playerPanel1;
    private RoleRandomizerPlayerPanel playerPanel2;
    private RoleRandomizerPlayerPanel playerPanel3;
    private RoleRandomizerPlayerPanel playerPanel4;
    private RoleRandomizerPlayerPanel playerPanel5;

    public RoleRandomizerPanel() {
        super();

        JLabel baRoleRandomizerLabel = new JLabel("BA Role Randomizer");
        baRoleRandomizerLabel.setSize(50, 25);
        baRoleRandomizerLabel.setForeground(Color.WHITE);
        this.add(baRoleRandomizerLabel);

        this.initializeAndAddPlayerPanels();

        JButton randomizeButton = new JButton("RANDOMIZE!");
        randomizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] availableRoles = new int[] {0, 1, 2, 3, 4};

                int roleSelectionPlayer1 = selectRole(playerPanel1, availableRoles);
                System.out.println("PLAYER 1 ROLE SELECTION: " + roleSelectionPlayer1);

                // Random role selected
                int roleSelectionPlayer2 = selectRole(playerPanel2, availableRoles);
                System.out.println("PLAYER 2 ROLE SELECTION: " + roleSelectionPlayer2);
                System.out.println("AVAILABLE ROLES LEFT: " + availableRoles[0] + " " + availableRoles[1] + " " + availableRoles[2] + " " + availableRoles[3] + " " + availableRoles[4]);

                int roleSelectionPlayer3 = selectRole(playerPanel3, availableRoles);
                System.out.println("PLAYER 3 ROLE SELECTION: " + roleSelectionPlayer3);
                System.out.println("AVAILABLE ROLES LEFT: " + availableRoles[0] + " " + availableRoles[1] + " " + availableRoles[2] + " " + availableRoles[3] + " " + availableRoles[4]);

                int roleSelectionPlayer4 = selectRole(playerPanel4, availableRoles);
                System.out.println("PLAYER 4 ROLE SELECTION: " + roleSelectionPlayer4);
                System.out.println("AVAILABLE ROLES LEFT: " + availableRoles[0] + " " + availableRoles[1] + " " + availableRoles[2] + " " + availableRoles[3] + " " + availableRoles[4]);

                int roleSelectionPlayer5 = selectRole(playerPanel5, availableRoles);
                System.out.println("PLAYER 5 ROLE SELECTION: " + roleSelectionPlayer5);
                System.out.println("AVAILABLE ROLES LEFT: " + availableRoles[0] + " " + availableRoles[1] + " " + availableRoles[2] + " " + availableRoles[3] + " " + availableRoles[4]);

            }

            private int selectRole(RoleRandomizerPlayerPanel playerPanel, int[] avaliableRoles) {
                int roleSelection;
                while (true) {
                    roleSelection = playerPanel.calculateRole();
                    if (avaliableRoles[roleSelection] != -1) {
                        avaliableRoles[roleSelection] = -1;
                        break;
                    } else {
                        roleSelection = playerPanel.calculateRole();
                    }
                }
                return roleSelection;
            }
        });
        randomizeButton.setFocusPainted(false);
        this.add(randomizeButton);
    }

    private void initializeAndAddPlayerPanels() {
        this.playerPanel1 = new RoleRandomizerPlayerPanel(1);
        this.add(playerPanel1);
        this.playerPanel2 = new RoleRandomizerPlayerPanel(2);
        this.add(playerPanel2);
        this.playerPanel3 = new RoleRandomizerPlayerPanel(3);
        this.add(playerPanel3);
        this.playerPanel4 = new RoleRandomizerPlayerPanel(4);
        this.add(playerPanel4);
        this.playerPanel5 = new RoleRandomizerPlayerPanel(5);
        this.add(playerPanel5);
    }
}
