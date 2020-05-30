package common.models;

import client.components.table.DisplayAs;
import client.components.table.Editable;
import common.sql.SQLITE;
import common.utils.RandomFactory;

import java.io.Serializable;

/**
 * This class consists of the user's permissions object and its associated methods.
 *
 * @author Perdana Bailey
 * @author Hieu Nghia Huynh
 * @author Jamie Martin
 */
@SQLITE(type="FOREIGN KEY(id) REFERENCES User(id), FOREIGN KEY(username) REFERENCES User(username)")
public class Permissions implements Serializable, Editable {
    /**
     * The permissions ID.
     */
    @SQLITE(type="INTEGER PRIMARY KEY AUTOINCREMENT")
    public int id;

    /**
     * The permissions username.
     */
    @SQLITE(type="VARCHAR(255) NOT NULL UNIQUE")
    public String username;

    /**
     * The can create billboard permission status.
     */
    @SQLITE(type="BOOLEAN")
    public boolean canCreateBillboard = false;

    /**
     * The can edit billboard permission status.
     */
    @SQLITE(type="BOOLEAN")
    public boolean canEditBillboard  = false;

    /**
     * The can schedule billboard permission status.
     */
    @SQLITE(type="BOOLEAN")
    public boolean canScheduleBillboard = false;

    /**
     * The can edit user permission status.
     */
    @SQLITE(type="BOOLEAN")
    public boolean canEditUser = false;

    /**
     * An empty constructor just for creating the object.
     */
    public Permissions() {

    }

    /**
     * Permissions object constructor.
     *
     * @param username Permissions username.
     * @param canCreateBillboard Permissions canCreateBillboard permission.
     * @param canEditBillboard Permissions canEditBillboard permission.
     * @param canScheduleBillboard Permissions canScheduleBillboard permission.
     * @param canEditUser Permissions canEditUser permission.
     */
    public Permissions(String username, boolean canCreateBillboard, boolean canEditBillboard, boolean canScheduleBillboard, boolean canEditUser) {
        this.username = username;
        this.canCreateBillboard = canCreateBillboard;
        this.canEditBillboard = canEditBillboard;
        this.canScheduleBillboard = canScheduleBillboard;
        this.canEditUser = canEditUser;
    }

    /**
     * Permissions object constructor.
     *
     * @param id Permissions id.
     * @param username Permissions username.
     * @param canCreateBillboard Permissions canCreateBillboard permission.
     * @param canEditBillboard Permissions canEditBillboard permission.
     * @param canScheduleBillboard Permissions canScheduleBillboard permission.
     * @param canEditUser Permissions canEditUser permission.
     */
    public Permissions(int id, String username, boolean canCreateBillboard, boolean canEditBillboard, boolean canScheduleBillboard, boolean canEditUser) {
        this.id = id;
        this.username = username;
        this.canCreateBillboard = canCreateBillboard;
        this.canEditBillboard = canEditBillboard;
        this.canScheduleBillboard = canScheduleBillboard;
        this.canEditUser = canEditUser;
    }

    /**
     * Returns the permissions ID for the Client display.
     *
     * @return An integer that is the permissions ID.
     */
    @DisplayAs(value = "Id", index = 0)
    public int getId() {
        return id;
    }

    /**
     * Returns the permissions username for the Client display.
     *
     * @return An string that is the permissions username.
     */
    @DisplayAs(value = "Username", index = 1)
    public String getUsername() {
        return username;
    }

    /**
     * Returns the can create billboard permission for the Client display.
     *
     * @return The status of the can create billboard permission.
     */
    @DisplayAs(value = "Can Create Billboard", index = 2, editable = true)
    public Boolean getCanCreateBillboard() {
        return canCreateBillboard;
    }

    /**
     * Sets the can create billboard permission for the Client display.
     *
     * @param b The status of the can create billboard permission.
     */
    public void setCanCreateBillboard(Boolean b) { canCreateBillboard = b; }

    /**
     * Returns the can edit billboard permission for the Client display.
     *
     * @return The status of the can edit billboard permission.
     */
    @DisplayAs(value = "Can Edit Billboard", index = 3, editable = true)
    public Boolean getCanEditBillboard() {
        return canEditBillboard;
    }

    /**
     * Sets the can edit billboard permission for the Client display.
     *
     * @param b The status of the can edit billboard permission.
     */
    public void setCanEditBillboard(Boolean b) { canEditBillboard = b; }

    /**
     * Returns the can schedule billboard permission for the Client display.
     *
     * @return The status of the can schedule billboard permission.
     */
    @DisplayAs(value = "Can Schedule Billboard", index = 4, editable = true)
    public Boolean getCanScheduleBillboard() {
        return canScheduleBillboard;
    }

    /**
     * Sets the can schedule billboard permission for the Client display.
     *
     * @param b The status of the can schedule billboard permission.
     */
    public void setCanScheduleBillboard(Boolean b) { canScheduleBillboard = b; }

    /**
     * Returns the can edit billboard permission for the Client display.
     *
     * @return The status of the can edit billboard permission.
     */
    @DisplayAs(value = "Can Edit User", index = 5, editable = true)
    public Boolean getCanEditUser() {
        return canEditUser;
    }

    /**
     * Sets the can edit billboard permission for the Client display.
     *
     * @param b The status of the can edit billboard permission.
     */
    public void setCanEditUser(Boolean b) { canEditUser = b; }

    /**
     * Returns if the permission is editable for the Client display.
     *
     * @return The boolean that determines whether the permission is editable.
     */
    @Override
    public boolean isEditable() {
        return true;
    }

    /**
     * Generates a random permissions object with random variables.
     *
     * @param id The id of the user.
     * @param username The user's username.
     * @return A randomised permissions object.
     */
    public static Permissions Random(int id, String username) {
        return new Permissions(
            id,
            username,
            RandomFactory.Boolean(),
            RandomFactory.Boolean(),
            RandomFactory.Boolean(),
            RandomFactory.Boolean()
        );
    }
}
