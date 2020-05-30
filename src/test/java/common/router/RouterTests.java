package common.router;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class RouterTests {

    private static StringRouter router;
    private static String authentication = "protected";

    static class StringRouter extends GenericRouter<String, String, StringRouter> {
        public StringRouter() {
            super();
        }

        @Override protected StringRouter getThis() { return this; }
    }

    @BeforeAll
    public static void PrepRoutes() {
        router = new StringRouter()
            .ADD_AUTH("/authnull", "it should be just me on this route")
            .SET_AUTH(authentication)
            .ADD_AUTH("/auth", "permittedToSeeSecret", "a secret")
            .ADD("/unauth", "no secrets here")
            .ADD("/overwritten", "old action")
            .ADD("/overwritten", "new action!");
    }

    @Test
    public void TestAuthNull() throws Exception {
        List<String> actions = router.route("/authnull");
        assert actions != null;

        List<String> expectedActions = new ArrayList<>();

        expectedActions.add("it should be just me on this route");

        assertArrayEquals(actions.toArray(), expectedActions.toArray());
    }

    @Test
    public void TestAuthenticated() throws Exception {
        List<String> actions = router.route("/auth");
        assert actions != null;

        List<String> expectedActions = new ArrayList<>();

        expectedActions.add(authentication);
        expectedActions.add("permittedToSeeSecret");
        expectedActions.add("a secret");

        assertArrayEquals(actions.toArray(), expectedActions.toArray());
    }

    @Test
    public void TestUnauthenticated() throws Exception {
        List<String> actions = router.route("/unauth");
        assert actions != null;

        List<String> expectedActions = new ArrayList<>();

        expectedActions.add("no secrets here");

        assertArrayEquals(actions.toArray(), expectedActions.toArray());
    }

    @Test
    public void TestNull() throws Exception {
        List<String> actions = router.route("/null");
        assert actions == null;
    }

    @Test
    public void TestOverwrite() throws Exception {
        List<String> actions = router.route("/overwritten");
        assert actions != null;

        List<String> expectedActions = new ArrayList<>();

        expectedActions.add("new action!");

        assertArrayEquals(actions.toArray(), expectedActions.toArray());
    }
}
