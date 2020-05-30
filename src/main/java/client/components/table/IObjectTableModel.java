package client.components.table;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * This class makes the Java Swing table model for the client.
 *
 * @author Jamie Martin
 */
public abstract class IObjectTableModel<T> extends AbstractTableModel {
    // The list of objects
    private List<T> objectRows = new ArrayList<>();

    /**
     * Get the list of all the rows from the table.
     *
     * @return A list of the specific type for each row.
     */
    public List<T> getObjectRows() {
        return objectRows;
    }

    /**
     * Set the list of all the rows from the table.
     *
     * @param objectRows A list of the specific type for each row.
     */
    public void setObjectRows(List<T> objectRows) {
        this.objectRows = objectRows;
    }

    /**
     * Get row count from the list of all rows.
     *
     * @return The count of rows.
     */
    @Override
    public int getRowCount() {
        return objectRows.size();
    }

    /**
     * Get the value of the given cell row and integer.
     *
     * @param rowIndex The row index of the cell.
     * @param columnIndex The column index of the cell.
     * @return The object value that is in the cell.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            T t = objectRows.get(rowIndex);
            return getValueAt(t, columnIndex);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Determines if the cell is editable.
     *
     * @param rowIndex The row index of the cell.
     * @param columnIndex The column index of the cell.
     * @return A boolean which determines if the cell is editable.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        T t = objectRows.get(rowIndex);
        if(t instanceof Editable){
            if(!((Editable) t).isEditable()){
                return false;
            }
        }
        return isColumnEditable(columnIndex);
    }

    /**
     * Sets the value using the given object at the cell row and integer.
     *
     * @param value This is the value to set the cell to.
     * @param row The row index of the cell.
     * @param column The column index of the cell.
     */
    public void setValueAt(Object value, int row, int column) {
        if(!isCellEditable(row, column)){
            return;
        }
        T t = objectRows.get(row);
        if(setObjectFieldValue(t, column, value)){
            fireTableCellUpdated(row, column);
        }
    }

    /**
     * Must implement if a column is editable.
     *
     * @param columnIndex The column index of the cell.
     * @return A boolean which determines if the column is editable.
     */
    public abstract boolean isColumnEditable(int columnIndex);

    /**
     * The method that gets the field value.
     *
     * @param type The object type of the value.
     * @param columnIndex The column index location of the value.
     * @return The value object.
     * @throws Exception Generic exception thrown when calling this function.
     */
    public abstract Object getValueAt(T type, int columnIndex) throws Exception;

    /**
     * The method that sets the field value.
     *
     * @param type The object type of the value.
     * @param columnIndex The column index location of the value.
     * @param value The value object being set.
     * @return A boolean representing if the field was set.
     */
    public abstract boolean setObjectFieldValue(T type, int columnIndex, Object value);

    /**
     * The method that retrieves the column name.
     *
     * @param columnIndex The column index location of the name.
     * @return The string of the column name.
     */
    @Override
    public abstract String getColumnName(int columnIndex);

    /**
     * The method that retrieves the field name.
     *
     * @param columnIndex The column index location of the field.
     * @return The string of the field name.
     */
    public abstract String getFieldName(int columnIndex);
}
