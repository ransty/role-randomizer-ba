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

import net.runelite.client.ui.FontManager;

import javax.swing.*;
import java.awt.*;

public class RandomizeResultPanel extends JPanel
{
    private final RoleRandomizerPluginPanel panel;

    public JTextArea roleRandomizerResultField;

    protected RandomizeResultPanel(RoleRandomizerPluginPanel panel)
    {
        super();

        this.panel = panel;

        setLayout(new BorderLayout());

        addResultTextComponent();

    }

    private void addResultTextComponent() {
        final JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        roleRandomizerResultField = new JTextArea(7, 0);

        roleRandomizerResultField.setEnabled(false);
        roleRandomizerResultField.setPreferredSize(new Dimension(100, this.panel.getHeight()));
        roleRandomizerResultField.setFont(FontManager.getRunescapeSmallFont());

        container.add(roleRandomizerResultField);

        add(container, BorderLayout.NORTH);
    }
}
