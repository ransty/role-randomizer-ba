package com.rolerandomizer;

import com.rolerandomizer.exceptions.NoPermutationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.management.relation.Role;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
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
