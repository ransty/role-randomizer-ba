/*
 * Copyright (c) 2021-2022, Keano Porcaro <keano@ransty.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.rolerandomizer.ui;

import com.rolerandomizer.MetaRoleInfo;
import com.rolerandomizer.PlayerPrefs;
import com.rolerandomizer.RoleParser;
import com.rolerandomizer.RoleRandomizerConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;

@Slf4j
public class RoleRandomizerPluginPanel extends PluginPanel
{

        public RandomizeResultPanel resultPanel;
        public RoleRandomizerPanel inputPanel;

    public RoleRandomizerPluginPanel(Client client, RoleRandomizerConfig config, ChatMessageManager chatMessageManager)
        {
            super();

            inputPanel = new RoleRandomizerPanel(client, config, chatMessageManager, this);

            resultPanel = new RandomizeResultPanel(this);

            JButton clearButton = new JButton("Clear");
            clearButton.setBackground(ColorScheme.PROGRESS_ERROR_COLOR);
            clearButton.setBorder(new EmptyBorder(5, 7, 5, 7));
            clearButton.setToolTipText("This wipes the slate clean!");
            clearButton.addActionListener(e -> {
                inputPanel.cleanSlate();
                resultPanel.cleanSlate();
            });

            JButton removePreviousButton = new JButton("Remove previous roles");
            removePreviousButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
            removePreviousButton.setFocusable(false);
            removePreviousButton.addActionListener(e -> {
                inputPanel.removePreviousRoles();
            });

            add(inputPanel);
            add(removePreviousButton);
            add(resultPanel);
            add(clearButton);
        }

    public void addPlayer(String playerName) {
        // sanitize the string even more
        playerName = playerName.replaceAll("\\[.*\\]", "").trim().replace(":", "");
        addNextPlayer(playerName, new boolean[]{true, true, true, true, true, true});
    }

    public void addPlayer(String playerName, String preferencesMessage) {
        playerName = playerName.replaceAll("\\[.*\\]", "").trim().replace(":", "");
        RoleParser rp = new RoleParser();

        // There must be a better way to do this
        List<MetaRoleInfo> preferences = rp.prefsFrom(preferencesMessage);
        boolean[] setPreferences = new boolean[6];
        int fillCounter = 0;
        for (MetaRoleInfo p : preferences) {
            switch(p.getShortName()) {
                case "m":
                    fillCounter++;
                    setPreferences[0] = true;
                    break;
                case "2":
                    fillCounter++;
                    setPreferences[1] = true;
                    break;
                case "h":
                    fillCounter++;
                    setPreferences[2] = true;
                    break;
                case "c":
                    fillCounter++;
                    setPreferences[3] = true;
                    break;
                case "d":
                    fillCounter++;
                    setPreferences[4] = true;
                    break;
            }
        }
        if (fillCounter == 5) {
            setPreferences[5] = true;
        }
        addNextPlayer(playerName, setPreferences);
    }

    private boolean addNextPlayer(String playerName, boolean[] prefs) {
        if (inputPanel.uiFieldPlayer1.getText().isEmpty()) {
            inputPanel.uiFieldPlayer1.setText(playerName);
            inputPanel.setPreferences(inputPanel.uiFieldPlayer1Preferences, prefs);
            return true;
        } else if (inputPanel.uiFieldPlayer2.getText().isEmpty()) {
            inputPanel.uiFieldPlayer2.setText(playerName);
            inputPanel.setPreferences(inputPanel.uiFieldPlayer2Preferences, prefs);
            return true;
        } else if (inputPanel.uiFieldPlayer3.getText().isEmpty()) {
            inputPanel.uiFieldPlayer3.setText(playerName);
            inputPanel.setPreferences(inputPanel.uiFieldPlayer3Preferences, prefs);
            return true;
        } else if (inputPanel.uiFieldPlayer4.getText().isEmpty()) {
            inputPanel.uiFieldPlayer4.setText(playerName);
            inputPanel.setPreferences(inputPanel.uiFieldPlayer4Preferences, prefs);
            return true;
        } else if (inputPanel.uiFieldPlayer5.getText().isEmpty()) {
            inputPanel.uiFieldPlayer5.setText(playerName);
            inputPanel.setPreferences(inputPanel.uiFieldPlayer5Preferences, prefs);
            return true;
        }
        return false;
    }
}
