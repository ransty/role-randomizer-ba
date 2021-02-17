package com.rolerandomizer;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Math;

public class RoleRandomizer
{

    private HashMap<Integer, String> usernames;

    private int[] playerOnePreferences;
    private int[] playerTwoPreferences;
    private int[] playerThreePreferences;
    private int[] playerFourPreferences;
    private int[] playerFivePreferences;

    public RoleRandomizer()
    {
        usernames = new HashMap<Integer, String>();
    }

    public ArrayList generatePermutations() throws Exception
    {
        ArrayList<int[]> permutations = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            if (this.playerOnePreferences[i] != 1) 
            {
                continue;
            }
            
            for (int j = 0; j < 5; j++)
            {
                if (j == i || this.playerTwoPreferences[j] != 1) 
                {
                    continue;
                }
                
                for (int k = 0; k < 5; k++)
                {
                    if (k == i || k == j || this.playerThreePreferences[k] != 1) 
                    {
                        continue;
                    }

                    for (int l = 0; l < 5; l++)
                    {
                        if (l == i || l == j || l == k || this.playerFourPreferences[l] != 1) 
                        {
                            continue;
                        }

                        for (int m = 0; m < 5; m++)
                        {
                            if (m == i || m == j || m == k || m == l || this.playerFivePreferences[m] != 1) 
                            {
                                continue;
                            }

                            int[] tempPermutation = {i, j, k, l, m};
                            permutations.add(tempPermutation);
                        }
                    }
                }
            }
        }
        return permutations;
    }

    public String[] randomize() throws Exception
    {
        ArrayList<int[]> perms = this.generatePermutations();
        if (perms.size() > 0)
        {
            int chosenIndex = (int) Math.floor(Math.random() * perms.size());
            return this.printRoles(perms.get(chosenIndex));
        }
        else
        {
            throw new NoPermutationException("No permutations to choose from");
        }
    }

    public void setUsernames(HashMap<Integer, String> usernames)
    {
        this.usernames = usernames;
    }

    public void setPlayerOnePreferences(int[] prefs)
    {
        this.playerOnePreferences = prefs;
    }

    public void setPlayerTwoPreferences(int[] prefs)
    {
        this.playerTwoPreferences = prefs;
    }

    public void setPlayerThreePreferences(int[] prefs)
    {
        this.playerThreePreferences = prefs;
    }

    public void setPlayerFourPreferences(int[] prefs)
    {
        this.playerFourPreferences = prefs;
    }

    public void setPlayerFivePreferences(int[] prefs)
    {
        this.playerFivePreferences = prefs;
    }

    private String[] printRoles(int[] ints) throws Exception
    {
        if (this.usernames.size() < 5)
        {
            throw new MissingUsernamesException("Not all usernames have been set, unable to randomize");
        }
        String[] roles = new String[5];
        for (int i = 0; i < ints.length; i++)
        {
            roles[ints[i]] = this.usernames.get(i);
        }
        return roles;
    }
}

class NoPermutationException extends Exception
{
    public NoPermutationException(String message)
    {
        super(message);
    }
}

class MissingUsernamesException extends Exception
{
    public MissingUsernamesException(String message)
    {
        super(message);
    }
}
