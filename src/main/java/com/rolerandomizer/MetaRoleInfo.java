package com.rolerandomizer;

import com.google.common.collect.ImmutableSet;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Not to be confused with the regular BA role, this describes a more specific team role in the context of
 * the BA metagame. Currently, it distinguishes between main and second attacker roles in funs.
 *
 * @see com.rolerandomizer.BaRole
 */
@RequiredArgsConstructor
public enum MetaRoleInfo
{
	MAIN_ATTACKER("Main attacker", "m", BaRole.ATTACKER, ImmutableSet.of("m", "(?<!2)a")),
	SECOND_ATTACKER("2nd attacker", "2", BaRole.ATTACKER, ImmutableSet.of("2", "2a", "a")),
	HEALER("Healer", "h", BaRole.HEALER, ImmutableSet.of("h")),
	COLLECTOR("Collector", "c", BaRole.COLLECTOR, ImmutableSet.of("c")),
	DEFENDER("Defender", "d", BaRole.DEFENDER, ImmutableSet.of("d"));

	// one or more of any of the possible matches, separated by slashes
	public static final String SLASH_SEPARATED_MATCHER;

	static
	{
		Set<String> all = new HashSet<>();
		all.addAll(MAIN_ATTACKER.matches);
		all.addAll(SECOND_ATTACKER.matches);
		all.addAll(HEALER.matches);
		all.addAll(COLLECTOR.matches);
		all.addAll(DEFENDER.matches);
		String ors = "[" + String.join("|", all) + "]";
		SLASH_SEPARATED_MATCHER = ors + "(?:/" + ors + ")*";
	}

	@Getter
	private final String fullName;
	@Getter
	private final String shortName;
	@Getter
	private final BaRole role;
	@Getter
	private final Set<String> matches;

}
