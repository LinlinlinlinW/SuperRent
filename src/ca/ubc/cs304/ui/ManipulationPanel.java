package ca.ubc.cs304.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

import ca.ubc.cs304.delegates.Delegate;
import ca.ubc.cs304.model.ModelForManipulation.ManipulateCustomersModel;
import jdk.nashorn.internal.scripts.JO;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
/**steps:*/
/**paint: initialize all panels*/
/**handle: get inputs, request for query(pass the param and call function), set the result panel(setText)*/
/**show: add actionlistener to the buttons(setVisible(true))*/

public class ManipulationPanel {
    private ImageIcon bkgImg= new ImageIcon("src\\bkgImg.jpg");
    private GridLayout btnLayout = new GridLayout(3,1);

    private Delegate delegate;
    private JPanel manipulationPanel;


    private JButton insertBtn;
    private JButton deleteBtn;
    private JButton updateBtn;
    private JButton viewSomeBtn;
    private JButton viewAllBtn ;
    private JButton backBtn;


    private JPanel leftPanel;
    private JPanel rightPanel;

    // insert btn
    private String dlicenseInsert;
    private String nameInsert;
    private String cellphoneInsert;
    private String addressInsert;

    // delete btn
    private String dlicenseDelete;

    // update btn
    private String addressUpdate;
    private String nameUpdate;


    /**logistics functions*/
    public JPanel getManipulationPanel() {
        return this.manipulationPanel;
    }
    public JButton getBackBtn() {return this.backBtn;}
    public ManipulationPanel(Delegate delegate) {

        // general setting
        this.delegate = delegate;
        manipulationPanel = new JPanel(new GridLayout(1,2));
        manipulationPanel.setOpaque(false);
        manipulationPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        btnLayout.setVgap(30);

        // initialize left and right part
        initializeLiftBtnPanel();
        setUpLeftPanelBtn();
        initializeRightBtnPanel();
        setUpRightPanelBtn();

        // add up left and right panels
        manipulationPanel.add(leftPanel);
        manipulationPanel.add(rightPanel);
    }

    /**left btn panel*/
    // EFFECTS: do insert
    private void doInsert(){
        String[] questions = new String[4];
        questions[0] = "input driver license";
        questions[1] = "input your name";
        questions[2] = "input your cellphone";
        questions[3] = "input your address";

        int i = 0;
        while(i<4) {
            switch (i) {
                case 0: {
                    String ddlicense = JOptionPane.showInputDialog(new JFrame("new Customer: driver license?"), questions[i]);
                    if (ddlicense.equals("")) {
                        JOptionPane.showMessageDialog(null, "please input the driver license", "Error",0);
                        break;
                    }
                    dlicenseInsert = ddlicense; i++; break;
                }
                case 1: {
                    String nname = JOptionPane.showInputDialog(new JFrame("new Customer: name?"), questions[i]);
                    if (nname.equals("")) {
                        JOptionPane.showMessageDialog(null, "please input the name", "Error",0);
                        break;
                    }
                    nameInsert = nname; i++; break;
                }
                case 2: {
                    String ccellphone = JOptionPane.showInputDialog(new JFrame("new Customer: cellphone?"),questions[i]);
                    if (ccellphone.equals("")) {
                        JOptionPane.showMessageDialog(null, "you don't have cellphone number");
                    }
                    cellphoneInsert = ccellphone; i++; break;
                }
                case 3: {
                    String aaddress = JOptionPane.showInputDialog(new JFrame("new Customer: address?"),questions[i]);
                    if (aaddress.equals("")) {
                        JOptionPane.showMessageDialog(null, "you don't have address");
                    }
                    addressInsert = aaddress; i++; break;
                }
                default :{
                    i = 4;
                    JOptionPane.showMessageDialog(null, "error in insert", "Error",0);
                    break;
                }
            }
        }
    }
    private void doDelete() {
        String question = "input the driver license";
        int i = 0;
        while (i == 0){
            String ddlicense = JOptionPane.showInputDialog(new Frame("delete: driver license"), question);
            if(ddlicense.equals("")){
                JOptionPane.showMessageDialog(null, "please input the driver license number!", "error",0);
            } else {
                i = 1;
                dlicenseDelete = ddlicense;
            }
        }
    }
    private void doUpdate() {
        String[] questions = new String[2];
        questions[0] = "input updated address";
        questions[1] = "input your name";

        int i = 0;
        while (i<2){
            switch (i) {
                case 0: {
                    String aaddress = JOptionPane.showInputDialog(new Frame("update: updated address?"), questions[i]);
                    if(aaddress.equals("")) {
                        JOptionPane.showMessageDialog(null,"please input updated address","error",0);
                        break;
                    }
                    addressUpdate = aaddress;
                    i++; break;
                }

                case 1: {
                    String nname = JOptionPane.showInputDialog(new Frame("update: updated name?"), questions[i]);
                    System.out.println("updated name: "+nname);
                    if(nname.equals("")) {
                        JOptionPane.showMessageDialog(null,"please input updated name","error",0);
                        break;
                    }
                    nameUpdate = nname;
                    i++; break;
                }

                default: {
                    i = 2;
                    JOptionPane.showMessageDialog(null,"error in update!","error",0);
                    break;
                }
            }
        }


    }
    private void paintCustomerTable() {
        ManipulateCustomersModel[] ManipulateCustomersModels = delegate.viewCustomer();

        // upper layer
        GridLayout gLayout = new GridLayout(ManipulateCustomersModels.length + 2,2);
        gLayout.setVgap(10);
        JPanel tablePane = new JPanel(gLayout);
        tablePane.setBorder(BorderFactory.createEmptyBorder(10,20,10,10));
        tablePane.setOpaque(false);

        // bottom layer
        JFrame tempFrame = new JFrame("VIEW CUSTOMER");
        tempFrame.setBounds(380,0,bkgImg.getIconWidth()/2,bkgImg.getIconHeight());
        tempFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tempFrame.setVisible(true);
        tempFrame.setResizable(true);

        // middle layer
        JPanel contentPane = (JPanel) tempFrame.getContentPane();
        contentPane.add(tablePane);
        contentPane.setOpaque(false);

        // second paint the result to the panel
        JLabel hint = new JLabel("TABLE CUSTOMER");
        hint.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        hint.setFont(new Font("SansSerif", Font.BOLD, 40));
        tablePane.add(hint);

        JLabel attr = new JLabel("dlicense      name        cellphone        address");
        attr.setFont(new Font("SansSerif",Font.BOLD,30));
        tablePane.add(attr);

        for (int i = 0; i< ManipulateCustomersModels.length; i++) {
            JLabel aftCustomer = new JLabel();
            aftCustomer.setFont(new Font("SansSerif",Font.PLAIN,20));
            aftCustomer.setText(
                    ManipulateCustomersModels[i].getDlicense()+"        "+ ManipulateCustomersModels[i].getName()+"        "+
                            ManipulateCustomersModels[i].getCellphone()+ "        "+ ManipulateCustomersModels[i].getAddress());
            tablePane.add(aftCustomer);
        }
    }


    private void paintAllTables() {
        ArrayList<ArrayList> results = delegate.viewAllTables();
        System.out.println("GOT ALL RESULTS!!!!");
        // bottom layer
        viewAllTable view = new viewAllTable();
        view.addColumnsToTables();


            for (int i = 0; i < results.size(); i++) {
                ArrayList<ArrayList> rs = results.get(i);
                System.out.println("GOT " + i + " result set");

                switch (i) {
                    case 0: {
                        for (int j = 0; j < rs.size(); j++) {
                            ArrayList<String> temp = rs.get(j);
                            view.addRowVehicleType(temp);
                        }
                    }
                    break;
                    case 1: {
                        for (int j = 0; j < rs.size(); j++) {
                            ArrayList<String> temp = rs.get(j);
                            view.addRowCustomers(temp);
                        }
                    }
                    break;
                    case 2: {
                        for (int j = 0; j < rs.size(); j++) {
                            ArrayList<String> temp = rs.get(j);
                            view.addRowReservations(temp);
                        }
                    }
                    break;
                    case 3: {
                        for (int j = 0; j < rs.size(); j++) {
                            ArrayList<String> temp = rs.get(j);
                            view.addRowVehicles(temp);
                        }
                    }
                    break;
                    case 4: {
                        for (int j = 0; j < rs.size(); j++) {
                            ArrayList<String> temp = rs.get(j);
                            view.addRowRentals(temp);
                        }
                    }
                    break;
                    case 5: {
                        for (int j = 0; j < rs.size(); j++) {
                            ArrayList<String> temp = rs.get(j);
                            view.addRowReturns(temp);
                        }
                    }
                    break;

                }
            }



        //view.addColumnsToTables();

        /*
        tempFrame.setBounds(380,0,bkgImg.getIconWidth()/2,bkgImg.getIconHeight());
        tempFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tempFrame.setVisible(true);
        tempFrame.setResizable(true);

        // middle layer
        JPanel contentPane = (JPanel) tempFrame.getContentPane();
        contentPane.setOpaque(false);

        GridLayout gLayout = new GridLayout(3,2);
        gLayout.setVgap(10);
        JPanel protectPane = new JPanel(gLayout);
        protectPane.setOpaque(false);
        contentPane.add(protectPane);


        // TODO columns panels haven't been implemented (need to separate result set)
        // upper layer
        try {
            for (int i = 0; i < results.size(); i++) {
                JPanel tablePane = new JPanel(new GridLayout(results.get(i).getInt(1), 1));
                tablePane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
                tablePane.setOpaque(false);

                JLabel hint = new JLabel();
                hint.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                hint.setFont(new Font("SansSerif", Font.BOLD, 40));

                JLabel attr = new JLabel();
                attr.setFont(new Font("SansSerif", Font.BOLD, 30));

                JLabel columns = new JLabel();
                columns.setFont(new Font("SansSerif",Font.PLAIN,20));
                GridLayout gd = new GridLayout();
                gd.setVgap(8);
                gd.setHgap(8);
                gd.setColumns(1);

                switch(i) {
                    case 0: {
                        gd.setRows(4);
                        hint.setText("TABLE CUSTOMER");
                        attr.setText("dlicense      name        cellphone        address");
                        // TODO
                        //columns.setText(results.get(i));
                    } break;

                    case 1: {
                        gd.setRows(5);
                        hint.setText("TABLE RETURNS");
                        attr.setText("dlicense      returndate        odometer        fullTank      value");
                        // TODO
                        //columns.setText();
                    } break;

                    case 2: {
                        gd.setRows(12);
                        hint.setText("TABLE RENTALS");
                        attr.setText("rid     vlicense     dlicense    fromdate     todate      fromquerydate     " +
                                "toquerydate     odometer     cardname    cardno     expdate     confNo");
                        // TODO
                        //columns.setText();
                    } break;

                    case 3: {
                        gd.setRows(10);
                        hint.setText("TABLE VEHICLES");
                        attr.setText("vlicense    make    model    year    color    odometer    status    vtname    " +
                                "location    city");
                        // TODO
                        //columns.setText();
                    } break;

                    case 4: {
                        gd.setRows(9);
                        hint.setText("TABLE VEHICLESTYPE");
                        attr.setText("vtname   features   wrate   drate   hrate   wirate   dirate    hirate   krate");
                        // TODO
                        // columns.setText();
                    } break;

                    case 5: {
                        gd.setRows(8);
                        hint.setText("TABLE RESERVATIONS");
                        attr.setText("confNo    vtname    vlicense   dlicense   fromdate    todate    fromquerydate    toquerydate");
                        // TODO
                        // columns.setText();
                    } break;

                    default: {
                       throw new Exception();
                    }
                }

                columns.setLayout(gd);
                tablePane.add(hint);
                tablePane.add(attr);
                tablePane.add(columns);

                protectPane.add(tablePane);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"error when reading all tables","error",0);
            System.exit(0);
        }
        */

    }

    // EFFECTS: initialize left panel
    private void initializeLiftBtnPanel() {
        leftPanel = new JPanel(btnLayout);
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(200,150,200,150));

        insertBtn = new JButton("INSERT CUSTOMER");
        insertBtn.setFont(new Font("SansSerif", Font.BOLD, 30));
        leftPanel.add(insertBtn);

        deleteBtn = new JButton("DELETE CUSTOMER");
        deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 30));;
        leftPanel.add(deleteBtn);

        updateBtn = new JButton("UPDATE CUSTOMER");
        updateBtn.setFont(new Font("SansSerif", Font.BOLD, 30));
        leftPanel.add(updateBtn);
    }
    private void setUpLeftPanelBtn(){
        insertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doInsert();
                delegate.insertCustomer(Integer.parseInt(dlicenseInsert), nameInsert, cellphoneInsert, addressInsert);
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doDelete();
                delegate.deleteCustomer(Integer.parseInt(dlicenseDelete));
            }
        });

        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doUpdate();
                delegate.updateCustomer(addressUpdate, nameUpdate);
            }
        });
    }

    // EFFECTS: initialize right panel
    private void initializeRightBtnPanel() {
        btnLayout.setVgap(30);

        rightPanel = new JPanel(btnLayout);

        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(200,150,200,150));

        viewSomeBtn = new JButton("VIEW CUSTOMER");
        viewSomeBtn.setFont(new Font("SansSerif", Font.BOLD, 30));
        rightPanel.add(viewSomeBtn);

        viewAllBtn = new JButton("VIEW ALL TABLES");
        viewAllBtn.setFont(new Font("SansSerif", Font.BOLD, 30));
        rightPanel.add(viewAllBtn);

        backBtn = new JButton("BACK");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 30));
        rightPanel.add(backBtn);
    }
    private void setUpRightPanelBtn() {
        viewSomeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintCustomerTable();
            }
        });

        viewAllBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintAllTables();

            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manipulationPanel.setVisible(false);
            }
        });
    }
}
