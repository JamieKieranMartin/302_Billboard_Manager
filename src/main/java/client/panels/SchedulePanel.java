package client.panels;

import client.components.table.*;
import client.services.BillboardService;
import client.services.ScheduleService;
import client.services.SessionService;
import common.models.*;
import common.swing.Notification;
import common.utils.Picture;
import common.utils.scheduling.Day;
import common.utils.scheduling.DayOfWeek;
import common.utils.scheduling.Time;
import common.utils.session.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
/**
 * This class renders the Java Swing schedule panel for the client.
 *
 * @author Trevor Waturuocha
 */
public class SchedulePanel extends JPanel implements ActionListener {

    IObjectTableModel<Schedule> tableModel;
    JTable table;
    Container buttonContainer = new Container();
    JButton createButton = new JButton("Create Schedule"),
        refreshButton = new JButton("Refresh"),
        showButton = new JButton("Show Schedule"),
        deleteButton = new JButton("Delete Selected");
    String selected;

    /**
     * The Schedule Panel constructor that generates the SchedulePanel GUI.
     */
    public SchedulePanel() {
        // Get session
        Session session = SessionService.getInstance();

        // add the action listeners
        createButton.addActionListener(this);
        refreshButton.addActionListener(this);
        deleteButton.addActionListener(this);
        showButton.addActionListener(this);

        // Disable schedule button if user is not permitted
        if (!session.permissions.canScheduleBillboard) {
            createButton.setEnabled(false);
        }
        deleteButton.setEnabled(false);

        // Getting table data and configuring table
        tableModel = new ObjectTableModel(Schedule.class, null);
        tableModel.setObjectRows(ScheduleService.getInstance().refresh());
        table = new JTable(tableModel);
        setupSelection();
        JScrollPane pane = new JScrollPane(table);

        // Add buttons to container
        buttonContainer.setLayout(new FlowLayout());
        buttonContainer.add(createButton);
        buttonContainer.add(deleteButton);
        buttonContainer.add(showButton);
        buttonContainer.add(refreshButton);

        // Add components to frame
        setLayout(new BorderLayout());
        add(buttonContainer, BorderLayout.NORTH);
        add(pane, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Set up the table selection logic.
     */
    public void setupSelection() {
        table.setAutoCreateRowSorter(true);
        table.setCellSelectionEnabled(true);
        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(e -> {
            selected = (String)table.getModel().getValueAt(table.getSelectedRow(), 1);
            Session session = SessionService.getInstance();

            // if the current session can schedule billboards then allow them to delete
            if (selected != null && session.permissions.canScheduleBillboard) {
                deleteButton.setEnabled(true);
            } else {
                deleteButton.setEnabled(false);
            }
        });
    }

    /**
     * Adding listener events for the schedule panel buttons.
     *
     * @param e Pass through the action event to manage the respective action.
     */
    @Override
    // Adding listener events for the user panel buttons.
    public void actionPerformed(ActionEvent e) {
        // Check if create schedule button is pressed
        if(e.getSource() == createButton) {
            try {
                // Setting up billboard dropdown menu
                List<Billboard> billboardList = BillboardService.getInstance().billboards;
                List<String> billboardNames = billboardList.stream().map(b -> b.name).collect(Collectors.toList());

                JComboBox billboards = new JComboBox(new DefaultComboBoxModel(billboardNames.toArray()));

                JComboBox days = new JComboBox(new DefaultComboBoxModel(getNames(DayOfWeek.class)));

                // Setting up start time spinner
                SpinnerDateModel startModel = new SpinnerDateModel();
                JSpinner startTime = new JSpinner(startModel);
                startTime.setEditor(new JSpinner.DateEditor(startTime,"H:mm"));
                // Setting up duration spinner
                SpinnerNumberModel durModel = new SpinnerNumberModel(1, 1, 1440, 1);
                JSpinner duration = new JSpinner(durModel);
                // Setting up interval spinner
                SpinnerNumberModel intModel = new SpinnerNumberModel(1, 0, 60, 1);
                JSpinner interval = new JSpinner(intModel);
                // Setting up components for Schedule dialog box
                final JComponent[] components = new JComponent[]{
                    new JLabel("Select a billboard:"),
                    billboards,
                    new JLabel("Select day to show"),
                    days,
                    new JLabel("Billboard start time:"),
                    startTime,
                    new JLabel("Billboard duration:"),
                    duration,
                    new JLabel("Billboard interval:"),
                    interval
                };
                int result = JOptionPane.showConfirmDialog(this, components, "Schedule a billboard", JOptionPane.PLAIN_MESSAGE);
                // If OK button is clicked, update schedule table
                if (result == JOptionPane.OK_OPTION) {
                    // If billboard is not selected, display warning message
                    if (billboards.getSelectedItem() == null) {
                        Notification.display("Billboard was not selected. Please try again");
                        // If billboard is not selected, display warning message
                    } else if (startTime.getValue() == null || duration.getValue() == null || interval.getValue() == null ) {
                        Notification.display("One of the schedule values are empty. Please try again");
                        // Else populate table
                    } else {
                        Schedule schedule = new Schedule();
                        schedule.billboardName = ((String)billboards.getSelectedItem());
                        schedule.dayOfWeek = days.getSelectedIndex();
                        schedule.start = Time.timeToMinute((Date) startTime.getValue());
                        schedule.duration = (Integer) duration.getValue();
                        schedule.interval = (Integer) interval.getValue();

                        tableModel.setObjectRows(ScheduleService.getInstance().insert(schedule));
                        tableModel.fireTableDataChanged();
                    }
                }
            }
            catch (Exception ex) {
                Notification.display(ex.getMessage());
            }
        }
        // Check if delete button is pressed
        if (e.getSource() == deleteButton) {
            var scheduleList = tableModel.getObjectRows();
            Schedule s = scheduleList.stream().filter(x -> x.billboardName.equals(selected)).findFirst().get();
            tableModel.setObjectRows(ScheduleService.getInstance().delete(s));
            tableModel.fireTableDataChanged();
        }
        // Check if show button is pressed
        if(e.getSource() == showButton){
            try {
                // Setting up calendar table
                DefaultTableModel mtblCalendar = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
                JTable tblCalendar = new JTable(mtblCalendar);
                tblCalendar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Turn off auto resizing so user can scroll through data
                List<Day> day = ScheduleService.getInstance().getSchedule();
                int max = 0;
                for (Day time : day) {
                    if (time.times.size() > max) {
                        max = time.times.size(); // Getting maximum size for table
                    }
                }
                String[] headers = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                // Inserting columns
                for (int i = 0; i < 7; i++){
                    mtblCalendar.addColumn(headers[i]);
                }
                // Inserting empty strings for uneven columns
                for (int i = 0; i < 7; i++){
                    for (int j = day.get(i).times.size(); j <= max; j++){
                        if (j != max){
                            day.get(i).times.add(" ");
                        }
                    }
                }
                // Inserting rows
                for (int i = 0; i < max; i++) {
                    mtblCalendar.addRow(new Object[]{day.get(0).times.get(i), day.get(1).times.get(i), day.get(2).times.get(i), day.get(3).times.get(i), day.get(4).times.get(i), day.get(5).times.get(i), day.get(6).times.get(i)});
                }
                // Resizing column widths
                for (int i = 0; i < 7; i++){
                    tblCalendar.getColumnModel().getColumn(i).setPreferredWidth(200);
                    tblCalendar.getColumnModel().getColumn(i).setMinWidth(200);
                    tblCalendar.getColumnModel().getColumn(i).setMaxWidth(400);
                }
                // Adding components to pane
                JScrollPane pane = new JScrollPane(tblCalendar);
                int result = JOptionPane.showConfirmDialog(this, pane, "Calendar", JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {

                }
            }
            catch (Exception ex) {
                // Display pop-up message for any errors that arise
                ex.printStackTrace();
                Notification.display(ex.getMessage());
            }
        }
        // Check if refresh button is pressed
        if (e.getSource() == refreshButton) {
            tableModel.setObjectRows(ScheduleService.getInstance().refresh());
            tableModel.fireTableDataChanged();
        }
    }

    /**
     * A helper function for the getting the enum names.
     *
     * @param enumType The enum based on a class.
     * @return Returns a string list of all the enum names from the enum.
     */
    public static String[] getNames(Class<? extends Enum<?>> enumType) {
        return Arrays.stream(enumType.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
}
