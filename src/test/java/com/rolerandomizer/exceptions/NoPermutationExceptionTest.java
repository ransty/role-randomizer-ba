package com.rolerandomizer.exceptions;

import com.rolerandomizer.RoleRandomizer;
import org.junit.Test;

public class NoPermutationExceptionTest
{
    @Test(expected = NoPermutationException.class)
    public void testNoPermutationException() throws Exception
    {
        RoleRandomizer rr = new RoleRandomizer();
        int[] badPrefs = new int[]{1, 0, 0, 0, 0};
        rr.setPlayerOnePreferences(badPrefs);
        rr.setPlayerTwoPreferences(badPrefs);
        rr.setPlayerThreePreferences(badPrefs);
        rr.setPlayerFourPreferences(badPrefs);
        rr.setPlayerFivePreferences(badPrefs);
        // should throw an exception because there are no possible permutations
        rr.randomize();
    }
}
