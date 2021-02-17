package com.rolerandomizer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("BA Role Randomizer")
public interface RoleRandomizerConfig extends Config
{
	@ConfigItem(
		keyName = "player1Name",
		name = "Current team",
		description = "Player in BA team"
	)
	default String teamOrder()
	{
		return "";
	}
}
