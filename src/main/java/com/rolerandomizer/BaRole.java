package com.rolerandomizer;

import java.awt.Color;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.client.util.Text;

@RequiredArgsConstructor
public enum BaRole
{
	ATTACKER(Color.RED.darker()),
	DEFENDER(Color.BLUE.darker()),
	COLLECTOR(Color.YELLOW),
	HEALER(Color.GREEN.darker().darker());

	@Getter
	private final Color color;

	public String displayName()
	{
		return Text.titleCase(this);
	}
}
