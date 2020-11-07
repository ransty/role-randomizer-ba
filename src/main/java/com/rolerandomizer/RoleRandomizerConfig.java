package com.rolerandomizer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("BA Role Randomizer")
public interface RoleRandomizerConfig extends Config
{
	@ConfigItem(
		keyName = "player1Name",
		name = "Player 1",
		description = "Player in BA team"
	)
	default String player1()
	{
		return "text1";
	}

	@ConfigItem(
			keyName = "Main Attack",
			name = "Main Attack?",
			description = "Add preference of main attack",
			position = 2
	)
	default boolean mainAttackBoolean() { return false; }

	@ConfigItem(
			keyName = "player2Name",
			name = "Player 2",
			description = "Player in BA team"
	)
	default String player2() { return "text2"; }

	@ConfigItem(
			keyName = "player3Name",
			name = "Player 4",
			description = "Player in BA team"
	)
	default String player3() { return "text3"; }

	@ConfigItem(
			keyName = "player4Name",
			name = "Player 4",
			description = "Player in BA team"
	)
	default String player4() { return "text4"; }

	@ConfigItem(
			keyName = "player5Name",
			name = "Player 5",
			description = "Player in BA team"
	)
	default String player5() { return "text5"; }
}
