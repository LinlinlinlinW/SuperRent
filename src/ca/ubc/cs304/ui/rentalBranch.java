package ca.ubc.cs304.ui;

import ca.ubc.cs304.model.ModelForService.ClerkGenerateReportForBranchRentalModel;
import ca.ubc.cs304.model.ModelForService.ClerkGenerateReportForRentalModel;
import ca.ubc.cs304.model.ModelForService.CustomerGetAvailableVehicleModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class rentalBranch {
    private JTable table1;
    private JPanel main;
    private JTable table2;
    private JTextField textField1;

    private DefaultTableModel tblmodel;
    private DefaultTableModel othermodel;


    public rentalBranch() {
        JFrame tempFrame = new JFrame("VIEW ALL TABLES");
        tempFrame.setContentPane(main);
        tempFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tempFrame.pack();
        tempFrame.setVisible(true);
        tempFrame.setSize(2200, 1100);

        this.tblmodel = new DefaultTableModel(new String[]{"vtname", "location", "vlicense", "make", "model", "year", "color"}, 0);
        this.othermodel = new DefaultTableModel(new String[]{"Car Type", "Number rented"}, 0);

    }

    public void populateTable(ClerkGenerateReportForBranchRentalModel model) {
        table1.setModel(tblmodel);
        table2.setModel(othermodel);

        tblmodel.addRow(new String[]{"vtname", "location", "vlicense", "make", "model", "year", "color"});
        othermodel.addRow(new String[]{"Car Type", "Number rented"});

        CustomerGetAvailableVehicleModel[] vehicles = model.getRentedVehicles();

        String[] vehicletypes = model.getVehicleTypesRented();
        int[] numbasvehicles = model.getRentedPerVehicleType();

        for (int i = 0; i < vehicles.length; i++) {
            CustomerGetAvailableVehicleModel vehicle = vehicles[i];
            tblmodel.addRow(new String[]{vehicle.getVtname(), vehicle.getLocation(), vehicle.getVlicense(), vehicle.getMake(), vehicle.getModel(), Integer.toString(vehicle.getYear()), vehicle.getColor()});
        }

        for (int i = 0; i < vehicletypes.length; i++) {
            othermodel.addRow(new String[]{vehicletypes[i], Integer.toString(numbasvehicles[i])});
        }

        textField1.setText("Total Cars Rented" + Integer.toString(model.getTotalRentals()));

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
        main = new JPanel();
        main.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Daily Report for Rentals at this Branch");
        main.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        table1 = new JTable();
        main.add(table1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        table2 = new JTable();
        main.add(table2, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        textField1 = new JTextField();
        textField1.setEditable(false);
        main.add(textField1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return main;
    }

}


