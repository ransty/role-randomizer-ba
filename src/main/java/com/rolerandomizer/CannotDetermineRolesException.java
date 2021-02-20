package com.rolerandomizer;

public class CannotDetermineRolesException extends Exception
{
	public CannotDetermineRolesException()
	{
		super("Cannot determine role prefs for all 5 players");
	}
}
