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

import com.google.common.collect.ImmutableList;
import com.google.inject.Provides;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import javax.inject.Inject;

import com.rolerandomizer.ui.RoleRandomizerPluginPanel;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.api.events.MenuOpened;
import net.runelite.client.config.ConfigManager;
import static net.runelite.api.widgets.WidgetInfo.TO_CHILD;
import static net.runelite.api.widgets.WidgetInfo.TO_GROUP;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "BA Role Randomizer"
)
public class RoleRandomizerPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private RoleRandomizerConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	private final RoleParser parser = new RoleParser();

	private RoleRandomizer randomizer;

	private static final String ADD_TO_RANDOMIZER = "Add to rando";
	private static final String KICK_OPTION = "Kick";
	private static final ImmutableList<String> BEFORE_OPTIONS = ImmutableList.of("Add friend", "Remove friend", KICK_OPTION);
	private static final ImmutableList<String> AFTER_OPTIONS = ImmutableList.of("Message");

	private String currentMessage = null;

	private RoleRandomizerPluginPanel panel;

	private String[] player1Prefs;
	private String[] player2Prefs;
	private String[] player3Prefs;
	private String[] player4Prefs;
	private String[] player5Prefs;
	private HashMap<Integer, String> usernames;

	private NavigationButton navButton;

	@Inject
	private ClientToolbar clientToolbar;

	private String teamRoles;

	@Override
	protected void startUp() {
		randomizer = new RoleRandomizer();
		usernames = new HashMap<>();

		panel = new RoleRandomizerPluginPanel(client, config, chatMessageManager);

		BufferedImage icon = ImageUtil.loadImageResource(RoleRandomizerPlugin.class, "icon.png");
		navButton = NavigationButton.builder()
				.tooltip("BA Role Randomizer")
				.priority(9)
				.icon(icon)
				.panel(panel)
				.build();
		clientToolbar.addNavigation(navButton);

	}

	@Override
	protected void shutDown() {
		usernames = null;
		randomizer = null;
		currentMessage = null;

		clientToolbar.removeNavigation(navButton);

		log.debug("Shutting down BA role randomizer plugin");
	}

	@Subscribe
	public void onMenuOpened(MenuOpened event)
	{
		if (event.getMenuEntries().length < 2)
		{
			return;
		}

		final MenuEntry entry = event.getMenuEntries()[event.getMenuEntries().length - 2];

		if (entry.getType() != MenuAction.CC_OP_LOW_PRIORITY && entry.getType() != MenuAction.RUNELITE)
		{
			return;
		}

		final int groupId = TO_GROUP(entry.getParam1());
		final int childId = TO_CHILD(entry.getParam1());

		if (groupId != WidgetInfo.CHATBOX.getGroupId())
		{
			return;
		}

		final Widget widget = client.getWidget(groupId, childId);
		final Widget parent = widget.getParent();

		if (WidgetInfo.CHATBOX_MESSAGE_LINES.getId() != parent.getId())
		{
			return;
		}

		final int first = WidgetInfo.CHATBOX_FIRST_MESSAGE.getChildId();

		final int dynamicChildId = (childId - first) * 4;

		final Widget messageContents = parent.getChild(dynamicChildId);
		if (messageContents == null)
		{
			return;
		}

		String playerName = messageContents.getText();

		client.createMenuEntry(1)
				.setOption(ADD_TO_RANDOMIZER)
				.setTarget(entry.getTarget())
				.setType(MenuAction.RUNELITE)
				.onClick(e ->
				{
					panel.addPlayer(Text.removeTags(Text.toJagexName(playerName)));
				});
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{

		final int componentId = event.getActionParam1();
		int groupId = WidgetInfo.TO_GROUP(componentId);
		String option = event.getOption();

		if (groupId == WidgetInfo.FRIENDS_LIST.getGroupId() || groupId == WidgetInfo.FRIENDS_CHAT.getGroupId()
				|| componentId == WidgetInfo.CLAN_MEMBER_LIST.getId() || componentId == WidgetInfo.CLAN_GUEST_MEMBER_LIST.getId())
		{
			boolean after;

			if (AFTER_OPTIONS.contains(option))
			{
				after = true;
			}
			else if (BEFORE_OPTIONS.contains(option))
			{
				after = false;
			}
			else
			{
				return;
			}

			client.createMenuEntry(after ? -2 : -1)
					.setOption(ADD_TO_RANDOMIZER)
					.setTarget(event.getTarget())
					.setType(MenuAction.RUNELITE)
					.onClick(e ->
					{
						panel.addPlayer(Text.removeTags(Text.toJagexName(event.getTarget())));
					});
		}
	}

	public String buildPrefsPrint()
	{
		String prefs = "";
		prefs += nounCapitalize(usernames.get(0)) + " " + stripArrayExtras(player1Prefs) + "  ";
		prefs += nounCapitalize(usernames.get(1)) + " " + stripArrayExtras(player2Prefs) + "  ";
		prefs += nounCapitalize(usernames.get(2)) + " " + stripArrayExtras(player3Prefs) + "  ";
		prefs += nounCapitalize(usernames.get(3)) + " " + stripArrayExtras(player4Prefs) + "  ";
		prefs += nounCapitalize(usernames.get(4)) + " " + stripArrayExtras(player5Prefs) + "  ";
		return prefs;
	}

	public String stripArrayExtras(String[] array)
	{
		return String.join("", array)
			.replace("m2hcd", "fill")
			.replace("m2", "a");
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted) throws Exception
	{
		if (commandExecuted.getCommand().equals("prefs") && randomizer.isPreferencesSet())
		{
			client.addChatMessage(
				ChatMessageType.GAMEMESSAGE,
				"",
				buildPrefsPrint(),
				null
			);
		}

		if (commandExecuted.getCommand().equals("r"))
		{
			// first determine which player prefers which role
			List<PlayerPrefs> prefs = parser.parse(Arrays.asList(commandExecuted.getArguments()));
			if (prefs.size() == 5)
			{
				setUsernames(prefs);
				if (!randomizer.isPreferencesSet()) {
					collectPreferences(prefs);
					setAllPreferences(prefs);
				}
				teamRoles = generateRandom();
				client.addChatMessage(
					ChatMessageType.GAMEMESSAGE,
					"",
					teamRoles,
					null
				);
			}
		}

		if (commandExecuted.getCommand().equals("prevr") && !teamRoles.isEmpty())
		{
			client.addChatMessage(
				ChatMessageType.GAMEMESSAGE,
				"",
				teamRoles,
				null
			);
		}

		if (commandExecuted.getCommand().equals("rr") && !teamRoles.isEmpty())
		{
			teamRoles = generateRandom();
			client.addChatMessage(
				ChatMessageType.GAMEMESSAGE,
				"",
				teamRoles,
				null
			);
		}
	}

	private void setUsernames(List<PlayerPrefs> prefs)
	{
		for (int i = 0; i < prefs.size(); i++)
		{
			usernames.put(i, prefs.get(i).getName());
		}
		randomizer.setUsernames(usernames);
	}

	private void collectPreferences(List<PlayerPrefs> prefs)
	{
		player1Prefs = prefs.get(0).getPrefs().stream().map(MetaRoleInfo::getShortName).toArray(String[]::new);
		player2Prefs = prefs.get(1).getPrefs().stream().map(MetaRoleInfo::getShortName).toArray(String[]::new);
		player3Prefs = prefs.get(2).getPrefs().stream().map(MetaRoleInfo::getShortName).toArray(String[]::new);
		player4Prefs = prefs.get(3).getPrefs().stream().map(MetaRoleInfo::getShortName).toArray(String[]::new);
		player5Prefs = prefs.get(4).getPrefs().stream().map(MetaRoleInfo::getShortName).toArray(String[]::new);
	}

	private void setAllPreferences(List<PlayerPrefs> prefs)
	{
		randomizer.setPlayerOnePreferences(convertPreferences(prefs.get(0).getPrefs()));
		randomizer.setPlayerTwoPreferences(convertPreferences(prefs.get(1).getPrefs()));
		randomizer.setPlayerThreePreferences(convertPreferences(prefs.get(2).getPrefs()));
		randomizer.setPlayerFourPreferences(convertPreferences(prefs.get(3).getPrefs()));
		randomizer.setPlayerFivePreferences(convertPreferences(prefs.get(4).getPrefs()));
	}

	private String generateRandom() throws Exception
	{
		String[] roles = randomizer.randomize();
		StringBuilder shortFormRoles = new StringBuilder();
		for (int index = 0; index < 5; index++)
		{
			switch (index)
			{
				case 0:
				case 1:
					shortFormRoles.append(ColorUtil.wrapWithColorTag(
						nounCapitalize(roles[index]),
						Color.RED.darker())).append(" / "
					);
					for (Map.Entry<Integer, String> entry: randomizer.usernames.entrySet()) {
						if (Objects.equals(entry.getValue(), roles[index])) {
							randomizer.popPreference(entry.getKey(), index);
						}
					}
					break;
				case 2:
					shortFormRoles.append(ColorUtil.wrapWithColorTag(
						nounCapitalize(roles[index]),
						Color.GREEN.darker().darker())).append(" / "
					);
					for (Map.Entry<Integer, String> entry: randomizer.usernames.entrySet()) {
						if (Objects.equals(entry.getValue(), roles[index])) {
							randomizer.popPreference(entry.getKey(), index);
						}
					}
					break;
				case 3:
					shortFormRoles.append(ColorUtil.wrapWithColorTag(
						nounCapitalize(roles[index]),
						Color.YELLOW)).append(" / "
					);
					for (Map.Entry<Integer, String> entry: randomizer.usernames.entrySet()) {
						if (Objects.equals(entry.getValue(), roles[index])) {
							randomizer.popPreference(entry.getKey(), index);
						}
					}
					break;
				case 4:
					shortFormRoles.append(ColorUtil.wrapWithColorTag(
						nounCapitalize(roles[index]),
						Color.BLUE.darker())
					);
					for (Map.Entry<Integer, String> entry: randomizer.usernames.entrySet()) {
						if (Objects.equals(entry.getValue(), roles[index])) {
							randomizer.popPreference(entry.getKey(), index);
						}
					}
					break;
			}
		}
		return shortFormRoles.toString();
	}

	private String nounCapitalize(String noun)
	{
		return noun.substring(0, 1).toUpperCase() + noun.substring(1);
	}

	public int[] convertPreferences(List<MetaRoleInfo> prefs)
	{
		int[] result = {0, 0, 0, 0, 0};
		for (MetaRoleInfo pref : prefs)
		{
			result[pref.ordinal()] = 1;
		}
		return result;
	}

	@Provides
	RoleRandomizerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(RoleRandomizerConfig.class);
	}
}
