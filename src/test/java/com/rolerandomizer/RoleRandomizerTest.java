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

import com.rolerandomizer.exceptions.NoPermutationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.HashMap;


public class RoleRandomizerTest {

    RoleRandomizer randomizer;
    HashMap<Integer, String> users;

    @Before
    public void setUp()
    {
        randomizer = new RoleRandomizer();
        int[] aacPrefs = new int[]{1, 1, 0, 1, 0};
        int[] hcdPrefs = new int[]{0, 0, 1, 1, 1};
        users = new HashMap<>();

        for (int i = 0; i < 5; i++)
        {
            users.put(i, "");
        }

        randomizer.setUsernames(users);
        randomizer.setPlayerOnePreferences(aacPrefs);
        randomizer.setPlayerTwoPreferences(hcdPrefs);
        randomizer.setPlayerThreePreferences(aacPrefs);
        randomizer.setPlayerFourPreferences(hcdPrefs);
        randomizer.setPlayerFivePreferences(aacPrefs);
    }

    @Test
    public void testSetUsernames()
    {
        assertEquals(randomizer.usernames, users);
    }

    @Test
    public void testSetPlayerOnePreferences()
    {
        int[] prefs = {1, 0, 1, 0, 1};
        randomizer.setPlayerOnePreferences(prefs);
        assertEquals(randomizer.playerOnePreferences, prefs);
    }

    @Test
    public void testSetPlayerTwoPreferences()
    {
        int[] prefs = {1, 1, 1, 1, 1};
        randomizer.setPlayerTwoPreferences(prefs);
        assertEquals(randomizer.playerTwoPreferences, prefs);
    }

    @Test
    public void testSetPlayerThreePreferences()
    {
        int[] prefs = {1, 0, 1, 1, 1};
        randomizer.setPlayerThreePreferences(prefs);
        assertEquals(randomizer.playerThreePreferences, prefs);
    }

    @Test
    public void testSetPlayerFourPreferences()
    {
        int[] prefs = {1, 0, 0, 0, 1};
        randomizer.setPlayerFourPreferences(prefs);
        assertEquals(randomizer.playerFourPreferences, prefs);
    }

    @Test
    public void testSetPlayerFivePreferences()
    {
        int[] prefs = {1, 1, 1, 0, 1};
        randomizer.setPlayerFivePreferences(prefs);
        assertEquals(randomizer.playerFivePreferences, prefs);
    }

    @Test
    public void testRandomize()
    {
        String[] roles;
        try
        {
            roles = randomizer.randomize();
            assertEquals(users.size(), roles.length);
            assertEquals(true, roles[0].contains(""));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test(expected = NoPermutationException.class)
    public void testRandomizeNoPerms() throws Exception
    {
            int[] noPrefs = {0, 0, 0, 0, 0};
            randomizer.setPlayerOnePreferences(noPrefs);
            randomizer.setPlayerTwoPreferences(noPrefs);
            randomizer.setPlayerThreePreferences(noPrefs);
            randomizer.setPlayerFourPreferences(noPrefs);
            randomizer.setPlayerFivePreferences(noPrefs);
            randomizer.randomize();
    }

    @Test()
    public void testRandomizeNoPrefs() throws Exception
    {
        randomizer.setPlayerFivePreferences(null);
        randomizer.setPlayerFourPreferences(null);
        randomizer.setPlayerThreePreferences(null);
        randomizer.setPlayerTwoPreferences(null);
        randomizer.setPlayerOnePreferences(null);
        randomizer.randomize();
    }

    @Test
    public void testIsPreferencesSet_notSet()
    {
        randomizer.setPlayerFivePreferences(null);
        assertEquals(false, randomizer.isPreferencesSet());
        randomizer.setPlayerFourPreferences(null);
        assertEquals(false, randomizer.isPreferencesSet());
        randomizer.setPlayerThreePreferences(null);
        assertEquals(false, randomizer.isPreferencesSet());
        randomizer.setPlayerTwoPreferences(null);
        assertEquals(false, randomizer.isPreferencesSet());
        randomizer.setPlayerOnePreferences(null);
        assertEquals(false, randomizer.isPreferencesSet());
    }

    @Test
    public void testGeneratePermutations()
    {
        int[] noPrefs = {0, 0, 0, 0, 0};
        randomizer.setPlayerOnePreferences(noPrefs);
        randomizer.setPlayerTwoPreferences(noPrefs);
        randomizer.setPlayerThreePreferences(noPrefs);
        randomizer.setPlayerFourPreferences(noPrefs);
        randomizer.setPlayerFivePreferences(noPrefs);
        try {
            randomizer.generatePermutations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
