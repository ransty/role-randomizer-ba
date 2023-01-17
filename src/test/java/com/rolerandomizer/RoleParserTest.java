package com.rolerandomizer;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.rolerandomizer.exceptions.CannotDetermineRolesException;
import com.rolerandomizer.exceptions.CannotParseArgException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RoleParserTest {
    private RoleParser parser;

    private static final List<MetaRoleInfo> FILL = Arrays.asList(MetaRoleInfo.values().clone());
    private static final List<MetaRoleInfo> A_H_C = ImmutableList.of(MetaRoleInfo.MAIN_ATTACKER,
            MetaRoleInfo.SECOND_ATTACKER, MetaRoleInfo.HEALER, MetaRoleInfo.COLLECTOR);
    private static final List<MetaRoleInfo> H_C_D = ImmutableList.of(MetaRoleInfo.HEALER, MetaRoleInfo.COLLECTOR,
            MetaRoleInfo.DEFENDER);

    private static final List<List<String>> GOOD_INPUTS = ImmutableList.of(
            ImmutableList.of("alice", "bob", "collguy", "defsabo", "egglad"),
            ImmutableList.of("lugal_ki_en", "fill", "ransty", "a/c/h", "loasted", "fill", "toohey", "fill", "equi"),
            ImmutableList.of("phil", "fill", "fill", "fill", "me", "m", "somebody", "anybody"),
            ImmutableList.of("dach", "dch", "mach", "fill", "cahd", "fill", "chad", "fill", "hesi")
    );

    private static final List<List<PlayerPrefs>> GOOD_OUTPUTS = ImmutableList.of(
            ImmutableList.of(
                    new PlayerPrefs("alice", FILL),
                    new PlayerPrefs("bob", FILL),
                    new PlayerPrefs("collguy", FILL),
                    new PlayerPrefs("defsabo", FILL),
                    new PlayerPrefs("egglad", FILL)
            ),
            ImmutableList.of(
                    new PlayerPrefs("equi", FILL),
                    new PlayerPrefs("loasted", FILL),
                    new PlayerPrefs("lugal_ki_en", FILL),
                    new PlayerPrefs("ransty", A_H_C),
                    new PlayerPrefs("toohey", FILL)
            ),
            ImmutableList.of(
                    new PlayerPrefs("anybody", FILL),
                    new PlayerPrefs("fill", FILL),
                    new PlayerPrefs("me", Collections.singletonList(MetaRoleInfo.MAIN_ATTACKER)),
                    new PlayerPrefs("phil", FILL),
                    new PlayerPrefs("somebody", FILL)
            ),
            ImmutableList.of(
                    new PlayerPrefs("cahd", FILL),
                    new PlayerPrefs("chad", FILL),
                    new PlayerPrefs("dach", H_C_D),
                    new PlayerPrefs("hesi", FILL),
                    new PlayerPrefs("mach", FILL)
            )
    );

    private static final List<List<String>> CANNOT_DETERMINE_INPUTS = ImmutableList.of(
            ImmutableList.of("not", "enough", "for5"),
            ImmutableList.of("too", "many", "names", "for", "five", "players"),
            ImmutableList.of("b2b_roles", "2/c/d", "a/c/h", "phil", "jim", "greg")
    );

    private static final List<List<String>> BAD_ARGS_INPUTS = ImmutableList.of(
            ImmutableList.of("v_not_role", "2/h/v", "p2", "p3", "p4", "b5"),
            ImmutableList.of("no_dupe_roles", "a/a/d", "p2", "p3", "p4", "b5"),
            ImmutableList.of("bad_slash", "a/ch", "p2", "p3", "p4", "b5"),
            ImmutableList.of("end_slash", "a/c/", "p2", "p3", "p4", "b5"),
            ImmutableList.of("fill_plus", "fill/c/d", "p2", "p3", "p4", "b5")
    );

    @Before
    public void setUp() {
        parser = new RoleParser();
    }

    @Test
    public void testUnambiguousInputs() {
        Assert.assertEquals("test case inputs + outputs should be same size",
                GOOD_OUTPUTS.size(), GOOD_INPUTS.size());
        for (int i = 0; i < GOOD_INPUTS.size(); i++) {
            try {
                List<String> inputs = GOOD_INPUTS.get(i);
                List<PlayerPrefs> prefs = parser.parse(inputs);
                Assert.assertEquals(GOOD_OUTPUTS.get(i), prefs);
            } catch (CannotParseArgException | CannotDetermineRolesException e) {
                Assert.fail(e.getMessage());
            }
        }
    }

    @Test
    public void testBadArgs() {
        for (List<String> inputs : BAD_ARGS_INPUTS) {
            try {
                parser.parse(inputs);
                Assert.fail("should have thrown");
            } catch (CannotDetermineRolesException e) {
                Assert.fail(e.getMessage());
            } catch (CannotParseArgException e) {
                //pass
            }
        }
    }

    @Test
    public void testCannotDetermine() {
        for (List<String> inputs : CANNOT_DETERMINE_INPUTS) {
            try {
                parser.parse(inputs);
                Assert.fail("should have thrown");
            } catch (CannotDetermineRolesException e) {
                // pass
            } catch (CannotParseArgException e) {
                Assert.fail(e.getMessage());
            }
        }
    }
}
