package com.rolerandomizer;

import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Strings sent as arguments could be interpreted as player names or roles
 */
@RequiredArgsConstructor
public enum InputCandidate
{
	NAME(Pattern.compile("[A-Za-z0-9_-]{1,12}")),
	ROLE(Pattern.compile("[m2ahcd]{1,5}|fill|" + MetaRoleInfo.SLASH_SEPARATED_MATCHER));

	@Getter
	private final Pattern pattern;
}
