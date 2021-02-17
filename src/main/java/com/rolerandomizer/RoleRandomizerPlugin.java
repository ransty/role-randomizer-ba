/*
 * Copyright (c) 2021, Keano Porcaro <keano@ransty.com>
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
package com.rolerandomizer;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.CommandExecuted;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ColorUtil;

@Slf4j
@PluginDescriptor(
		name = "BA Role Randomizer"
)
public class RoleRandomizerPlugin extends Plugin
{
	@Inject
	private Client client;

	private RoleRandomizer randomizer;

	private String[] player1Prefs;
	private String[] player2Prefs;
	private String[] player3Prefs;
	private String[] player4Prefs;
	private String[] player5Prefs;
	private boolean isPreferencesSet;
	private HashMap<Integer, String> usernames;

	@Override
	protected void startUp() throws Exception
	{
		randomizer = new RoleRandomizer();
		usernames = new HashMap<>();
	}

	@Override
	protected void shutDown() throws Exception
	{
		usernames = null;
		randomizer = null;
		log.debug("Shutting down BA role randomizer plugin");
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted) throws Exception
	{
		if (commandExecuted.getCommand().equals("prefs") && isPreferencesSet)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", usernames.get(0) + " prefs " + Arrays.toString(player1Prefs), null);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", usernames.get(1) + " prefs " + Arrays.toString(player2Prefs), null);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", usernames.get(2) + " prefs " + Arrays.toString(player3Prefs), null);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", usernames.get(3) + " prefs " + Arrays.toString(player4Prefs), null);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", usernames.get(4) + " prefs " + Arrays.toString(player5Prefs), null);
		}

		if (commandExecuted.getCommand().equals("r"))
		{
			if (commandExecuted.getArguments().length == 10)
			{
				setUsernames(commandExecuted.getArguments());
				collectPreferences(commandExecuted.getArguments());
				setAllPreferences();
				String teamRoles = generateRandom();
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", teamRoles, null);
			}
		}
	}

	private void setUsernames(String[] commandArgs)
	{
		usernames.put(0, commandArgs[0]);
		usernames.put(1, commandArgs[2]);
		usernames.put(2, commandArgs[4]);
		usernames.put(3, commandArgs[6]);
		usernames.put(4, commandArgs[8]);
		randomizer.setUsernames(usernames);
	}

	private void collectPreferences(String[] commandArgs)
	{
		String regex = "[^a-zA-Z0-9]";
		player1Prefs = new String[]{commandArgs[1].replaceAll(regex, "")};
		player2Prefs = new String[]{commandArgs[3].replaceAll(regex, "")};
		player3Prefs = new String[]{commandArgs[5].replaceAll(regex, "")};
		player4Prefs = new String[]{commandArgs[7].replaceAll(regex, "")};
		player5Prefs = new String[]{commandArgs[9].replaceAll(regex, "")};
	}

	private void setAllPreferences()
	{
		randomizer.setPlayerOnePreferences(convertPreferences(player1Prefs));
		randomizer.setPlayerTwoPreferences(convertPreferences(player2Prefs));
		randomizer.setPlayerThreePreferences(convertPreferences(player3Prefs));
		randomizer.setPlayerFourPreferences(convertPreferences(player4Prefs));
		randomizer.setPlayerFivePreferences(convertPreferences(player5Prefs));
		isPreferencesSet = true;
	}

	private String generateRandom() throws Exception
	{
		String[] roles = randomizer.randomize();
		StringBuilder shortFormRoles = new StringBuilder();
		for (int index = 0; index < 5; index++)
		{
			switch(index)
			{
				case 0:
				case 1:
					shortFormRoles.append(ColorUtil.wrapWithColorTag(
							roles[index]
									.substring(0, 1)
									.toUpperCase()
									+ roles[index]
									.substring(1), Color.RED.darker())).append(" / ");
					break;
				case 2:
					shortFormRoles.append(ColorUtil.wrapWithColorTag(
							roles[index]
									.substring(0, 1)
									.toUpperCase()
									+ roles[index]
									.substring(1), Color.GREEN.darker().darker())).append(" / ");
					break;
				case 3:
					shortFormRoles.append(ColorUtil.wrapWithColorTag(
							roles[index]
									.substring(0, 1)
									.toUpperCase()
									+ roles[index]
									.substring(1), Color.YELLOW)).append(" / ");
					break;
				case 4:
					shortFormRoles.append(ColorUtil.wrapWithColorTag(
							roles[index]
									.substring(0, 1)
									.toUpperCase()
									+ roles[index]
									.substring(1), Color.BLUE.darker()));
					break;
			}
		}
		return shortFormRoles.toString();
	}

	private int[] convertPreferences(String[] prefs)
	{
		int[] newPrefs = {0, 0, 0, 0, 0};
		for (String s : prefs) {
			if (s.equals("fill"))
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

	@Provides
	RoleRandomizerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(RoleRandomizerConfig.class);
	}
}