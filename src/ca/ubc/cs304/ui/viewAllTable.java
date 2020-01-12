package ca.ubc.cs304.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class viewAllTable {
    private JTable vehicleTypeTable;
    public JPanel mainView;
    private JTable customersTable;
    private JTable reservationsTables;
    private JTable rentalsTable;
    private JTable vehiclesTable;
    private JTable returnsTable;

    private DefaultTableModel vehicleTypeModel;
    private DefaultTableModel customersModel;
    private DefaultTableModel reservationsModel;
    private DefaultTableModel vehiclesModel;
    private DefaultTableModel rentalsModel;
    private DefaultTableModel returnsModel;


    public viewAllTable() {

        JFrame tempFrame = new JFrame("VIEW ALL TABLES");
        tempFrame.setContentPane(mainView);
        tempFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tempFrame.pack();
        tempFrame.setVisible(true);
        tempFrame.setSize(2200, 1100);

        this.vehicleTypeModel = new DefaultTableModel(new String[]{"vtname", "features", "wrate", "drate", "hrate", "wirate", "dirate", "hirate", "krate"}, 0);
        this.customersModel = new DefaultTableModel(new String[]{"dlicense", "name", "cellphone", "address"}, 0);
        this.reservationsModel = new DefaultTableModel(new String[]{"confNo", "vtname", "vlicense", "dlicense", "fromDate", "toDate", "fromQueryDate", "toQueryDate"}, 0);
        this.vehiclesModel = new DefaultTableModel(new String[]{"vlicense", "make", "model", "year", "color", "odometer", "status", "vtname", "location", "city"}, 0);
        this.rentalsModel = new DefaultTableModel(new String[]{"rid", "vlicense", "dlicense", "fromDate", "toDate", "fromQueryDate", "toQueryDate", "odometer", "cardName", "cardNo", "expDate", "confNo"}, 0);
        this.returnsModel = new DefaultTableModel(new String[]{"rid", "return_date", "odometer", "fulltank"}, 0);
    }

    public void addColumnsToTables() {

        vehicleTypeTable.setModel(vehicleTypeModel);
        vehicleTypeModel.addRow(new String[]{"vtname", "features", "wrate", "drate", "hrate", "wirate", "dirate", "hirate", "krate"});


        customersTable.setModel(customersModel);
        customersModel.addRow(new String[]{"dlicense", "name", "cellphone", "address"});


        reservationsTables.setModel(reservationsModel);
        reservationsModel.addRow(new String[]{"confNo", "vtname", "vlicense", "dlicense", "fromDate", "toDate", "fromQueryDate", "toQueryDate"});


        vehiclesTable.setModel(vehiclesModel);
        vehiclesModel.addRow(new String[]{"vlicense", "make", "model", "year", "color", "odometer", "status", "vtname", "location", "city"});


        rentalsTable.setModel(rentalsModel);
        rentalsModel.addRow(new String[]{"rid", "vlicense", "dlicense", "fromDate", "toDate", "fromQueryDate", "toQueryDate", "odometer", "cardName", "cardNo", "expDate", "confNo"});


        returnsTable.setModel(returnsModel);
        returnsModel.addRow(new String[]{"rid", "return_date", "odometer", "fulltank"});

    }

    public void addRowVehicleType(ArrayList<String> tuple) {
        String vtname = tuple.get(0);
        String features = tuple.get(1);
        String wrate = tuple.get(2);
        String drate = tuple.get(3);
        String hrate = tuple.get(4);
        String wirate = tuple.get(5);
        String dirate = tuple.get(6);
        String hirate = tuple.get(7);
        String krate = tuple.get(8);
        vehicleTypeModel.addRow(new String[]{vtname, features, wrate, drate, hrate, wirate, dirate, hirate,  krate});
    }

    public void addRowCustomers(ArrayList<String> tuple) {
        String dlicense = tuple.get(0);
        String name = tuple.get(1);
        String cellphone = tuple.get(2);
        String address = tuple.get(3);
        customersModel.addRow(new String[]{dlicense, name, cellphone, address});
    }

    public void addRowReservations(ArrayList<String> tuple) {
        String confNo = tuple.get(0);
        String vtname = tuple.get(1);
        String vlicense = tuple.get(2);
        String dlicense = tuple.get(3);
        String fromDate = tuple.get(4);
        String toDate = tuple.get(5);
        String fromQueryDate = tuple.get(6);
        String toQueryDate = tuple.get(7);
        reservationsModel.addRow(new String[]{confNo, vtname, vlicense, dlicense, fromDate, toDate, fromQueryDate, toQueryDate});

    }

    public void addRowVehicles(ArrayList<String> tuple) {
        String vlicense = tuple.get(0);
        String make = tuple.get(1);
        String model = tuple.get(2);
        String year = tuple.get(3);
        String color = tuple.get(4);
        String odometer = tuple.get(5);
        String status = tuple.get(6);
        String vtname = tuple.get(7);
        String location = tuple.get(8);
        String city = tuple.get(9);
        vehiclesModel.addRow(new String[]{vlicense, make, model, year, color, odometer, status, vtname, location, city});
    }

    public void addRowRentals(ArrayList<String> tuple) {
        String rid = tuple.get(0);
        String vlicense = tuple.get(1);
        String dlicense = tuple.get(2);
        String fromDate = tuple.get(3);
        String toDate = tuple.get(4);
        String fromQueryDate = tuple.get(5);
        String toQueryDate = tuple.get(6);
        String odometer = tuple.get(7);
        String cardName = tuple.get(8);
        String cardNo = tuple.get(9);
        String expDate = tuple.get(10);
        String confNo = tuple.get(11);
        rentalsModel.addRow(new String[]{rid, vlicense, dlicense, fromDate, toDate, fromQueryDate, toQueryDate, odometer, cardName, cardNo, expDate, confNo});
    }

    public void addRowReturns(ArrayList<String> tuple) {
        String rid = tuple.get(0);
        String return_date = tuple.get(1);
        String odometer = tuple.get(2);
        String fulltank = tuple.get(3);
        String value = tuple.get(4);
        returnsModel.addRow(new String[]{rid, return_date, odometer, fulltank, value});
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainView = new JPanel();
        mainView.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("VehicleTypes");
        mainView.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        vehicleTypeTable = new JTable();
        mainView.add(vehicleTypeTable, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        customersTable = new JTable();
        mainView.add(customersTable, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Customers");
        mainView.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Reservations");
        mainView.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reservationsTables = new JTable();
        mainView.add(reservationsTables, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Vehicles");
        mainView.add(label4, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rentalsTable = new JTable();
        mainView.add(rentalsTable, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Rentals");
        mainView.add(label5, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Returns");
        mainView.add(label6, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        returnsTable = new JTable();
        mainView.add(returnsTable, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainView.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        vehiclesTable = new JTable();
        scrollPane1.setViewportView(vehiclesTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainView;
    }


}
