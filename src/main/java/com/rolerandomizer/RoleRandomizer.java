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
