package com.rolerandomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RoleParser
{
	private static final List<MetaRoleInfo> ALL_ROLES = Arrays.asList(MetaRoleInfo.values().clone());

	private final Set<Set<PlayerPrefs>> validPrefSets = new HashSet<>();
	private final List<String> args = new ArrayList<>();
	private final List<Set<InputCandidate>> argCandidates = new ArrayList<>();

	/**
	 * Assumptions:
	 * - rando is requested for 5 players
	 * - player names contain no spaces
	 * - no specified role means any role (fill)
	 * - correct role formats include:
	 * -- any combination of "m2ahcd" chars, concatenated without repeats, e.g., "2cd" good, "cadah" bad
	 * -- any slash-separated combination of the above, also without repeats, e.g., "2/c/d" good, "ahc/d" bad
	 * -- the exact string "fill", no more and no less
	 *
	 * @param inputs list of string input from the command (split by whitespace)
	 * @return list of PlayerPrefs, in lexicographical order by player name
	 */
	public List<PlayerPrefs> parse(List<String> inputs) throws CannotParseArgException, CannotDetermineRolesException
	{
		validPrefSets.clear();
		argCandidates.clear();
		args.clear();
		args.addAll(inputs);
		determineCandidates();
		determinePrefs();
		// out of all valid results, result with shortest char count in player names is likely most sensible
		Set<PlayerPrefs> bestResult = validPrefSets.stream()
			.min(Comparator.comparingInt(set -> set.stream().mapToInt(prefs -> prefs.getName().length()).sum()))
			.orElseThrow(CannotDetermineRolesException::new);
		return bestResult.stream()
			.sorted(Comparator.comparing(PlayerPrefs::getName))
			.collect(Collectors.toList());
	}

	private void determineCandidates() throws CannotParseArgException
	{
		// First determine whether each word could be NAME, ROLE, or both
		for (String input : args)
		{
			Set<InputCandidate> candidates = new HashSet<>();
			for (InputCandidate ic : InputCandidate.values())
			{
				boolean match = ic.getPattern().matcher(input).matches();
				switch (ic)
				{
					case NAME:
						break;
					case ROLE:
						if (!input.equals("fill"))
						{
							// Also make sure that the roles have no dupes
							Set<Character> charSet = input.chars()
								.mapToObj(e -> (char) e)
								.filter(e -> e != '/')
								.collect(Collectors.toSet());
							String noSlashInput = input.replace("/", "");
							match &= charSet.size() == noSlashInput.length();
						}
						break;
				}
				if (match)
				{
					candidates.add(ic);
				}
			}
			if (candidates.isEmpty())
			{
				throw new CannotParseArgException(input);
			}
			argCandidates.add(candidates);
		}
	}

	private void determinePrefs()
	{
		recurse(argCandidates.size() - 1, new ArrayList<>());
	}

	// i = current index of args, pending = running list of sub-divisions
	private void recurse(int i, List<PlayerPrefs> pending)
	{
		// base cases: reached beginning of input, or have more than 5 players
		if (i == -1)
		{
			if (isValid(pending))
			{
				validPrefSets.add(new HashSet<>(pending));
			}
			return;
		}
		else if (isInvalid(pending))
		{
			return;
		}
		String word = args.get(i);
		Set<InputCandidate> cands = argCandidates.get(i);
		PlayerPrefs lastPrefs = null;
		if (!pending.isEmpty())
		{
			lastPrefs = pending.get(pending.size() - 1);
		}
		if (cands.contains(InputCandidate.ROLE))
		{
			// can't have roles b2b, and can't start args with role
			if ((lastPrefs == null || !lastPrefs.getName().isEmpty()) && i > 0)
			{
				List<PlayerPrefs> newPrefs = new ArrayList<>(pending);
				newPrefs.add(new PlayerPrefs("", prefsFrom(word)));
				recurse(i - 1, newPrefs);
			}
		}
		if (cands.contains(InputCandidate.NAME))
		{
			// start a new name if we have no results yet, or the last thing we found was a name
			if (lastPrefs == null || !lastPrefs.getName().isEmpty())
			{
				List<PlayerPrefs> newPrefs = new ArrayList<>(pending);
				newPrefs.add(new PlayerPrefs(word, ALL_ROLES));
				recurse(i - 1, newPrefs);
			}
			// if we have a role but not a name, pair that name with that role
			if (lastPrefs != null && lastPrefs.getName().isEmpty())
			{
				List<PlayerPrefs> newPrefs = new ArrayList<>(pending);
				newPrefs.remove(newPrefs.size() - 1);
				newPrefs.add(new PlayerPrefs(word, lastPrefs.getPrefs()));
				recurse(i - 1, newPrefs);
			}
		}
	}

	private List<MetaRoleInfo> prefsFrom(String roleStr)
	{
		if (roleStr.equals("fill"))
		{
			return ALL_ROLES;
		}
		return Arrays.stream(MetaRoleInfo.values())
			.filter(info -> info.getMatches().stream()
				.anyMatch(s -> Pattern.compile(s).matcher(roleStr).find()))
			.collect(Collectors.toList());
	}

	// prefs are explicitly valid if there are five of them with non-empty, non-repeated names
	// note: it's possible for a pending list to be neither valid nor invalid (not enough info)
	private boolean isValid(List<PlayerPrefs> pending)
	{
		if (pending.size() != 5)
		{
			return false;
		}
		Set<String> names = new HashSet<>();
		for (PlayerPrefs pref : pending)
		{
			String name = pref.getName();
			if (name.isEmpty() || names.contains(name) || name.length() > 12)
			{
				return false;
			}
			names.add(name);
		}
		return true;
	}

	// parsing is explicitly invalid if any of the following are true:
	// >5 players, duplicate player names, multiple empty names, a player with a name longer than 12 chars
	// note: one empty name is ok since it might not have been parsed yet
	private boolean isInvalid(List<PlayerPrefs> pending)
	{
		if (pending.size() > 5)
		{
			return true;
		}
		Set<String> names = new HashSet<>();
		boolean hasEmptyNames = false;
		for (PlayerPrefs pref : pending)
		{
			String name = pref.getName();
			if (name.isEmpty())
			{
				if (hasEmptyNames)
				{
					return true;
				}
				hasEmptyNames = true;
			}
			if (names.contains(name) || name.length() > 12)
			{
				return true;
			}
			names.add(name);
		}
		return false;
	}
}

