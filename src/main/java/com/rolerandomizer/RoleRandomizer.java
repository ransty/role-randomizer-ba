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
import com.rolerandomizer.exceptions.NoPermutationException;

public class RoleRandomizer
{

    public HashMap<Integer, String> usernames;

    public int[] playerOnePreferences;
    public int[] playerTwoPreferences;
    public int[] playerThreePreferences;
    public int[] playerFourPreferences;
    public int[] playerFivePreferences;

    public RoleRandomizer()
    {
        usernames = new HashMap<Integer, String>();
    }

    public ArrayList generatePermutations() throws Exception
    {
        ArrayList<int[]> permutations = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            if (playerOnePreferences[i] != 1)
            {
                continue;
            }
            
            for (int j = 0; j < 5; j++)
            {
                if (j == i || playerTwoPreferences[j] != 1)
                {
                    continue;
                }
                
                for (int k = 0; k < 5; k++)
                {
                    if (k == i || k == j || playerThreePreferences[k] != 1)
                    {
                        continue;
                    }

                    for (int l = 0; l < 5; l++)
                    {
                        if (l == i || l == j || l == k || playerFourPreferences[l] != 1)
                        {
                            continue;
                        }

                        for (int m = 0; m < 5; m++)
                        {
                            if (m == i || m == j || m == k || m == l || playerFivePreferences[m] != 1)
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
        if (isPreferencesSet())
        {
            ArrayList<int[]> perms = generatePermutations();
            if (perms.size() > 0)
            {
                int chosenIndex = (int) Math.floor(Math.random() * perms.size());
                return printRoles(perms.get(chosenIndex));
            }
            else
            {
                throw new NoPermutationException("No permutations to choose from");
            }
        }
        return null;
    }

    public boolean isPreferencesSet()
    {
        if (playerOnePreferences != null && playerOnePreferences.length > 0)
        {
            if (playerTwoPreferences != null && playerTwoPreferences.length > 0)
            {
                if (playerThreePreferences != null && playerThreePreferences.length > 0)
                {
                    if (playerFourPreferences != null && playerFourPreferences.length > 0)
                    {
                        if (playerFivePreferences != null && playerFivePreferences.length > 0)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void setUsernames(HashMap<Integer, String> usernames)
    {
        this.usernames = usernames;
    }

    public void setPlayerOnePreferences(int[] prefs)
    {
        playerOnePreferences = prefs;
    }

    public void setPlayerTwoPreferences(int[] prefs)
    {
        playerTwoPreferences = prefs;
    }

    public void setPlayerThreePreferences(int[] prefs)
    {
        playerThreePreferences = prefs;
    }

    public void setPlayerFourPreferences(int[] prefs)
    {
        playerFourPreferences = prefs;
    }

    public void setPlayerFivePreferences(int[] prefs)
    {
        playerFivePreferences = prefs;
    }

    private String[] printRoles(int[] ints) throws Exception
    {
        String[] roles = new String[5];
        for (int i = 0; i < ints.length; i++)
        {
            roles[ints[i]] = usernames.get(i);
        }
        return roles;
    }
}

