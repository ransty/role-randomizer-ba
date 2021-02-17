package com.rolerandomizer;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.CommandExecuted;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ColorUtil;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

@Slf4j
@PluginDescriptor(
		name = "BA Role Randomizer"
)
public class RoleRandomizerPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private RoleRandomizerConfig config;

	private RoleRandomizer randomizer;

	private NavigationButton navButton;

	private String[] player1Prefs;
	private String[] player2Prefs;
	private String[] player3Prefs;
	private String[] player4Prefs;
	private String[] player5Prefs;
	private HashMap<Integer, String> usernames;

	@Override
	protected void startUp() throws Exception
	{
		randomizer = new RoleRandomizer();
		usernames = new HashMap<Integer, String>();
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted) throws Exception
	{
		if (commandExecuted.getCommand().equals("prefs"))
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

	/**
	 * prefs should be entered into the command like so
	 *  ::r <username> <preferences>
	 * where preferences can be of the following:
	 *  a 2 h c d fill
	 */
	private void setAllPreferences()
	{
		// prefs should be entered like this
		// ::r <name> <prefs>
		// where prefs can be a minimum of 3 of the following:
		//  a/2/h/c/d or fill
		// no prefs specified will be handled in another statement
		randomizer.setPlayerOnePreferences(convertPreferences(player1Prefs));
		randomizer.setPlayerTwoPreferences(convertPreferences(player2Prefs));
		randomizer.setPlayerThreePreferences(convertPreferences(player3Prefs));
		randomizer.setPlayerFourPreferences(convertPreferences(player4Prefs));
		randomizer.setPlayerFivePreferences(convertPreferences(player5Prefs));
	}

	private String generateRandom() throws Exception
	{
		String[] roles = randomizer.randomize();
		String shortFormRoles = "";
		for (int index = 0; index < 5; index++)
		{
			switch(index)
			{
				case 0:
				case 1:
					shortFormRoles += ColorUtil.wrapWithColorTag(
							roles[index]
									.substring(0, 1)
									.toUpperCase()
									+roles[index]
									.substring(1), Color.RED.darker()) + " / ";
					break;
				case 2:
					shortFormRoles += ColorUtil.wrapWithColorTag(
							roles[index]
									.substring(0, 1)
									.toUpperCase()
									+roles[index]
									.substring(1), Color.GREEN.darker().darker()) + " / ";
					break;
				case 3:
					shortFormRoles += ColorUtil.wrapWithColorTag(
							roles[index]
									.substring(0, 1)
									.toUpperCase()
									+roles[index]
									.substring(1), Color.YELLOW) + " / ";
					break;
				case 4:
					shortFormRoles += ColorUtil.wrapWithColorTag(
							roles[index]
									.substring(0, 1)
									.toUpperCase()
									+roles[index]
									.substring(1), Color.BLUE.darker());
					break;
			}
		}
		return shortFormRoles;
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