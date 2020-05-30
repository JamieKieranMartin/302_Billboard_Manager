package common.router;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A router class to manage ActionLike actions on a Path. Http-Like.
 *
 * @author Jamie Martin
 * @author Perdana Bailey
 */
public abstract class GenericRouter<Path, ActionLike, RouterLike extends GenericRouter<Path, ActionLike, RouterLike>> {
    protected abstract RouterLike getThis();

    private ActionLike authenticatedAction;

    // <Path, Array<ActionLike> Hash Map to store the path to find those Actions on.
    // Multiple actions are allowable, enabling chaining ie: Authenticate, then Get
    protected final HashMap<Path, List<ActionLike>> routes = new HashMap<>();

    public GenericRouter() { }

    /**
     * Routes a given Path and returns the Actions to do.
     *
     * @param path The Path class.
     * @return List<ActionLike> The result found using the given Path.
     */
    public List<ActionLike> route(Path path) {
        // Attempt to get the actions from the given path, null if none.
        List<ActionLike> actions = routes.get(path);

        return actions;
    }

    /**
     * Add an array of Classes that extend Action to the routes path.
     *
     * @param path The Path you want to set, ie: String "/login".
     * @param actions The ActionLike actions you want to perform in order given, ie: Insert.class, GetSecret.class.
     * @return RouterLike Returns self/this which allows chaining of ADD().ADD().
     */
    public RouterLike ADD(Path path, ActionLike... actions) {
        routes.put(path, Arrays.asList(actions));
        return getThis();
    }

    /**
     * Add an auth Action that appends itself to the start of ADD_AUTH() paths.
     *
     * @param action The ActionLike action you want to perform.
     * @return RouterLike Returns self/this which allows chaining of ADD().ADD().
     */
    public RouterLike SET_AUTH(ActionLike action) {
        this.authenticatedAction = action;
        return getThis();
    }

    /**
     * Add an array of Classes that extend Action to the routes path with authentication checks.
     *
     * @param path The Path you want to set, ie: String "/login".
     * @param actions The actions you want to perform in order given, ie: Insert.class, GetSecret.class.
     * @return RouterLike Returns self/this which allows chaining of ADD().ADD().
     */
    public RouterLike ADD_AUTH(Path path, ActionLike... actions) {
        // Ensure there is an authentication class available.
        if (authenticatedAction == null) {
            ADD(path, actions);
            return getThis();
        }

        // Add the authentication class and the actions.
        List<ActionLike> actionList = new ArrayList<>();
        actionList.add(authenticatedAction);

        for (ActionLike action : actions) {
            actionList.add(action);
        }

        routes.put(path, actionList);
        return getThis();
    }
}
