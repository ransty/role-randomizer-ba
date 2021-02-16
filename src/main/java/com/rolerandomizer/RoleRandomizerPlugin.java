package com.rolerandomizer;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MessageNode;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.CommandExecuted;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;

@Slf4j
@PluginDescriptor(
	name = "BA Role Randomizer"
)
public class RoleRandomizerPlugin extends Plugin
{
	private static final BufferedImage ICON = ImageUtil.getResourceStreamFromClass(RoleRandomizerPlugin.class, "rolerandom.png");

	@Inject
	private Client client;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private RoleRandomizerConfig config;

	private RoleRandomizer randomizer;

	private RoleRandomizerPanel panel;
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
		panel = new RoleRandomizerPanel();
		randomizer = new RoleRandomizer();
		usernames = new HashMap<Integer, String>();
		navButton = NavigationButton.builder()
				.tooltip("BA Role Randomizer")
				.icon(ICON)
				.priority(6)
				.panel(panel)
				.build();
		clientToolbar.addNavigation(navButton);
		log.info("Example started!");
		panel.randomizeButton.addActionListener(new ActionListener() {
			@Override
			@Subscribe
			public void actionPerformed(ActionEvent e) {
				try {
					panel.setPreferencesAndUsernames();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				if (panel.roles != null)
				{
					String shortFormRoles = "";
					for (int index = 0; index < 5; index++) {
						if (index != 4) {
							shortFormRoles += panel.roles[index] + " / ";
						} else {
							shortFormRoles += panel.roles[index];
						}
					}
					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", shortFormRoles, null);
					log.debug("Roles" + Arrays.toString(panel.roles));
				}
			}
		});
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted) throws Exception {
		if (commandExecuted.getCommand().equals("prefs")) {
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", usernames.get(0) + " prefs " + Arrays.toString(player1Prefs), null);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", usernames.get(1) + " prefs " + Arrays.toString(player2Prefs), null);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", usernames.get(2) + " prefs " + Arrays.toString(player3Prefs), null);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", usernames.get(3) + " prefs " + Arrays.toString(player4Prefs), null);
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", usernames.get(4) + " prefs " + Arrays.toString(player5Prefs), null);
		}
		if (commandExecuted.getCommand().equals("r"))
		{
			// lets build the randoer
			if (commandExecuted.getArguments().length == 10) {
				usernames.put(0, commandExecuted.getArguments()[0]);
				usernames.put(1, commandExecuted.getArguments()[2]);
				usernames.put(2, commandExecuted.getArguments()[4]);
				usernames.put(3, commandExecuted.getArguments()[6]);
				usernames.put(4, commandExecuted.getArguments()[8]);
				randomizer.setUsernames(usernames);
				player1Prefs = new String[]{commandExecuted.getArguments()[1].replaceAll("[^a-zA-Z0-9]", "")};
				player2Prefs = new String[]{commandExecuted.getArguments()[3].replaceAll("[^a-zA-Z0-9]", "")};
				player3Prefs = new String[]{commandExecuted.getArguments()[5].replaceAll("[^a-zA-Z0-9]", "")};
				player4Prefs = new String[]{commandExecuted.getArguments()[7].replaceAll("[^a-zA-Z0-9]", "")};
				player5Prefs = new String[]{commandExecuted.getArguments()[9].replaceAll("[^a-zA-Z0-9]", "")};

				log.info(usernames.get(0) + " prefs " + Arrays.toString(player1Prefs));
				log.info(usernames.get(1) + " prefs " + Arrays.toString(player2Prefs));
				log.info(usernames.get(2) + " prefs " + Arrays.toString(player3Prefs));
				log.info(usernames.get(3) + " prefs " + Arrays.toString(player4Prefs));
				log.info(usernames.get(4) + " prefs " + Arrays.toString(player5Prefs));

				// prefs should be entered like this
				// ::rando <name> <prefs>
				// where prefs can be a minimum of 3 of the following:
				//  a/2/h/c/d or fill
				// no prefs specified will be handled in another method
				randomizer.setPlayerOnePreferences(convertPreferences(player1Prefs));
				randomizer.setPlayerTwoPreferences(convertPreferences(player2Prefs));
				randomizer.setPlayerThreePreferences(convertPreferences(player3Prefs));
				randomizer.setPlayerFourPreferences(convertPreferences(player4Prefs));
				randomizer.setPlayerFivePreferences(convertPreferences(player5Prefs));
				// rando!!!
				String[] roles = randomizer.randomize();
				String shortFormRoles = "";
				for (int index = 0; index < 5; index++) {
					switch(index) {
						case 0:
						case 1:
							shortFormRoles += ColorUtil.wrapWithColorTag(roles[index].substring(0, 1).toUpperCase()+roles[index].substring(1), Color.RED.darker()) + " / ";
							break;
						case 2:
							shortFormRoles += ColorUtil.wrapWithColorTag(roles[index].substring(0, 1).toUpperCase()+roles[index].substring(1), Color.GREEN.darker().darker()) + " / ";
							break;
						case 3:
							shortFormRoles += ColorUtil.wrapWithColorTag(roles[index].substring(0, 1).toUpperCase()+roles[index].substring(1), Color.YELLOW) + " / ";
							break;
						case 4:
							shortFormRoles += ColorUtil.wrapWithColorTag(roles[index].substring(0, 1).toUpperCase()+roles[index].substring(1), Color.BLUE.darker());
							break;
					}
				}
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", shortFormRoles, null);
			}
		}
	}

	public int[] convertPreferences(String[] prefs) {
		int[] newPrefs = {0, 0, 0, 0, 0};
		for (String s : prefs) {
			if (s.equals("fill")) {
				return new int[]{1, 1, 1, 1, 1};
			}
			for (int i = 0; i < s.length(); i++) {
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
