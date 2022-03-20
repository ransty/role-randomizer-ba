package com.rolerandomizer.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.FlatTextField;

import com.rolerandomizer.RoleRandomizer;

public class RoleRandomizerPanel extends JPanel implements ActionListener {

        private final RoleRandomizerPluginPanel panel;

    public final JTextField uiFieldPlayer1;
    public final JTextField uiFieldPlayer1Preferences;
    public final JTextField uiFieldPlayer2;
    public final JTextField uiFieldPlayer2Preferences;
    public final JTextField uiFieldPlayer3;
    public final JTextField uiFieldPlayer3Preferences;
    public final JTextField uiFieldPlayer4;
    public final JTextField uiFieldPlayer4Preferences;
    public final JTextField uiFieldPlayer5;
    public final JTextField uiFieldPlayer5Preferences;

    private RoleRandomizer rr;


    private JButton roleRandomizerButton;

        protected RoleRandomizerPanel(RoleRandomizerPluginPanel panel)
        {
            super();

            this.panel = panel;

            setLayout(new GridLayout(6, 2, 7, 7));
            uiFieldPlayer1 = addComponent("Player 1");
            uiFieldPlayer1Preferences = addComponent("Preferences");
            uiFieldPlayer2 = addComponent("Player 2");
            uiFieldPlayer2Preferences = addComponent("Preferences");
            uiFieldPlayer3 = addComponent("Player 3");
            uiFieldPlayer3Preferences = addComponent("Preferences");
            uiFieldPlayer4 = addComponent("Player 4");
            uiFieldPlayer4Preferences = addComponent("Preferences");
            uiFieldPlayer5 = addComponent("Player 5");
            uiFieldPlayer5Preferences = addComponent("Preferences");

            addResetButtonComponent("Reset");
            addButtonComponent("Randomize!");

            rr = new RoleRandomizer();

        }

    private JTextField addComponent(String label)
    {
        final JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        final JLabel uiLabel = new JLabel(label);
        final FlatTextField uiInput = new FlatTextField();

        uiInput.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        uiInput.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
        uiInput.setBorder(new EmptyBorder(5, 7, 5, 7));

        uiLabel.setFont(FontManager.getRunescapeSmallFont());
        uiLabel.setBorder(new EmptyBorder(0, 0, 4, 0));
        uiLabel.setForeground(Color.WHITE);

        container.add(uiLabel, BorderLayout.NORTH);
        container.add(uiInput, BorderLayout.CENTER);

        add(container);

        return uiInput.getTextField();
    }

    private void addResetButtonComponent(String label)
    {
        final JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        JButton resetButton = new JButton(label);

        resetButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        resetButton.setBorder(new EmptyBorder(5, 7, 5, 7));
        resetButton.setToolTipText("This resets preferences!");

        resetButton.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    resetPreferences();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    resetPreferences();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    resetPreferences();
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetPreferences();
            }
        });

        container.add(resetButton, BorderLayout.CENTER);
        add(container, BorderLayout.NORTH);
    }

    private void resetPreferences() {
        panel.resultPanel.roleRandomizerResultField.setText("");
        panel.resultPanel.roleRandomizerResultField.insert("\n", 0);

        uiFieldPlayer1Preferences.setText("");
        uiFieldPlayer2Preferences.setText("");
        uiFieldPlayer3Preferences.setText("");
        uiFieldPlayer4Preferences.setText("");
        uiFieldPlayer5Preferences.setText("");

        rr.playerOnePreferences = null;
        rr.playerTwoPreferences = null;
        rr.playerThreePreferences = null;
        rr.playerFourPreferences = null;
        rr.playerFivePreferences = null;

    }

    private void addButtonComponent(String label)
    {
        final JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        roleRandomizerButton = new JButton(label);

        roleRandomizerButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        roleRandomizerButton.setBorder(new EmptyBorder(5, 7, 5, 7));

        roleRandomizerButton.addActionListener(this);
        roleRandomizerButton.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    randomize();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        container.add(roleRandomizerButton, BorderLayout.CENTER);

        add(container, BorderLayout.SOUTH);
    }

    public void getUsernames() {
        HashMap<Integer, String> usernames = new HashMap<>();
        usernames.put(0, uiFieldPlayer1.getText());
        usernames.put(1, uiFieldPlayer2.getText());
        usernames.put(2, uiFieldPlayer3.getText());
        usernames.put(3, uiFieldPlayer4.getText());
        usernames.put(4, uiFieldPlayer5.getText());
        rr.setUsernames(usernames);
    }

    public void getPreferences() {
        String regex = "[^a-zA-Z0-9]";
        String[] player1Prefs = new String[]{uiFieldPlayer1Preferences.getText().replaceAll(regex, "")};
        rr.setPlayerOnePreferences(convertPreferences(player1Prefs));

        String[] player2Prefs = new String[]{uiFieldPlayer2Preferences.getText().replaceAll(regex, "")};
        rr.setPlayerTwoPreferences(convertPreferences(player2Prefs));

        String[] player3Prefs = new String[]{uiFieldPlayer3Preferences.getText().replaceAll(regex, "")};
        rr.setPlayerThreePreferences(convertPreferences(player3Prefs));

        String[] player4Prefs = new String[]{uiFieldPlayer4Preferences.getText().replaceAll(regex, "")};
        rr.setPlayerFourPreferences(convertPreferences(player4Prefs));

        String[] player5Prefs = new String[]{uiFieldPlayer5Preferences.getText().replaceAll(regex, "")};
        rr.setPlayerFivePreferences(convertPreferences(player5Prefs));
    }

    public void randomize() {
        panel.resultPanel.roleRandomizerResultField.setText("");
        panel.resultPanel.roleRandomizerResultField.insert("\n", 0);

        getUsernames();
        if (!rr.isPreferencesSet()) {
            getPreferences();
        }

        try {
            String[] roles = rr.randomize();

            for (int index = 0; index < 5; index++) {
                    switch (index) {
                        case 0:
                            panel.resultPanel.roleRandomizerResultField.insert("              " +
                                    "Main: " + roles[index] + "\n", 1);
                            for (Map.Entry<Integer, String> entry: rr.usernames.entrySet()) {
                                if (entry.getValue() == roles[index]) {
                                    rr.popPreference(entry.getKey(), index);
                                }
                            }
                            break;
                        case 1:
                            panel.resultPanel.roleRandomizerResultField.append("              " +
                                    "Second: " + roles[index] + "\n");
                            for (Map.Entry<Integer, String> entry: rr.usernames.entrySet()) {
                                if (entry.getValue() == roles[index]) {
                                    rr.popPreference(entry.getKey(), index);
                                }
                            }
                            break;
                        case 2:
                            panel.resultPanel.roleRandomizerResultField.append("              " +
                                    "Healer: " + roles[index] + "\n");
                            for (Map.Entry<Integer, String> entry: rr.usernames.entrySet()) {
                                if (entry.getValue() == roles[index]) {
                                    rr.popPreference(entry.getKey(), index);
                                }
                            }
                            break;
                        case 3:
                            panel.resultPanel.roleRandomizerResultField.append("              " +
                                    "Collector: " + roles[index] + "\n");
                            for (Map.Entry<Integer, String> entry: rr.usernames.entrySet()) {
                                if (entry.getValue() == roles[index]) {
                                    rr.popPreference(entry.getKey(), index);
                                }
                            }
                            break;
                        case 4:
                            panel.resultPanel.roleRandomizerResultField.append("              " +
                                    "Defender: " + roles[index]);
                            for (Map.Entry<Integer, String> entry: rr.usernames.entrySet()) {
                                if (entry.getValue() == roles[index]) {
                                    rr.popPreference(entry.getKey(), index);
                                }
                            }
                            break;
                    }
            }

        } catch (Exception ex) {
            panel.resultPanel.roleRandomizerResultField.setText("All roles have been played at least once");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        randomize();
    }

    private int[] convertPreferences(String[] prefs)
    {
        int[] newPrefs = {0, 0, 0, 0, 0};
        for (String s : prefs) {
            if (s.equalsIgnoreCase("fill"))
            {
                return new int[]{1, 1, 1, 1, 1};
            }
            for (int i = 0; i < s.length(); i++)
            {
                char c = s.charAt(i);
                switch (c) {
                    case 'a':
                        newPrefs[0] = 1;
                        newPrefs[1] = 1;
                        break;
                    case 'm':
                        newPrefs[0] = 1;
                        break;
                    case '2':
                        newPrefs[1] = 1;
                        break;
                    case 'h':
                        newPrefs[2] = 1;
                        break;
                    case 'c':
                        newPrefs[3] = 1;
                        break;
                    case 'd':
                        newPrefs[4] = 1;
                        break;
                }
            }
        }
        return newPrefs;
    }
}
