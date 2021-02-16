package com.rolerandomizer;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.PluginPanel;
import net.runelite.api.Client;
import javax.management.relation.Role;
import javax.swing.*;
import javax.inject.Inject;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

@Slf4j
public class RoleRandomizerPanel extends PluginPanel {

    @Inject
    private Client client;

    private RoleRandomizerPlayerPanel playerPanel1;
    private RoleRandomizerPlayerPanel playerPanel2;
    private RoleRandomizerPlayerPanel playerPanel3;
    private RoleRandomizerPlayerPanel playerPanel4;
    private RoleRandomizerPlayerPanel playerPanel5;

    JButton randomizeButton;

    private JLabel baRoleRandomizerLabel;

    public String[] roles;

    RoleRandomizer rr;

    public RoleRandomizerPanel() {
        super();

        baRoleRandomizerLabel = new JLabel("BA Role Randomizer");
        baRoleRandomizerLabel.setSize(50, 25);
        baRoleRandomizerLabel.setForeground(Color.WHITE);
        this.add(baRoleRandomizerLabel);

        this.initializeAndAddPlayerPanels();

        rr = new RoleRandomizer();

        randomizeButton = new JButton("RANDOMIZE!");
        randomizeButton.setFocusPainted(false);
        this.add(randomizeButton);
    }

    public void setPreferencesAndUsernames() {
        HashMap<Integer, String> playerNames = new HashMap<Integer, String>();
        rr.setPlayerOnePreferences(playerPanel1.getPossibleRoles());
        rr.setPlayerTwoPreferences(playerPanel2.getPossibleRoles());
        rr.setPlayerThreePreferences(playerPanel3.getPossibleRoles());
        rr.setPlayerFourPreferences(playerPanel4.getPossibleRoles());
        rr.setPlayerFivePreferences(playerPanel5.getPossibleRoles());
        playerNames.put(0, playerPanel1.playerName);
        playerNames.put(1, playerPanel2.playerName);
        playerNames.put(2, playerPanel3.playerName);
        playerNames.put(3, playerPanel4.playerName);
        playerNames.put(4, playerPanel5.playerName);
        rr.setUsernames(playerNames);
        try {
            roles = rr.randomize();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
