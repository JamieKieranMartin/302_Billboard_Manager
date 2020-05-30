package common.models;

import client.components.table.DisplayAs;
import client.components.table.Editable;
import common.sql.SQLITE;
import common.utils.Picture;
import common.utils.RandomFactory;

import java.awt.*;
import java.io.*;

/**
 * This class consists of the billboard object and its associated methods.
 *
 * @author Perdana Bailey
 * @author Hieu Nghia Huynh
 * @author Jamie Martin
 */
@SQLITE(type="FOREIGN KEY(userId) REFERENCES User(id)")
public class Billboard implements Serializable, Editable {
    /**
     * The billboard ID.
     */
    @SQLITE(type="INTEGER PRIMARY KEY AUTOINCREMENT")
    public int id;

    /**
     * The billboard name.
     */
    @SQLITE(type="VARCHAR(255) NOT NULL UNIQUE")
    public String name;

    /**
     * The billboard message.
     */
    @SQLITE(type="VARCHAR(255)")
    public String message;

    /**
     * The billboard message colour.
     */
    @SQLITE(type="VARCHAR(7) DEFAULT \"#000000\"")
    public String messageColor = "#000000";

    /**
     * The billboard picture.
     */
    @SQLITE(type="BLOB")
    public String picture;

    /**
     * The billboard background colour.
     */
    @SQLITE(type="VARCHAR(7) DEFAULT \"#ffffff\"")
    public String backgroundColor = "#ffffff";

    /**
     * The billboard information/text.
     */
    @SQLITE(type="VARCHAR(255)")
    public String information;

    /**
     * The billboard information/text colour.
     */
    @SQLITE(type="VARCHAR(7) DEFAULT \"#000000\"")
    public String informationColor = "#000000";

    /**
     * The billboard locked status.
     */
    @SQLITE(type="BOOLEAN")
    public boolean locked = false;

    /**
     * The billboard owner userID.
     */
    @SQLITE(type="INTEGER NOT NULL")
    public int userId;

    /**
     * An empty constructor just for creating the object.
     */
    public Billboard() {

    }

    /**
     * Billboard object constructor with an id.
     *
     * @param id Database billboard ID as an int.
     * @param name Billboard name as a string.
     * @param message Billboard message as a string.
     * @param messageColor Billboard message colour as a string.
     * @param picture Billboard picture as a byte array.
     * @param backgroundColor Billboard background colour as a string.
     * @param information Billboard information as a string.
     * @param informationColor Billboard information colour as a string.
     * @param locked Billboard scheduled as a boolean.
     * @param userId Owner ID as an int.
     */
    public Billboard(int id,
                     String name,
                     String message,
                     String messageColor,
                     String picture,
                     String backgroundColor,
                     String information,
                     String informationColor,
                     boolean locked,
                     int userId) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.messageColor = messageColor;
        this.picture = picture;
        this.backgroundColor = backgroundColor;
        this.information = information;
        this.informationColor = informationColor;
        this.locked = locked;
        this.userId = userId;
    }

    /**
     * Billboard object constructor without an id.
     *
     * @param name Billboard name as a string.
     * @param message Billboard message as a string.
     * @param messageColor Billboard message colour as a string.
     * @param picture Billboard picture as a byte array.
     * @param backgroundColor Billboard background colour as a string.
     * @param information Billboard information as a string.
     * @param informationColor Billboard information colour as a string.
     * @param locked Billboard scheduled as a boolean.
     * @param userId Owner ID as an int.
     */
    public Billboard(
        String name,
        String message,
        String messageColor,
        String picture,
        String backgroundColor,
        String information,
        String informationColor,
        boolean locked,
        int userId) {
        this.name = name;
        this.message = message;
        this.messageColor = messageColor;
        this.picture = picture;
        this.backgroundColor = backgroundColor;
        this.information = information;
        this.informationColor = informationColor;
        this.locked = locked;
        this.userId = userId;
    }

    /**
     * Returns the billboard ID for the Client display.
     *
     * @return An integer that is the billboard ID.
     */
    @DisplayAs(value = "Id", index = 0)
    public int getId() {
        return id;
    }

    /**
     * Returns the billboard name for the Client display.
     *
     * @return A string that is the billboard name.
     */
    @DisplayAs(value = "Billboard Name", index = 1)
    public String getName() {
        return name;
    }

    /**
     * Returns the billboard message for the Client display.
     *
     * @return A string that is the billboard message.
     */
    @DisplayAs(value = "Message", index = 2, editable = true)
    public String getMessage() {
        return message;
    }

    /**
     * Sets the billboard message for the Client.
     *
     * @param m A string that is the billboard message.
     */
    public void setMessage(String m) { message = m; }

    /**
     * Returns the billboard message colour for the Client display.
     *
     * @return The colour that is the billboard message colour.
     */
    @DisplayAs(value = "Message Colour", index = 3, editable = true)
    public Color getMessageColor() {
        return Color.decode(messageColor);
    }

    /**
     * Sets the billboard message colour for the Client.
     *
     * @param c A colour that is the billboard message colour.
     */
    public void setMessageColor(Color c) {
        messageColor = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    /**
     * Returns the billboard picture for the Client display.
     *
     * @return The picture that is the billboard picture.
     */
    @DisplayAs(value = "Picture", index = 4, editable = true)
    public Picture getPicture() {
        return new Picture(picture);
    }

    /**
     * Sets the billboard picture for the Client.
     *
     * @param p A picture that is the billboard message colour.
     */
    public void setPicture(Picture p) {
        picture = p.data;
    }

    /**
     * Returns the billboard background colour for the Client display.
     *
     * @return The colour that is the billboard background colour.
     */
    @DisplayAs(value = "Background Colour", index = 5, editable = true)
    public Color getBackgroundColor() {
        return Color.decode(backgroundColor);
    }

    /**
     * Sets the billboard background colour for the Client.
     *
     * @param c A colour that is the billboard background colour.
     */
    public void setBackgroundColor(Color c) {
        backgroundColor = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    /**
     * Returns the billboard information for the Client display.
     *
     * @return The string that is the billboard information.
     */
    @DisplayAs(value = "Information", index = 6, editable = true)
    public String getInformation() {
        return information;
    }

    /**
     * Sets the billboard information for the Client.
     *
     * @param i A string that is the billboard information.
     */
    public void setInformation(String i) {
        information = i;
    }

    /**
     * Returns the billboard information colour for the Client display.
     *
     * @return The colour that is the billboard information colour.
     */
    @DisplayAs(value = "Information Colour", index = 7, editable = true)
    public Color getInformationColor() {
        return Color.decode(informationColor);
    }

    /**
     * Sets the billboard information colour for the Client.
     *
     * @param c A colour that is the billboard information colour.
     */
    public void setInformationColor(Color c) {
        informationColor = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    /**
     * Returns the billboard locked status for the Client display.
     *
     * @return The boolean that is the billboard locked status.
     */
    @DisplayAs(value = "Locked", index = 8)
    public Boolean getLocked() {
        return locked;
    }

    /**
     * Returns the billboard owner id for the Client display.
     *
     * @return The integer that is the billboard owner id.
     */
    @DisplayAs(value = "User Id", index = 9)
    public int getUserId() {
        return userId;
    }

    /**
     * Returns if the billboard is editable for the Client display.
     *
     * @return The boolean that determines whether the billboard is editable.
     */
    @Override
    public boolean isEditable() {
        return true;
    }

    /**
     * Generates a random billboard object with random variables.
     *
     * @param userId the id of the user creating the billboard.
     * @return A randomised billboard.
     */
    public static Billboard Random(int userId) {
        return new Billboard(
            RandomFactory.String(),
            RandomFactory.String(),
            RandomFactory.Color(),
            null,
            RandomFactory.Color(),
            RandomFactory.String(),
            RandomFactory.Color(),
            false,
            userId
        );
    }
}
