package ca.ubc.cs304.ui;

import ca.ubc.cs304.delegates.Delegate;
import ca.ubc.cs304.model.ModelForService.CustomerGetAvailableVehicleModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ServicePanel {
    private JPanel servicePanel = new JPanel(new GridLayout(1,2));
    private ImageIcon bkgImg= new ImageIcon("src\\bkgImg.jpg");
    public Delegate delegate;


    // customer button panel
    private JPanel customerBtnPanel;
    private JButton previewBtn;
    private JButton reserveBtn;
    private JButton backBtn;

    // preview service
    private String previewVtName;
    private String previewLocation;
    private String previewFromDate;
    private String previewToDate;

    // reserve service
    private String reserveVtName;
    private String reserveDlicense;
    private String reserveFromDate;
    private String reserveToDate;

    // clerk button panel
    private JPanel clerkBtnPanel;
    private JButton rentBtn;
    private JButton returnBtn;
    private JButton reportBtn1,reportBtn2,reportBtn3,reportBtn4;

    /**clerk panel*/
    // EFFECTS: helper functions of RENT
    private int confNo;
    private String cardName;
    private String cardNo;
    private String expDateYear;
    private String expDateMon;

    // EFFECTS: helper functions of RETURN
    private int returnRid;
    private int odometer;
    private int fullTank;

    // EFFECTS: helper functions of generate reports
    private String locationBranchReturn;
    private String cityBranchReturn;

    private String locationBranchRental;
    private String cityBranchRental;

    // EFFECTS: helper functions of generate reports

    private void paintReceipt(String[] receipt, String action) {
        // bottom layer
        JFrame tempFrame = new JFrame(action+" RECEIPT");
        tempFrame.setBounds(380,0,bkgImg.getIconWidth()/2,bkgImg.getIconHeight());
        tempFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tempFrame.setVisible(true);
        tempFrame.setResizable(true);

        // middle layer
        JPanel contentPane = (JPanel) tempFrame.getContentPane();
        contentPane.setOpaque(false);

        // upper layer
        GridLayout gLayout = new GridLayout(receipt.length + 2,2);
        gLayout.setVgap(10);
        JPanel tablePane = new JPanel(gLayout);
        tablePane.setBorder(BorderFactory.createEmptyBorder(10,20,10,10));
        tablePane.setOpaque(false);
        contentPane.add(tablePane);

        JLabel hint = new JLabel("RENTAL RECEIPT:");
        hint.setBorder(BorderFactory.createEmptyBorder(50,10,10,10));
        tablePane.add(hint);

        for (int i = 0; i < receipt.length; i++) {
            JLabel detail = new JLabel();
            detail.setText(receipt[i]);
            tablePane.add(detail);
        }
    }

    private void doRent() {
        int i=0;
        while (i<5) {
            switch(i) {
                case 0 : {
                    Object[] options = {"yes", "no" };
                    Object selectedValue = JOptionPane.showInputDialog(null,
                            "do you have confirmation number", "rent: confNo?",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            options, options[0]);

                    if (selectedValue.toString().equals("no")) {
                        // if the clerk doesn't have a confirmation number, just set confNo to 0
                        doReservation();
                        confNo = getReserveOutput();
                        i++; break;
                    } else if (selectedValue.toString().equals("yes")){
                        confNo = Integer.parseInt(JOptionPane.showInputDialog(new JFrame("rent"), "please input your reservation number"));
                        i++; break;
                    } else {
                        JOptionPane.showMessageDialog(null, "error in rental","error", 0);
                        break;
                    }
                }
                case 1: {
                    String ccardName = JOptionPane.showInputDialog(new JFrame("rent: cardName?"), "please input yoru card Name");
                    if (ccardName.equals("")){
                        JOptionPane.showMessageDialog(null, "please input the card name!","error", 0);
                        break;
                    }
                    cardName = ccardName;
                    i++;
                    break;
                }
                case 2:{
                    String ccardNo = JOptionPane.showInputDialog(new JFrame("rent: cardNo?"), "please input yoru card number");
                    if (ccardNo.equals("")){
                        JOptionPane.showMessageDialog(null, "please input the card number!","error", 0);
                        break;
                    }
                    cardNo = ccardNo;
                    i++;
                    break;
                }
                case 3 : {
                    String eexpDateYear = JOptionPane.showInputDialog(new JFrame("rent: expDateYear?"), "please input your card expDate Year");
                    if (eexpDateYear.equals("") || Integer.parseInt(eexpDateYear) <= 2019) {
                        JOptionPane.showMessageDialog(null, "please input the valid card year!","error", 0);
                        break;
                    }
                    expDateYear = eexpDateYear;
                    i++; break;
                }
                case 4 : {
                    String eexpDateMon = JOptionPane.showInputDialog(new JFrame("rent: expDateMon?"), "please input your card expDate Month");
                    if (eexpDateMon.equals("") ||( Integer.parseInt(eexpDateMon) > 12) || (Integer.parseInt(eexpDateMon)<12 && Integer.parseInt(expDateYear) == 2019) || Integer.parseInt(eexpDateMon) <= 0 ) {
                        JOptionPane.showMessageDialog(null, "please input the valid card month!","error", 0);
                        break;
                    }
                    expDateMon = eexpDateMon;
                    i++; break;
                }
                default: {
                    i = 5;
                    JOptionPane.showMessageDialog(null, "error in rental","error", 0);
                    break;
                }
            }
        }

    }

    private void setUpClerkBtnPanel() {
        rentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doRent();
                String[] receipt = delegate.clerkRentVehicle(confNo, null,-1,null,null,cardName,cardNo,expDateYear+ "/"+expDateMon);
                paintReceipt(receipt, "RENT");
            }
        });

        returnBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] ques = new String[3];
                ques[0] = "please input your rid";
                ques[1] = "please input odometer reading";
                ques[2] = "is your tank full?";
                int i = 0;
                while (i<3) {
                    switch(i) {
                        case 0 : {
                            String rrid = JOptionPane.showInputDialog(new JFrame("return: rid?"), ques[i]);
                            if (rrid.equals("")){
                                JOptionPane.showMessageDialog(null,"please input your rental id!","error",0);
                                break;
                            }
                            returnRid = Integer.parseInt(rrid);
                            i++; break;
                        }

                        case 1 : {
                            String oodometer = JOptionPane.showInputDialog(new JFrame("return: odometer?"), ques[i]);
                            if(oodometer.equals("")){
                                JOptionPane.showMessageDialog(null,"please input your odometer reading!","error",0);
                                break;
                            }
                            odometer = Integer.parseInt(oodometer);
                            i++; break;
                        }

                        case 2 : {
                            Object[] options = {"full", "not full" };
                            Object selectedValue = JOptionPane.showInputDialog(null,
                                    "is the tank full?", "rent: tank full?",
                                    JOptionPane.INFORMATION_MESSAGE, null,
                                    options, options[0]);
                            if (selectedValue.toString().equals("full")) {
                                fullTank = 1;
                                i++; break;
                            } else if (selectedValue.toString().equals("not full")) {
                                fullTank = 0;
                                i++; break;
                            } else {
                                JOptionPane.showMessageDialog(null,"please check your gas residual!","error",0);
                                break;
                            }
                        }

                        default:{
                            JOptionPane.showMessageDialog(null,"error in return!","error",0);
                            break;
                        }
                    }
                }


                String[] receipt = delegate.clerkReturnVehicle(returnRid,odometer,fullTank);
                paintReceipt(receipt, "RETURN");
            }
        });

        // report for branch return
        reportBtn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            doBranchReturn();
            }
        });

        // report for branch rental
        reportBtn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            doBranchRental();
            }
        });

        // report for general return
        reportBtn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            doGeneralReturn();
            }
        });

        // report for general rental
        reportBtn4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            doGeneralRental();
            }
        });
    }

    private void doGeneralRental() {
        rental view = new rental();
        view.populateTable(delegate.clerkGenerateReportForRental());
    }

    private void doGeneralReturn() {
        returns view = new returns();
        view.populateTable(delegate.clerkGenerateReportForReturn());
    }

    private void doBranchRental() {
        rentalBranch view = new rentalBranch();int i = 0;
        while (i<2) {
            switch (i) {
                case 0 : {
                    String loc = JOptionPane.showInputDialog(new JFrame("report for branch rental"),"input the location for report");
                    if (loc.equals("")) {
                        JOptionPane.showMessageDialog(null,"please input valid location","error",0);
                        break;
                    }
                    locationBranchRental = loc;
                    i++; break;
                }

                case 1: {
                    String place = JOptionPane.showInputDialog(new JFrame("report for branch rental"),"input the city for report");
                    if (place.equals("")) {
                        JOptionPane.showMessageDialog(null,"please input valid city","error",0);
                        break;
                    }
                    cityBranchRental = place;
                    i++;break;
                }

                default:{
                    i=2;
                    JOptionPane.showMessageDialog(null,"error in generating branch rental report");
                    break;
                }
            }
        }
        view.populateTable(delegate.clerkGenerateReportForBranchRental(locationBranchRental, cityBranchRental));
    }

    private void doBranchReturn() {

        returnsBranch view = new returnsBranch();

        int i = 0;
        while (i<2) {
            switch (i) {
                case 0 : {
                    String location = JOptionPane.showInputDialog(new JFrame("report for branch return"),"input the location for report");
                    if (location.equals("")) {
                        JOptionPane.showMessageDialog(null,"please input valid location","error",0);
                        break;
                    }
                    locationBranchReturn = location;
                    i++; break;
                }

                case 1: {
                    String city = JOptionPane.showInputDialog(new JFrame("report for branch return"),"input the location for report");
                    if (city.equals("")) {
                        JOptionPane.showMessageDialog(null,"please input valid city","error",0);
                        break;
                    }
                    cityBranchReturn = city;
                    i++;break;
                }

                default:{
                    i=2;
                    JOptionPane.showMessageDialog(null,"error in generating branch return report");
                    break;
                }
            }
        }

        view.populateTable(delegate.clerkGenerateReportForBranchReturn(locationBranchReturn,cityBranchReturn));
    }

    private void initializeClerkBtnPanel() {
        GridLayout gd = new GridLayout(7,1);
        gd.setVgap(30);
        clerkBtnPanel = new JPanel(gd);
        clerkBtnPanel.setBorder(BorderFactory.createEmptyBorder(100,150,50,150));
        clerkBtnPanel.setOpaque(false);
        clerkBtnPanel.setVisible(true);

        JLabel clerk = new JLabel("CLERK", SwingConstants.CENTER);
        clerk.setFont(new Font("SansSerif", Font.BOLD, 40));
        rentBtn = new JButton("RENT");
        rentBtn.setFont(new Font("SansSerif",Font.BOLD,30));
        returnBtn = new JButton("RETURN");
        returnBtn.setFont(new Font("SansSerif",Font.BOLD,30));
        reportBtn1 = new JButton("REPORT FOR BRANCH RETURN");
        reportBtn1.setFont(new Font("SansSerif",Font.BOLD,30));
        reportBtn2 = new JButton("REPORT FOR BRANCH RENTAL");
        reportBtn2.setFont(new Font("SansSerif",Font.BOLD,30));
        reportBtn3 = new JButton("REPORT FOR GENERAL RETURN");
        reportBtn3.setFont(new Font("SansSerif",Font.BOLD,30));
        reportBtn4 = new JButton("REPORT FOR GENERAL RENTAL");
        reportBtn4.setFont(new Font("SansSerif",Font.BOLD,30));

        clerkBtnPanel.add(clerk);
        clerkBtnPanel.add(rentBtn);
        clerkBtnPanel.add(returnBtn);
        clerkBtnPanel.add(reportBtn1);
        clerkBtnPanel.add(reportBtn2);
        clerkBtnPanel.add(reportBtn3);
        clerkBtnPanel.add(reportBtn4);
    }


    /**customer panel*/
    // EFFECTS: helper functions for both PREVIEW and RESERVE
    private boolean checkValidityOfYear (String year) {
        if (year.equals("") ||
                Integer.parseInt(year) > 2022 ||
                Integer.parseInt(year) <1990) {
            JOptionPane.showMessageDialog(null, "please input a valid year", "Error",0);
            return false;
        }
        return true;
    }
    private boolean checkValidityOfMon (String month) {
        if (month.equals("") || Integer.parseInt(month) > 12 || Integer.parseInt(month) < 1){
            JOptionPane.showMessageDialog(null, "please input a valid month", "Error",0);
            return false;
        }

        return true;
    }
    private boolean checkValidityOfDay (String year, String month, String day) {
        // if the day < 1, false
        if (Integer.parseInt(day)<1 || day.equals("")) {
            JOptionPane.showMessageDialog(null, "please input a valid Day > 1", "Error",0);
            return false;
        }

        // if the month = 1,3,5,7,8,10,12,31, and the day > 31, false
        if (Integer.parseInt(month)==1 ||
                Integer.parseInt(month)==3 ||
                Integer.parseInt(month)==5 ||
                Integer.parseInt(month)==7 ||
                Integer.parseInt(month)== 8 ||
                Integer.parseInt(month)== 10 ||
                Integer.parseInt(month)==12) {
            if (Integer.parseInt(day)>31) {
                JOptionPane.showMessageDialog(null, "please input a valid Day <= 31", "Error",0);
                return false;
            }
        }

        // if the month = 4,6,9,11, and the day > 30, false
        if (Integer.parseInt(month) == 4 ||
                Integer.parseInt(month) == 6 ||
                Integer.parseInt(month) == 9 ||
                Integer.parseInt(month) == 11) {
            if (Integer.parseInt(day) > 30) {
                JOptionPane.showMessageDialog(null, "please input a valid Day <= 30", "Error",0);
                return false;
            }
        }

        // if it is 闰年, and the day > 29, false
        if ((Integer.parseInt(year) % 4) == 0 ) {
            if (Integer.parseInt(day) > 29) {
                JOptionPane.showMessageDialog(null, "please input a valid Day <= 29", "Error",0);
                return false;
            }
        }

        // if it is not 闰年, and the day > 28, false
        if ((Integer.parseInt(year) % 4) != 0) {
            if (Integer.parseInt(day) > 28) {
                JOptionPane.showMessageDialog(null, "please input a valid Day <= 28", "Error",0);
                return false;
            }
        }
        return true;
    }
    private boolean checkValidityOfHour (String hour) {
        if (Integer.parseInt(hour) >24 || Integer.parseInt(hour)<0 || hour.equals("")) {
            JOptionPane.showMessageDialog(null, "please input a valid Hour > 0 and <24", "Error",0);
            return false;
        }
        return true;
    }
    private boolean checkValidityOfMinute (String minute) {
        if (Integer.parseInt(minute) > 59 || Integer.parseInt(minute) < 0 || minute.equals("")) {
            JOptionPane.showMessageDialog(null, "please input a valid minute > 0 and < 60", "Error",0);
            return false;
        }
        return true;
    }
    private boolean checkValidityOfSec (String sec) {
        if(Integer.parseInt(sec) > 59 || Integer.parseInt(sec) < 0 || sec.equals("")) {
            JOptionPane.showMessageDialog(null, "please input a valid second > 0 and < 60", "Error",0);
            return false;
        }
        return true;
    }
    private String makeUpZero(String number) {
        if (Integer.parseInt(number) < 10 && Integer.parseInt(number) > 0){
            number = "0"+number;
        }
        return number;
    }

    // EFFECTS: helper functions for PREVIEW, output: vlicense, make, model, year, color, vtname, location
    private String[] getPreviewOutput(){
        System.out.println("customer get available vehicles input: "+previewVtName + "," + previewLocation + "," + previewFromDate + "," + previewToDate);

        String[] output = new String[99];
        CustomerGetAvailableVehicleModel[] cgavh = delegate.customerGetAvailableVehicles(previewVtName,previewLocation,previewFromDate,previewToDate);

        for(int i = 0; i<cgavh.length; i++){
            output[i] = cgavh[i].getVlicense() + "   "
                    + cgavh[i].getMake() + "   "
                    + cgavh[i].getModel() + "   "
                    + cgavh[i].getYear() + "   "
                    + cgavh[i].getColor()  + "   "
                    + cgavh[i].getVtname() +"   "
                    + cgavh[i].getLocation() +"\n";
            System.out.println("get preview output:  "+output[i]);
        }

        return output;
    }
    private void doPreview(){
        String[] questions = new String[14];
        String fromYear = null;
        String fromMon = null;
        String fromDay = null;
        String fromHour = null;
        String fromMin = null;
        String fromSec = null;
        String toYear = null;
        String toMon = null;
        String toDay = null;
        String toHour =  null;
        String toMin = null;
        String toSec = null;

        questions[0] = "what is the vehicle type?";
        questions[1] = "what is the location?";
        questions[2] = "start from which year? format: yyyy";
        questions[3] = "start from which month? format: mm";
        questions[4] = "start from which day? format: dd";
        questions[5] = "start from which hour? format: hh";
        questions[6] = "start from which minute? format: mm";
        questions[7] = "start from which second? format: ss";
        questions[8] = "end at which year? format: yyyy";
        questions[9] = "end at which month? format: mm";
        questions[10] = "end at which day? format: dd";
        questions[11] = "end at which hour? format: hh";
        questions[12] = "end at which minute? formate: mm";
        questions[13] = "end at which second? formate: ss";

        int i = 0;
        while (i<14) {
            switch (i) {
                case 0: {
                    String res = JOptionPane.showInputDialog(new JFrame("name of vehicle type"), questions[i]);
                    if(res.equals("")) {
                        JOptionPane.showMessageDialog(null, "Please input the vehicle type","Error",0);
                        break;
                    } else {
                        previewVtName = res;
                        i++; break;
                    }
                }
                case 1: {
                    String res = JOptionPane.showInputDialog(new JFrame("location"),questions[i]);
                    if(res.equals("")) {
                        JOptionPane.showMessageDialog(null, "please input the location", "Error",0);
                        break;
                    } else {
                        previewLocation = res;
                        i++; break;
                    }
                }
                case 2: {
                    fromYear = JOptionPane.showInputDialog(new JFrame("from Year"), questions[i]);
                    if(!checkValidityOfYear(fromYear)){
                        break;
                    }
                    i++;break;

                }
                case 3: {
                    fromMon = JOptionPane.showInputDialog(new JFrame("from Month"), questions[i]);
                    if(!checkValidityOfMon(fromMon)) {
                        break;
                    } else {
                        i++; break;
                    }
                }
                case 4: {
                    fromDay = JOptionPane.showInputDialog(new JFrame("from Day"), questions[i]);
                    if (!checkValidityOfDay(fromYear, fromMon, fromDay))
                        break;
                    i++; break;
                }
                case 5: {
                    fromHour= JOptionPane.showInputDialog(new JFrame("from Hour"), questions[i]);
                    if(!checkValidityOfHour(fromHour))
                        break;
                    i++; break;
                }
                case 6: {
                    fromMin = JOptionPane.showInputDialog(new JFrame("from Minute"), questions[i]);
                    if(!checkValidityOfMinute(fromMin))
                        break;
                    i++;break;
                }
                case 7: {
                    fromSec = JOptionPane.showInputDialog(new Frame("from Second"), questions[i]);
                    if(!checkValidityOfSec(fromSec))
                        break;
                    previewFromDate = fromYear + "-" + makeUpZero(fromMon) + "-" + makeUpZero(fromDay) + "T"
                            + makeUpZero(fromHour) + ":" + makeUpZero(fromMin) + ":" + makeUpZero(fromSec) + ".00Z";
                    System.out.println("previewFromDate: " + previewFromDate);
                    i++;break;
                }
                case 8 : {
                    toYear = JOptionPane.showInputDialog(new Frame("to Year"), questions[i]);
                    if(!checkValidityOfYear(toYear))
                        break;
                    i++;break;
                }
                case 9: {
                    toMon = JOptionPane.showInputDialog(new Frame("to Month"), questions[i]);
                    if (!checkValidityOfMon(toMon))
                        break;
                    i++;break;
                }
                case 10: {
                    toDay = JOptionPane.showInputDialog(new Frame("to Day"), questions[i]);
                    if(!checkValidityOfDay(toYear,toMon,toDay))
                        break;
                    i++;break;
                }
                case 11: {
                    toHour = JOptionPane.showInputDialog(new Frame("to Hour"), questions[i]);
                    if(!checkValidityOfHour(toHour))
                        break;
                    i++;break;
                }
                case 12: {
                    toMin = JOptionPane.showInputDialog(new Frame("to minute"), questions[i]);
                    if(!checkValidityOfMinute(toMin))
                        break;
                    i++;break;
                }
                case 13: {
                    toSec = JOptionPane.showInputDialog(new Frame("to Second"), questions[i]);
                    if (!checkValidityOfSec(toSec))
                        break;
                    previewToDate = toYear + "-" + makeUpZero(toMon) + "-" + makeUpZero(toDay) + "T" +
                            makeUpZero(toHour) + ":" + makeUpZero(toMin) + ":" + makeUpZero(toSec) + ".00Z";
                    System.out.println("previewFromDate: " + previewToDate);
                    i++;
                    break;
                }
                default: {
                    i = 14;
                    JOptionPane.showMessageDialog(null, "error in preview", "Error",0);
                    break;
                }
            }
        }


    }

    // EFFECTS: helper functions of RESERVE, output: confNo
    private int getReserveOutput() {
        System.out.println("customer reserves vehicles input: " + reserveVtName + "," + reserveDlicense + "," + reserveFromDate + "," + reserveToDate);
        return delegate.customerMakeReservation(reserveVtName,Integer.parseInt(reserveDlicense),reserveFromDate,reserveToDate);

    }
    private void doReservation() {
        String[] questions = new String[14];
        questions[0] = "what is the vehicle type?";
        questions[1] = "what is your driver license?";
        questions[2] = "start from which year? format: yyyy";
        questions[3] = "start from which month? format: mm";
        questions[4] = "start from which day? format: dd";
        questions[5] = "start from which hour? format: hh";
        questions[6] = "start from which minute? format: mm";
        questions[7] = "start from which second? format: ss";
        questions[8] = "end at which year? format: yyyy";
        questions[9] = "end at which month? format: mm";
        questions[10] = "end at which day? format: dd";
        questions[11] = "end at which hour? format: hh";
        questions[12] = "end at which minute? formate: mm";
        questions[13] = "end at which second? formate: ss";

        String fromYear = null;
        String fromMon = null;
        String fromDay = null;
        String fromHour = null;
        String fromMin = null;
        String fromSec = null;
        String toYear = null;
        String toMon = null;
        String toDay = null;
        String toHour =  null;
        String toMin = null;
        String toSec = null;

        int i = 0;
        while (i<14) {
            switch (i) {
                case 0: {
                    String res = JOptionPane.showInputDialog(new JFrame("name of vehicle type"), questions[i]);
                    if(res.equals("")) {
                        JOptionPane.showMessageDialog(null, "Please input the vehicle type","Error",0);
                        break;
                    } else {
                        reserveVtName = res;
                        i++; break;
                    }
                }
                case 1: {
                    String res = JOptionPane.showInputDialog(new JFrame("driver license"),questions[i]);
                    if(res.equals("")) {
                        JOptionPane.showMessageDialog(null, "please input the dlicense", "Error",0);
                        break;
                    } else {
                        reserveDlicense = res;
                        i++; break;
                    }
                }
                case 2: {
                    fromYear = JOptionPane.showInputDialog(new JFrame("from Year"), questions[i]);
                    if(!checkValidityOfYear(fromYear)){
                        break;
                    }
                    i++;break;

                }
                case 3: {
                    fromMon = JOptionPane.showInputDialog(new JFrame("from Month"), questions[i]);
                    if(!checkValidityOfMon(fromMon)) {
                        break;
                    } else {
                        i++; break;
                    }
                }
                case 4: {
                    fromDay = JOptionPane.showInputDialog(new JFrame("from Day"), questions[i]);
                    if (!checkValidityOfDay(fromYear, fromMon, fromDay))
                        break;
                    i++; break;
                }
                case 5: {
                    fromHour= JOptionPane.showInputDialog(new JFrame("from Hour"), questions[i]);
                    if(!checkValidityOfHour(fromHour))
                        break;
                    i++; break;
                }
                case 6: {
                    fromMin = JOptionPane.showInputDialog(new JFrame("from Minute"), questions[i]);
                    if(!checkValidityOfMinute(fromMin))
                        break;
                    i++;break;
                }
                case 7: {
                    fromSec = JOptionPane.showInputDialog(new Frame("from Second"), questions[i]);
                    if(!checkValidityOfSec(fromSec))
                        break;
                    reserveFromDate = fromYear + "-" + makeUpZero(fromMon) + "-" + makeUpZero(fromDay) + "T" + makeUpZero(fromHour) + ":" + makeUpZero(fromMin) + ":" + makeUpZero(fromSec) + ".00Z";
                    System.out.println("reserveFromDate: " + reserveFromDate);
                    i++;break;
                }
                case 8 : {
                    toYear = JOptionPane.showInputDialog(new Frame("to Year"), questions[i]);
                    if(!checkValidityOfYear(toYear))
                        break;
                    i++;break;
                }
                case 9: {
                    toMon = JOptionPane.showInputDialog(new Frame("to Month"), questions[i]);
                    if (!checkValidityOfMon(toMon))
                        break;
                    i++;break;
                }
                case 10: {
                    toDay = JOptionPane.showInputDialog(new Frame("to Day"), questions[i]);
                    if(!checkValidityOfDay(toYear,toMon,toDay))
                        break;
                    i++;break;
                }
                case 11: {
                    toHour = JOptionPane.showInputDialog(new Frame("to Hour"), questions[i]);
                    if(!checkValidityOfHour(toHour))
                        break;
                    i++;break;
                }
                case 12: {
                    toMin = JOptionPane.showInputDialog(new Frame("to minute"), questions[i]);
                    if(!checkValidityOfMinute(toMin))
                        break;
                    i++;break;
                }
                case 13: {
                    toSec = JOptionPane.showInputDialog(new Frame("to Second"), questions[i]);
                    if (!checkValidityOfSec(toSec))
                        break;
                    reserveToDate = toYear + "-" + makeUpZero(toMon) + "-" + makeUpZero(toDay) + "T" + makeUpZero(toHour) + ":" + makeUpZero(toMin) + ":" + makeUpZero(toSec) + ".00Z";
                    System.out.println("reserveToDate: " + reserveToDate);
                    i++;
                    break;
                }
                default: {
                    i = 14;
                    JOptionPane.showMessageDialog(null, "error in reservation", "Error",0);
                    break;
                }
            }
        }
    }

    // EFFECTS: main functions of customer panel
    private void initializeCustomerBtnPanel() {
        GridLayout gd = new GridLayout(4,1);
        gd.setVgap(40);
        customerBtnPanel = new JPanel(gd);
        customerBtnPanel.setBorder(BorderFactory.createEmptyBorder(100,150,100,150));
        customerBtnPanel.setOpaque(false);
        customerBtnPanel.setVisible(true);

        JLabel customer = new JLabel("CUSTOMER", SwingConstants.CENTER);
        customer.setFont(new Font("SansSerif", Font.BOLD, 40));
        previewBtn = new JButton("SHOW AVAILABLE VEHICLES");
        previewBtn.setFont(new Font("SansSerif", Font.BOLD, 30));
        reserveBtn = new JButton("RESERVE");
        reserveBtn.setFont(new Font("SansSerif", Font.BOLD, 30));
        backBtn = new JButton("BACK TO HOME");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 30));
        customerBtnPanel.add(customer);
        customerBtnPanel.add(previewBtn);
        customerBtnPanel.add(reserveBtn);
        customerBtnPanel.add(backBtn);
    }
    private void setUpCustomerBtnPanel() {
        previewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doPreview();
                String[] output = getPreviewOutput();
                JOptionPane.showMessageDialog(new JFrame("preview result"), output);
            }
        });

        reserveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doReservation();
                int confNo = getReserveOutput();
                if (confNo == 0)
                    JOptionPane.showMessageDialog(null, "fail to make a reservation!");
                else
                    JOptionPane.showMessageDialog(new JFrame("confirmation number"), "Your reservation number is "+confNo);
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                servicePanel.setVisible(false);
            }
        });
    }


    /**logistics functions*/
    public ServicePanel(Delegate delegate) {
        this.delegate = delegate;

        initializeClerkBtnPanel();
        setUpClerkBtnPanel();
        initializeCustomerBtnPanel();
        setUpCustomerBtnPanel();

        customerBtnPanel.setOpaque(false);
        clerkBtnPanel.setOpaque(false);
        servicePanel.add(customerBtnPanel);
        servicePanel.add(clerkBtnPanel);
        servicePanel.setOpaque(false);
    }
    public JPanel getServicePanel() {
        return this.servicePanel;
    }
    public JButton getBackBtn() {return this.backBtn;}

}
