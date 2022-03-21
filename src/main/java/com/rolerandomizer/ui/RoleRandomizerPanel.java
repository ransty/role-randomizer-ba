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
    public final JCheckBox[] uiFieldPlayer1Preferences;
    public final JTextField uiFieldPlayer2;
    public final JCheckBox[] uiFieldPlayer2Preferences;
    public final JTextField uiFieldPlayer3;
    public final JCheckBox[] uiFieldPlayer3Preferences;
    public final JTextField uiFieldPlayer4;
    public final JCheckBox[] uiFieldPlayer4Preferences;
    public final JTextField uiFieldPlayer5;
    public final JCheckBox[] uiFieldPlayer5Preferences;

    public boolean[] initialPlayer1Preferences;
    public boolean isInitialPlayer1PreferencesSet;
    public boolean[] initialPlayer2Preferences;
    public boolean isInitialPlayer2PreferencesSet;
    public boolean[] initialPlayer3Preferences;
    public boolean isInitialPlayer3PreferencesSet;
    public boolean[] initialPlayer4Preferences;
    public boolean isInitialPlayer4PreferencesSet;
    public boolean[] initialPlayer5Preferences;
    public boolean isInitialPlayer5PreferencesSet;

    private RoleRandomizer rr;


    private JButton roleRandomizerButton;

        protected RoleRandomizerPanel(RoleRandomizerPluginPanel panel)
        {
            super();

            this.panel = panel;

            setLayout(new GridLayout(6, 2, 7, 7));
            uiFieldPlayer1 = addComponent("Player 1");
            uiFieldPlayer1Preferences = generatePlayerPreferences();
            uiFieldPlayer1Preferences[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    flipCheckboxes(uiFieldPlayer1Preferences, uiFieldPlayer1Preferences[5]);
                }
            });
            initialPlayer1Preferences = new boolean[6];
            isInitialPlayer1PreferencesSet = false;

            uiFieldPlayer2 = addComponent("Player 2");
            uiFieldPlayer2Preferences = generatePlayerPreferences();
            uiFieldPlayer2Preferences[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    flipCheckboxes(uiFieldPlayer2Preferences, uiFieldPlayer2Preferences[5]);
                }
            });
            initialPlayer2Preferences = new boolean[6];
            isInitialPlayer2PreferencesSet = false;

            uiFieldPlayer3 = addComponent("Player 3");
            uiFieldPlayer3Preferences = generatePlayerPreferences();
            uiFieldPlayer3Preferences[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    flipCheckboxes(uiFieldPlayer3Preferences, uiFieldPlayer3Preferences[5]);
                }
            });
            initialPlayer3Preferences = new boolean[6];
            isInitialPlayer3PreferencesSet = false;

            uiFieldPlayer4 = addComponent("Player 4");
            uiFieldPlayer4Preferences = generatePlayerPreferences();
            uiFieldPlayer4Preferences[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    flipCheckboxes(uiFieldPlayer4Preferences, uiFieldPlayer4Preferences[5]);
                }
            });
            initialPlayer4Preferences = new boolean[6];
            isInitialPlayer4PreferencesSet = false;

            uiFieldPlayer5 = addComponent("Player 5");
            uiFieldPlayer5Preferences = generatePlayerPreferences();
            uiFieldPlayer5Preferences[5].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    flipCheckboxes(uiFieldPlayer5Preferences, uiFieldPlayer5Preferences[5]);
                }
            });
            initialPlayer5Preferences = new boolean[6];
            isInitialPlayer5PreferencesSet = false;

            addResetButtonComponent("Reset");
            addButtonComponent("Randomize!");

            rr = new RoleRandomizer();

        }

        private void flipCheckboxes(JCheckBox[] checkboxes, JCheckBox fillBox) {
            if (fillBox.isSelected()) {
                for (int i = 0; i < checkboxes.length - 1; i++) {
                    checkboxes[i].setSelected(true);
                }
            } else {
                for (int i = 0; i < checkboxes.length - 1; i++) {
                    checkboxes[i].setSelected(false);
                }
            }

        }

        private JCheckBox[] generatePlayerPreferences() {
            final JCheckBox[] preferences = new JCheckBox[6];
            final JPanel container = new JPanel();
            container.setLayout(new GridLayout(2,5,0,0));

            container.add(generateRadioLabel("M"));
            container.add(generateRadioLabel("2"));
            container.add(generateRadioLabel("H"));
            container.add(generateRadioLabel("C"));
            container.add(generateRadioLabel("D"));
            container.add(generateRadioLabel("F"));

            for (int i = 0; i < preferences.length; i++) {
                preferences[i] = generateRadioComponent();
                container.add(preferences[i]);
            }

            add(container);
            return preferences;
        }

        private JCheckBox generateRadioComponent() {
            final JCheckBox checkbox = new JCheckBox();

            checkbox.setBackground(ColorScheme.DARKER_GRAY_COLOR);
            checkbox.setBorder(new EmptyBorder(0,0,0,0));
            checkbox.setFocusPainted(false);
            checkbox.setFocusable(false);

            return checkbox;
        }

        private JLabel generateRadioLabel(String role) {
            final JLabel label = new JLabel(role);

            label.setFont(FontManager.getRunescapeSmallFont());
            label.setBorder(new EmptyBorder(0, 5,0,0));
            label.setForeground(Color.WHITE);
            label.setFocusable(false);
            switch (role) {
                case "M":
                    label.setToolTipText("Main attacker");
                    break;
                case "2":
                    label.setToolTipText("Second attacker");
                    break;
                case "H":
                    label.setToolTipText("Healer");
                    break;
                case "C":
                    label.setToolTipText("Collector");
                    break;
                case "D":
                    label.setToolTipText("Defender");
                    break;
                case "F":
                    label.setToolTipText("Fill");
                    break;
            }

            return label;
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

    private void resetPlayerPreferences(JCheckBox[] prefs, boolean[] initial) {
            for (int i = 0; i < prefs.length; i++) {
                prefs[i].setSelected(initial[i]);
            }
    }

    private void resetPreferences() {
        panel.resultPanel.roleRandomizerResultField.setText("");
        panel.resultPanel.roleRandomizerResultField.insert("\n", 0);

        resetPlayerPreferences(uiFieldPlayer1Preferences, initialPlayer1Preferences);
        resetPlayerPreferences(uiFieldPlayer2Preferences, initialPlayer2Preferences);
        resetPlayerPreferences(uiFieldPlayer3Preferences, initialPlayer3Preferences);
        resetPlayerPreferences(uiFieldPlayer4Preferences, initialPlayer4Preferences);
        resetPlayerPreferences(uiFieldPlayer5Preferences, initialPlayer5Preferences);

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
        if (!uiFieldPlayer1.getText().isEmpty() &&
                !uiFieldPlayer2.getText().isEmpty() &&
                !uiFieldPlayer3.getText().isEmpty() &&
                !uiFieldPlayer4.getText().isEmpty() &&
                !uiFieldPlayer5.getText().isEmpty()) {

            HashMap<Integer, String> usernames = new HashMap<>();
            usernames.put(0, uiFieldPlayer1.getText());
            usernames.put(1, uiFieldPlayer2.getText());
            usernames.put(2, uiFieldPlayer3.getText());
            usernames.put(3, uiFieldPlayer4.getText());
            usernames.put(4, uiFieldPlayer5.getText());
            rr.setUsernames(usernames);
        }
    }

    private int[] setPreferences(JCheckBox[] prefs) {
        int[] playerPrefs = new int[prefs.length - 1];
        for (int i = 0; i < playerPrefs.length; i++) {
            if (prefs[i].isSelected()) {
                playerPrefs[i] = 1;
            } else {
                playerPrefs[i] = 0;
            }
        }
        return playerPrefs;
    }

    public void setInitialPreferences(JCheckBox[] currentPrefs, boolean[] initPrefs) {
            for (int i = 0; i < initPrefs.length; i++) {
                initPrefs[i] = currentPrefs[i].isSelected();
            }
    }

    public void getPreferences() {
        if (!isInitialPlayer1PreferencesSet) {
            setInitialPreferences(uiFieldPlayer1Preferences, initialPlayer1Preferences);
            isInitialPlayer1PreferencesSet = true;
        }
        rr.setPlayerOnePreferences(setPreferences(uiFieldPlayer1Preferences));

        if (!isInitialPlayer2PreferencesSet) {
            setInitialPreferences(uiFieldPlayer2Preferences, initialPlayer2Preferences);
            isInitialPlayer2PreferencesSet = true;
        }
        rr.setPlayerTwoPreferences(setPreferences(uiFieldPlayer2Preferences));

        if (!isInitialPlayer3PreferencesSet) {
            setInitialPreferences(uiFieldPlayer3Preferences, initialPlayer3Preferences);
            isInitialPlayer3PreferencesSet = true;
        }
        rr.setPlayerThreePreferences(setPreferences(uiFieldPlayer3Preferences));

        if (!isInitialPlayer4PreferencesSet) {
            setInitialPreferences(uiFieldPlayer4Preferences, initialPlayer4Preferences);
            isInitialPlayer4PreferencesSet = true;
        }
        rr.setPlayerFourPreferences(setPreferences(uiFieldPlayer4Preferences));

        if (!isInitialPlayer5PreferencesSet) {
            setInitialPreferences(uiFieldPlayer5Preferences, initialPlayer5Preferences);
            isInitialPlayer5PreferencesSet = true;
        }
        rr.setPlayerFivePreferences(setPreferences(uiFieldPlayer5Preferences));
    }

    public void randomize() {
        panel.resultPanel.roleRandomizerResultField.setText("");
        panel.resultPanel.roleRandomizerResultField.insert("\n", 0);

        getUsernames();

        if (rr.usernames.size() != 5) {
            panel.resultPanel.roleRandomizerResultField.setText(" Set usernames in the above fields.");
            return;
        }

        getPreferences();

        try {
            String[] roles = rr.randomize();

            for (int index = 0; index < 5; index++) {
                    switch (index) {
                        case 0:
                            panel.resultPanel.roleRandomizerResultField.insert(" Main\t" + roles[index] + "\n", 1);
                            for (Map.Entry<Integer, String> entry: rr.usernames.entrySet()) {
                                if (entry.getValue() == roles[index]) {
                                    rr.popPreference(entry.getKey(), index);
                                    uncheckPreference(roles[index], index);
                                }
                            }
                            break;
                        case 1:
                            panel.resultPanel.roleRandomizerResultField.append(" Second\t" + roles[index] + "\n");
                            for (Map.Entry<Integer, String> entry: rr.usernames.entrySet()) {
                                if (entry.getValue() == roles[index]) {
                                    rr.popPreference(entry.getKey(), index);
                                    uncheckPreference(roles[index], index);
                                }
                            }
                            break;
                        case 2:
                            panel.resultPanel.roleRandomizerResultField.append(" Healer\t" + roles[index] + "\n");
                            for (Map.Entry<Integer, String> entry: rr.usernames.entrySet()) {
                                if (entry.getValue() == roles[index]) {
                                    rr.popPreference(entry.getKey(), index);
                                    uncheckPreference(roles[index], index);
                                }
                            }
                            break;
                        case 3:
                            panel.resultPanel.roleRandomizerResultField.append(" Collector\t" + roles[index] + "\n");
                            for (Map.Entry<Integer, String> entry: rr.usernames.entrySet()) {
                                if (entry.getValue() == roles[index]) {
                                    rr.popPreference(entry.getKey(), index);
                                    uncheckPreference(roles[index], index);
                                }
                            }
                            break;
                        case 4:
                            panel.resultPanel.roleRandomizerResultField.append(" Defender\t" + roles[index]);
                            for (Map.Entry<Integer, String> entry: rr.usernames.entrySet()) {
                                if (entry.getValue() == roles[index]) {
                                    rr.popPreference(entry.getKey(), index);
                                    uncheckPreference(roles[index], index);
                                }
                            }
                            break;
                    }
            }

        } catch (Exception ex) {
            panel.resultPanel.roleRandomizerResultField.setText(" All roles exhausted or no prefs set");
        }
    }

    private void uncheckPreference(String username, int role) {
        if (username.equalsIgnoreCase(uiFieldPlayer1.getText())) {
            uiFieldPlayer1Preferences[role].setSelected(false);
            if (uiFieldPlayer1Preferences[5].isSelected()) {
                uiFieldPlayer1Preferences[5].setSelected(false);
            }
        } else if (username.equalsIgnoreCase(uiFieldPlayer2.getText())) {
            uiFieldPlayer2Preferences[role].setSelected(false);
            if (uiFieldPlayer2Preferences[5].isSelected()) {
                uiFieldPlayer2Preferences[5].setSelected(false);
            }
        } else if (username.equalsIgnoreCase(uiFieldPlayer3.getText())) {
            uiFieldPlayer3Preferences[role].setSelected(false);
            if (uiFieldPlayer3Preferences[5].isSelected()) {
                uiFieldPlayer3Preferences[5].setSelected(false);
            }
        } else if (username.equalsIgnoreCase(uiFieldPlayer4.getText())) {
            uiFieldPlayer4Preferences[role].setSelected(false);
            if (uiFieldPlayer4Preferences[5].isSelected()) {
                uiFieldPlayer4Preferences[5].setSelected(false);
            }
        } else if (username.equalsIgnoreCase(uiFieldPlayer5.getText())) {
            uiFieldPlayer5Preferences[role].setSelected(false);
            if (uiFieldPlayer5Preferences[5].isSelected()) {
                uiFieldPlayer5Preferences[5].setSelected(false);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            randomize();
    }
}
