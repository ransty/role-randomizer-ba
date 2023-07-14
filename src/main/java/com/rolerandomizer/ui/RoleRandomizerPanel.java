package com.rolerandomizer.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.rolerandomizer.RoleRandomizerConfig;
import com.rolerandomizer.exceptions.NoPermutationException;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.FlatTextField;
import lombok.extern.slf4j.Slf4j;

import com.rolerandomizer.RoleRandomizer;
import net.runelite.client.util.ColorUtil;

@Slf4j
public class RoleRandomizerPanel extends JPanel implements ActionListener {

    private final RoleRandomizerPluginPanel panel;

    public JTextField uiFieldPlayer1;
    public JCheckBox[] uiFieldPlayer1Preferences;
    public JTextField uiFieldPlayer2;
    public JCheckBox[] uiFieldPlayer2Preferences;
    public JTextField uiFieldPlayer3;
    public JCheckBox[] uiFieldPlayer3Preferences;
    public JTextField uiFieldPlayer4;
    public JCheckBox[] uiFieldPlayer4Preferences;
    public JTextField uiFieldPlayer5;
    public JCheckBox[] uiFieldPlayer5Preferences;

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
    public boolean keepPreviousRolesSelected;

    private String[] previousRandom;

    private RoleRandomizer rr;

    private Client client;
    private RoleRandomizerConfig config;
    private final ChatMessageManager chatMessageManager;

    private JButton roleRandomizerButton;
    private JButton resetButton;

        protected RoleRandomizerPanel(Client client, RoleRandomizerConfig config, ChatMessageManager chatMessageManager, RoleRandomizerPluginPanel panel) {
            super();

            this.panel = panel;
            this.client = client;
            this.config = config;
            this.chatMessageManager = chatMessageManager;
            this.keepPreviousRolesSelected = false;

            setLayout(new GridLayout(6, 2, 5, 5));

            initializeComponents();
        }

        private void addFillListener(JCheckBox[] pPreferences) {
            pPreferences[5].addActionListener(e -> flipCheckboxes(pPreferences, pPreferences[5]));
        }

        public void addAllPreferences(JCheckBox[] pPreferences) {
            for (JCheckBox box : pPreferences) {
                box.setSelected(true);
            }
        }

        private void addCheckFillListener(JCheckBox[] playerPreferences) {
            for (int i = 0; i < playerPreferences.length - 1; i++) {
                playerPreferences[i].addActionListener(e -> {
                    if (playerPreferences[5].isSelected()) {
                        playerPreferences[5].setSelected(false);
                    } else {
                        int isFill = 0;
                        for (int box = 0; box < playerPreferences.length - 1; box++) {
                            if (!playerPreferences[box].isSelected()) {
                                break;
                            } else {
                                isFill++;
                            }
                        }

                        if (isFill == 5) {
                            playerPreferences[5].setSelected(true);
                        }
                    }
                });
            }
        }

        public void flipCheckboxes(JCheckBox[] checkboxes, JCheckBox fillBox) {
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

        private JCheckBox[] generatePlayerPreferences(JTextField textField, boolean[] initialPreferences, int playerNumber) {
            final JCheckBox[] preferences = new JCheckBox[6];
            final JPanel container = new JPanel();
            container.setLayout(new GridLayout(2,6,0,0));

            container.add(generateRadioLabel("M"));
            container.add(generateRadioLabel("2"));
            container.add(generateRadioLabel("H"));
            container.add(generateRadioLabel("C"));
            container.add(generateRadioLabel("D"));
            container.add(generateRadioLabel("F"));

            JButton destroy = new JButton("x");
            destroy.setOpaque(false);
            destroy.setContentAreaFilled(false);
            destroy.setBorderPainted(false);
            destroy.setFocusable(false);
            destroy.setForeground(ColorScheme.PROGRESS_ERROR_COLOR);
            destroy.setBorder(null);
            destroy.addActionListener(e -> {
                clearPlayerPreferences(preferences, initialPreferences, playerNumber);
                textField.setText("");
            });
            container.add(destroy);

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

        private void clearPlayerPreferences(JCheckBox[] prefs, boolean[] initialPreferences, int playerNumber) {
            for (JCheckBox box : prefs) {
                box.setSelected(false);
            }

            for (int i = 0; i < initialPreferences.length; i++) {
                initialPreferences[i] = false;
            }

            switch(playerNumber) {
                case 1:
                    isInitialPlayer1PreferencesSet = false;
                    break;
                case 2:
                    isInitialPlayer2PreferencesSet = false;
                    break;
                case 3:
                    isInitialPlayer3PreferencesSet = false;
                    break;
                case 4:
                    isInitialPlayer4PreferencesSet = false;
                    break;
                case 5:
                    isInitialPlayer5PreferencesSet = false;
                    break;
            }
        }

    private JTextField addComponent(String label) {
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

    private JButton addResetButtonComponent(String label) {
        final JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        JButton resetButton = new JButton(label);

        resetButton.setBackground(ColorScheme.DARK_GRAY_COLOR);
        resetButton.setBorder(new EmptyBorder(5, 7, 5, 7));
        resetButton.setToolTipText("This resets preferences!");
        resetButton.setEnabled(false);

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

        resetButton.addActionListener(e -> resetPreferences());

        container.add(resetButton, BorderLayout.CENTER);
        add(container, BorderLayout.NORTH);

        return resetButton;
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

        resetButton.setEnabled(false);

    }

    private void addRandomizeButtonComponent(String label) {
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

        resetButton.setEnabled(true);
    }

    public void randomize() {
        panel.resultPanel.roleRandomizerResultField.setText("");
        panel.resultPanel.roleRandomizerResultField.insert("\n", 0);

        getUsernames();

        if (rr.usernames.size() != 5) {
            panel.resultPanel.roleRandomizerResultField.setText(" Set usernames in the above fields.");
            return;
        }

        // Before getting preferences, check at least 1 box is ticked per player
        if (!checkMinimumBoxesTicked()) {
            panel.resultPanel.roleRandomizerResultField.setText(" Minimum amount of roles not set!");
            return;
        }

        getPreferences();

        try {
            String[] roles = rr.randomize();
            this.previousRandom = roles;

            StringBuilder shortFormRoles = new StringBuilder();

            for (int index = 0; index < 5; index++) {
                    switch (index) {
                        case 0:
                            panel.resultPanel.roleRandomizerResultField.insert(" Main\t" + roles[index] + "\n", 1);
                            if (config.sendToChatBox()) {
                                if (client.getGameState().equals(GameState.LOGGED_IN)) {
                                    shortFormRoles.append(ColorUtil.wrapWithColorTag((roles[index]),
                                            Color.RED.darker())).append(" / "
                                    );
                                }
                            }
                            if (!keepPreviousRolesSelected) {
                                removeRolePreferences(roles, index);
                            }
                            break;
                        case 1:
                            panel.resultPanel.roleRandomizerResultField.append(" Second\t" + roles[index] + "\n");
                            if (config.sendToChatBox()) {
                                if (client.getGameState().equals(GameState.LOGGED_IN)) {
                                    shortFormRoles.append(ColorUtil.wrapWithColorTag((roles[index]),
                                            Color.RED.darker())).append(" / "
                                    );
                                }
                            }
                            if (!keepPreviousRolesSelected) {
                                removeRolePreferences(roles, index);
                            }
                            break;
                        case 2:
                            panel.resultPanel.roleRandomizerResultField.append(" Healer\t" + roles[index] + "\n");
                            if (config.sendToChatBox()) {
                                if (client.getGameState().equals(GameState.LOGGED_IN)) {
                                    shortFormRoles.append(ColorUtil.wrapWithColorTag(
                                            (roles[index]),
                                            Color.GREEN.darker().darker())).append(" / "
                                    );
                                }
                            }
                            if (!keepPreviousRolesSelected) {
                                removeRolePreferences(roles, index);
                            }
                            break;
                        case 3:
                            panel.resultPanel.roleRandomizerResultField.append(" Collector\t" + roles[index] + "\n");
                            if (config.sendToChatBox()) {
                                if (client.getGameState().equals(GameState.LOGGED_IN)) {
                                    shortFormRoles.append(ColorUtil.wrapWithColorTag(
                                            (roles[index]),
                                            Color.YELLOW)).append(" / "
                                    );
                                }
                            }
                            if (!keepPreviousRolesSelected) {
                                removeRolePreferences(roles, index);
                            }
                            break;
                        case 4:
                            panel.resultPanel.roleRandomizerResultField.append(" Defender\t" + roles[index]);
                            if (config.sendToChatBox()) {
                                if (client.getGameState().equals(GameState.LOGGED_IN)) {
                                    shortFormRoles.append(ColorUtil.wrapWithColorTag(
                                            (roles[index]),
                                            Color.BLUE.darker())
                                    );
                                }
                            }
                            if (!keepPreviousRolesSelected) {
                                removeRolePreferences(roles, index);
                            }
                            break;
                    }
            }

            if (config.sendToChatBox()) {
                if (client.getGameState().equals(GameState.LOGGED_IN)) {
                    chatMessageManager.queue(
                            QueuedMessage.builder()
                                    .type(ChatMessageType.GAMEMESSAGE)
                                    .runeLiteFormattedMessage(shortFormRoles.toString())
                                    .build()
                    );
                }
            }

        } catch (NoPermutationException ex) {
            // Determine which role is missing
            panel.resultPanel.roleRandomizerResultField.setText(missingRole());
        }
    }

    private String missingRole() {
        StringBuilder result = new StringBuilder();
        int[] p1Count = countRoles(uiFieldPlayer1Preferences);
        int[] p2Count = countRoles(uiFieldPlayer2Preferences);
        int[] p3Count = countRoles(uiFieldPlayer3Preferences);
        int[] p4Count = countRoles(uiFieldPlayer4Preferences);
        int[] p5Count = countRoles(uiFieldPlayer5Preferences);

        int main_attack = p1Count[0] + p2Count[0] + p3Count[0] + p4Count[0] + p5Count[0];
        int second_attack = p1Count[1] + p2Count[1] + p3Count[1] + p4Count[1] + p5Count[1];
        int healer = p1Count[2] + p2Count[2] + p3Count[2] + p4Count[2] + p5Count[2];
        int collector = p1Count[3] + p2Count[3] + p3Count[3] + p4Count[3] + p5Count[3];
        int defender = p1Count[4] + p2Count[4] + p3Count[4] + p4Count[4] + p5Count[4];

        if (main_attack == 0) {
            result.append(" No available Mains");
            result.append(System.getProperty("line.separator"));
        } if (second_attack == 0) {
            result.append(" No available 2nd Attackers");
            result.append(System.getProperty("line.separator"));
        } if (healer == 0) {
            result.append(" No available Healers");
            result.append(System.getProperty("line.separator"));
        } if (collector == 0) {
            result.append(" No available Collectors");
            result.append(System.getProperty("line.separator"));
        } if (defender == 0) {
            result.append(" No available Defenders");
            result.append(System.getProperty("line.separator"));
        }
        log.info(result.toString());
        return result.toString();
    }

    private int[] countRoles(JCheckBox[] rolesSelected) {
        int[] returnCount = new int[5];
        for (int i = 0; i < rolesSelected.length; i++) {
            if (rolesSelected[i].isSelected()) {
                returnCount[i] = 1;
            }
        }
        return returnCount;
    }

    private boolean checkMinimumBoxesTicked() {
        if (minimumRolesSet(uiFieldPlayer1Preferences)) {
            if (minimumRolesSet(uiFieldPlayer2Preferences)) {
                if (minimumRolesSet(uiFieldPlayer3Preferences)) {
                    if (minimumRolesSet(uiFieldPlayer4Preferences)) {
                        if (minimumRolesSet(uiFieldPlayer5Preferences)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean minimumRolesSet(JCheckBox[] playerPreferences) {
        for (int i = 0; i < playerPreferences.length; i++) {
            if (playerPreferences[i].isSelected()) {
                // This means at least one box has been checked
                return true;
            }
        }
        return false;
    }

    public void removePreviousRoles() {
        for (int index = 0; index < 5; index++) {
            removeRolePreferences(previousRandom, index);
        }
    }
    
    private void removeRolePreferences(String[] roles, int index) {
        for (Map.Entry<Integer, String> entry: rr.usernames.entrySet()) {
            if (Objects.equals(entry.getValue(), roles[index])) {
                rr.popPreference(entry.getKey(), index);
                uncheckPreference(roles[index], index);
            }
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

    private void initializeComponents() {
        uiFieldPlayer1 = addComponent("Player 1");
        uiFieldPlayer1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (client.getGameState().equals(GameState.LOGGED_IN)) {
                    uiFieldPlayer1.setText(Objects.requireNonNull(client.getLocalPlayer()).getName());
                    addAllPreferences(uiFieldPlayer1Preferences);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        isInitialPlayer1PreferencesSet = false;
        initialPlayer1Preferences = new boolean[6];
        uiFieldPlayer1Preferences = generatePlayerPreferences(uiFieldPlayer1, initialPlayer1Preferences, 1);
        addFillListener(uiFieldPlayer1Preferences);
        addCheckFillListener(uiFieldPlayer1Preferences);

        uiFieldPlayer2 = addComponent("Player 2");
        initialPlayer2Preferences = new boolean[6];
        isInitialPlayer2PreferencesSet = false;
        uiFieldPlayer2Preferences = generatePlayerPreferences(uiFieldPlayer2, initialPlayer2Preferences, 2);
        addFillListener(uiFieldPlayer2Preferences);

        addCheckFillListener(uiFieldPlayer2Preferences);

        uiFieldPlayer3 = addComponent("Player 3");
        initialPlayer3Preferences = new boolean[6];
        isInitialPlayer3PreferencesSet = false;
        uiFieldPlayer3Preferences = generatePlayerPreferences(uiFieldPlayer3, initialPlayer3Preferences, 3);
        addFillListener(uiFieldPlayer3Preferences);
        addCheckFillListener(uiFieldPlayer3Preferences);

        uiFieldPlayer4 = addComponent("Player 4");
        initialPlayer4Preferences = new boolean[6];
        isInitialPlayer4PreferencesSet = false;
        uiFieldPlayer4Preferences = generatePlayerPreferences(uiFieldPlayer4, initialPlayer4Preferences, 4);
        addFillListener(uiFieldPlayer4Preferences);
        addCheckFillListener(uiFieldPlayer4Preferences);

        uiFieldPlayer5 = addComponent("Player 5");
        initialPlayer5Preferences = new boolean[6];
        isInitialPlayer5PreferencesSet = false;
        uiFieldPlayer5Preferences = generatePlayerPreferences(uiFieldPlayer5, initialPlayer5Preferences, 5);
        addFillListener(uiFieldPlayer5Preferences);
        addCheckFillListener(uiFieldPlayer5Preferences);

        resetButton = addResetButtonComponent("Reset");
        addRandomizeButtonComponent("Randomize!");

        rr = new RoleRandomizer();
    }

    public void cleanSlate() {
        initialPlayer1Preferences = new boolean[6];
        isInitialPlayer1PreferencesSet = false;
        initialPlayer2Preferences = new boolean[6];
        isInitialPlayer2PreferencesSet = false;
        initialPlayer3Preferences = new boolean[6];
        isInitialPlayer3PreferencesSet = false;
        initialPlayer4Preferences = new boolean[6];
        isInitialPlayer4PreferencesSet = false;
        initialPlayer5Preferences = new boolean[6];
        isInitialPlayer5PreferencesSet = false;

        resetCheckBoxes(uiFieldPlayer1Preferences);
        resetCheckBoxes(uiFieldPlayer2Preferences);
        resetCheckBoxes(uiFieldPlayer3Preferences);
        resetCheckBoxes(uiFieldPlayer4Preferences);
        resetCheckBoxes(uiFieldPlayer5Preferences);

        uiFieldPlayer1.setText("");
        uiFieldPlayer2.setText("");
        uiFieldPlayer3.setText("");
        uiFieldPlayer4.setText("");
        uiFieldPlayer5.setText("");

        resetButton.setEnabled(false);

        rr = new RoleRandomizer();
    }

    private void resetCheckBoxes(JCheckBox[] boxes) {
        for (JCheckBox jCheckBox : boxes) {
            jCheckBox.setSelected(false);
        }
    }
}
