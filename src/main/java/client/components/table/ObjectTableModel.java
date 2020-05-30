package client.components.table;

import client.services.DataService;
import common.swing.Notification;

import javax.swing.*;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * This class renders the Java Swing table model for the client.
 *
 * @author Jamie Martin
 */
public class ObjectTableModel<T> extends IObjectTableModel<T> {
    private Map<Integer, ColumnInfo> columnInfoMap;
    private DataService dataService;

    /**
     * Invokes a new display table.
     *
     * @param typeClass The class of the table being displayed.
     * @param dataService The data service that is used for the table.
     */
    public ObjectTableModel(Class<T> typeClass, DataService dataService) {
        this.dataService = dataService;

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(typeClass);
            this.columnInfoMap = new HashMap<>();

            // iterate through descriptors to build a information regarding table columns
            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                // gets the getter method
                Method m = pd.getReadMethod();
                // only uses methods with DisplayAs
                // determines table variables for a column
                DisplayAs displayAs = m.getAnnotation(DisplayAs.class);

                if (displayAs == null) continue;

                // display values
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.displayName = displayAs.value();
                columnInfo.index = displayAs.index();
                columnInfo.editable = displayAs.editable();

                // get the setter method
                if (displayAs.editable()) columnInfo.setterMethod = pd.getWriteMethod();

                // get the getter method
                columnInfo.getterMethod = m;
                columnInfo.propertyName = pd.getName();

                // add to hashmap of column info
                columnInfoMap.put(columnInfo.index, columnInfo);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Invokes the getter method for that column.
     *
     * @param type The class type of the getter.
     * @param columnIndex The column index of the getter.
     * @return The object being retrieved.
     * @throws Exception An exception thrown from the internal getterMethod.
     */
    @Override
    public Object getValueAt(T type, int columnIndex) throws Exception {
        return columnInfoMap.get(columnIndex).getterMethod.invoke(type);
    }

    /**
     * Gets the column count.
     *
     * @return An integer of the column count.
     */
    @Override
    public int getColumnCount() {
        return columnInfoMap.size();
    }

    /**
     * Gets the name of a column using the display name as defined in the DisplayAs annotation.
     *
     * @param columnIndex The column index where the name that is needed to be retrieved is from.
     * @return A string containing the name of the column.
     */
    @Override
    public String getColumnName(int columnIndex) {
        ColumnInfo columnInfo = columnInfoMap.get(columnIndex);

        if (columnInfo == null) throw new RuntimeException("No column found for index " + columnIndex);

        return columnInfo.displayName;
    }

    /**
     * Gets the class of a given column index.
     *
     * @param columnIndex The column index where the class that is needed to be retrieved is from.
     * @return The class containing the type of the column.
     */
    public Class<?> getColumnClass(int columnIndex) {
        ColumnInfo columnInfo = columnInfoMap.get(columnIndex);
        return columnInfo.getterMethod.getReturnType();
    }

    /**
     * Gets the field name of a particular column.
     *
     * @param columnIndex The column index where the field that is needed to be retrieved is from.
     * @return A string containing the field name.
     */
    @Override
    public String getFieldName(int columnIndex) {
        ColumnInfo columnInfo = columnInfoMap.get(columnIndex);
        return columnInfo.propertyName;
    }

    /**
     * Determines if the column is editable or not.
     *
     * @param columnIndex The column index where it is editable that is needed to be retrieved is from.
     * @return A boolean that determines if the column is editable.
     */
    @Override
    public boolean isColumnEditable(int columnIndex) {
        ColumnInfo columnInfo = columnInfoMap.get(columnIndex);
        return columnInfo.editable;
    }

    /**
     * Attempts to update the value on the server and reflects the changes to the table.
     *
     * @param type The object type of the intended field value.
     * @param columnIndex The column index of the intended field value location.
     * @param value The field value.
     * @return A boolean that determines whether the field value was updated or not.
     */
    @Override
    public boolean setObjectFieldValue(T type, int columnIndex, Object value) {
        ColumnInfo columnInfo = columnInfoMap.get(columnIndex);

        try {
            Object oldValue = columnInfo.getterMethod.invoke(type);

            if (columnInfo.setterMethod == null) return false;

            // invoke the setter method
            columnInfo.setterMethod.invoke(type, value);

            // attempt a Request to the server
            Boolean success = dataService.update(type);

            if (success) return true;
            else {
                // reinstate old value
                columnInfo.setterMethod.invoke(type, oldValue);
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper class to manage each column
     */
    private static class ColumnInfo {
        private Method setterMethod;
        private Method getterMethod;
        private int index;
        private String displayName;
        private String propertyName;
        private boolean editable;
    }
}
