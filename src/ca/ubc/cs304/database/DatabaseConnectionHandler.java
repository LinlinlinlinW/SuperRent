package ca.ubc.cs304.database;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


import ca.ubc.cs304.model.ModelForManipulation.*;
import ca.ubc.cs304.model.ModelForService.*;
import oracle.sql.TIMESTAMP;

import javax.xml.transform.Result;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    /**manipulation functions*/
    /**insertCustomer adds a new customer to the customers table in the database, takes a customersModel*/
    public void insertCustomer(int dlicense, String name, String cellphone, String address) {
        try {
            System.out.println("before insert");
            PreparedStatement ps = connection.prepareStatement("INSERT INTO customers VALUES (?,?,?,?)");
            ps.setInt(1, dlicense);
            ps.setString(2, name);
            if (cellphone == null) {
                ps.setNull(3, java.sql.Types.VARCHAR);
            } else {
                ps.setObject(3, cellphone);
            }
            if (address == null) {
                ps.setNull(4, java.sql.Types.VARCHAR);
            } else {
                ps.setString(4, address);
            }

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    public void deleteCustomer(int dlicense) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM customers WHERE DLICENSE = ?");
            ps.setInt(1, dlicense);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateCustomer(String address, String name) {
        try {
            PreparedStatement ps = connection.prepareStatement("update CUSTOMERS\n" +
                    "set customers.ADDRESS = ?\n" +
                    "where customers.NAME =  ?");
            ps.setString(1, address);
            ps.setString(2, name);

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

    }

    public ManipulateCustomersModel[] viewCustomer() {
        System.out.println("in dbhandler viewCustomer");
        ArrayList<ManipulateCustomersModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CUSTOMERS");
            while (rs.next()) {
                ManipulateCustomersModel model = new ManipulateCustomersModel();
                model.setDlicense(rs.getInt("dlicense"));
                model.setName(rs.getString("name"));
                model.setCellphone(rs.getString("cellphone"));
                model.setAddress(rs.getString("address"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new ManipulateCustomersModel[result.size()]);
    }

    /**logistics functions*/
    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");

            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }
    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }



    /**service functions*/
    // query 1
    /**
     * getAvailable Vehicles returns an array of VehiclesModel
     * takes car type, location, time interval fromDate, fromTime, toDate, toTime
     */
    public CustomerGetAvailableVehicleModel[] customerGetAvailableVehicles(String vtname, String location, String fromDate, String toDate) {
        ArrayList<CustomerGetAvailableVehicleModel> result = new ArrayList<>();
        String from = fromDate.substring(0,10) + " " + "00:00:00";
        String to = toDate.substring(0,10) + " " + "00:00:00";

        try {
            Statement stmt = connection.createStatement();
            String executeString = "SELECT * FROM vehicles WHERE status = \'available\'";
            if (vtname != null) {
                executeString += " AND vtname = \'" + vtname + "\'";
            }
            if (location != null) {
                executeString += " AND location = \'" + location + "\'";
            }
            if (fromDate != null && toDate != null) {
                Instant fromTimestamp = Instant.parse(fromDate);
                Instant toTimeStamp = Instant.parse(toDate);

                executeString += " AND vlicense NOT IN (SELECT vlicense FROM reservations WHERE (fromDate > TIMESTAMP \'" + Timestamp.from(fromTimestamp) +
                        "\' AND fromDate < TIMESTAMP \'" + Timestamp.from(toTimeStamp) +
                        "\') OR (toDate > TIMESTAMP \'" + Timestamp.from(fromTimestamp) +
                        "\' AND toDate < TIMESTAMP \'" + Timestamp.from(toTimeStamp) +
                        "\'))";
            }

            // order by location
            executeString += " ORDER BY location";
            System.out.println(executeString);
            ResultSet rs = stmt.executeQuery(executeString);


            while (rs.next()) {
                CustomerGetAvailableVehicleModel model = new CustomerGetAvailableVehicleModel();
                model.setVlicense(rs.getString("vlicense"));
                model.setMake(rs.getString("make"));
                model.setModel(rs.getString("model"));
                model.setYear(rs.getInt("year"));
                model.setColor(rs.getString("color"));
                model.setVtname(rs.getString("vtname"));
                model.setLocation(rs.getString("location"));
                result.add(model);
            }



            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // result.size() would just be the number of available cars, use that in handler
        return result.toArray(new CustomerGetAvailableVehicleModel[result.size()]);
    }

    // query 2
    public boolean checkCustomerExist(int dlicense) {
        boolean result = false;

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers WHERE dlicense = " + dlicense);
            result = rs.next();
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return result;
    }
    public int customerMakeReservation(String vtname, int dlicense, String fromDate, String toDate) {
        int confNo = 0;
        try {

            // see if there's an available vehicle
            CustomerGetAvailableVehicleModel[] available;
            String vlicense;
            available = customerGetAvailableVehicles(vtname, null, fromDate, toDate);
            if (available.length == 0) {
                throw new SQLException();
            } else {
                vlicense = available[0].getVlicense();
            }
            // figure out confNo?
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM reservations");
            rs.next();
            confNo = rs.getInt(1) + 1;    // should return just an int
            stmt.close();
            rs.close();


            Instant fromTimeStamp = Instant.parse(fromDate);
            Instant toTimeStamp = Instant.parse(toDate);

            PreparedStatement ps = connection.prepareStatement("INSERT INTO reservations VALUES (?,?,?,?,?,?,?,?)");
            ps.setInt(1, confNo);
            ps.setString(2, vtname);
            ps.setString(3, vlicense);
            ps.setInt(4, dlicense);
            ps.setTimestamp(5, Timestamp.from(fromTimeStamp));
            ps.setTimestamp(6, Timestamp.from(toTimeStamp));
            ps.setString(7, new String("" + fromTimeStamp).substring(0,10));
            ps.setString(8, new String(new String(""+toTimeStamp).substring(0,10)));

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return confNo;
    }

    // query 3
    /**
     * clerkRentVehicle returns a receipt with a lot of info for the
     * rental that was made
     * returns a message if there are no cars currently available
     */
    public String[] clerkRentVehicle(int confNo, String vtname, int dlicense, String fromDate, String toDate, String cardName, String cardNo, String expDate){
        String[] receipt = new String[10];
        System.out.println("Started rent interaction");
        try{//check if confNo exists
            if (confNo >= 1) {

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM reservations WHERE confNo = " + confNo); //QUERY FOR THE RESERVATION
                if (rs.next() == false){
                    throw new SQLException();
                }
                String lp = rs.getString("vlicense");
                int dl = rs.getInt("dlicense");
                Instant from = rs.getTimestamp("fromDate").toInstant();
                Instant to = rs.getTimestamp("toDate").toInstant();
                String fromQuery = rs.getString("fromQueryDate");
                String toQuery = rs.getString("toQueryDate");
                String type = rs.getString("vtname");
                rs.close();
                System.out.println(lp);
                ResultSet rs0 = stmt.executeQuery("SELECT * FROM vehicles WHERE vlicense =  \'" + lp + "\'");
                if (rs0.next() == false){
                    throw new SQLException();
                }

                System.out.println("Calling helper");
                receipt = clerkRentVehicleInsertQuery(lp, dl, from, to ,fromQuery,toQuery, rs0.getInt("odometer"), cardName, cardNo,expDate,confNo, rs0.getString("location"), rs0.getString("city"), type);
                System.out.println("Exit Helper");
                rs0.close();
                stmt.close();
            } else {

                //Make a new reservation if there was no reservation
                int confirmation = customerMakeReservation(vtname, dlicense, fromDate, toDate);    //Do a reservation first to check all the things such as carmodel etc...

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM reservartions WHERE confNo = " + confNo); //QUERY FOR THE RESERVATION
                if (!rs.next()){
                    throw new SQLException();
                }
                String lp = rs.getString("vlicense");
                int dl = rs.getInt("dlicense");
                Instant from = rs.getTimestamp("fromDate").toInstant();
                Instant to = rs.getTimestamp("toDate").toInstant();
                String fromQuery = rs.getString("fromQueryDate");
                String toQuery = rs.getString("toQueryDate");
                String type = rs.getString("vtname");
                rs.close();
                ResultSet rs0 = stmt.executeQuery("SELECT * FROM vehicles WHERE vlicense =  " + lp);
                if (!rs.next()){
                    throw new SQLException();
                }

                System.out.println("calling helper");
                receipt = clerkRentVehicleInsertQuery(lp, dl, from, to,fromQuery,toQuery, rs0.getInt("odometer"), cardName, cardNo,expDate,confirmation, rs0.getString("location"), rs0.getString("city"), type);
                rs0.close();
                stmt.close();

            }

        } catch (SQLException e) {
            e.getMessage();
            e.printStackTrace();
        }

        return receipt;

    }

    private String[] clerkRentVehicleInsertQuery(String vlicense, int dlicense,  Instant fromDate, Instant toDate,String fromQueryDate, String toQueryDate, int odometer,
                                                 String cardName, String cardNo, String expDate, int confNo, String location, String city, String vtname ) throws SQLException{

        String[] receipt = new String[10];

        try {
            ///////////MAKE QUERY FOR CONF NO///////////////////////////////
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) from rentals");
            rs.next();
            int rid = rs.getInt(1) + 1000;
            rs.close();
            stmt.close();
            ////////////////////////////////////////////////////////////////

            /////////////INSERT QUERY////////////////////////////////////////////////////
            PreparedStatement ps = connection.prepareStatement("INSERT INTO rentals VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");

            ps.setInt(1, rid);
            ps.setString(2, vlicense);
            ps.setInt(3, dlicense);
            ps.setTimestamp(4, Timestamp.from(fromDate));
            ps.setTimestamp(5, Timestamp.from(toDate));
            ps.setString(6, fromQueryDate);
            ps.setString(7, toQueryDate);
            ps.setInt(8, odometer);
            ps.setString(9, cardName);
            ps.setString(10, cardNo);
            ps.setString(11, expDate);
            ps.setInt(12, confNo);

            ps.executeUpdate();
            connection.commit();

            ps.close();
            ///////////////////////////////////////////////////////////////////////////////

            //build receipt
            receipt[0] = Integer.toString(confNo);
            receipt[1] = "" + fromDate;
            receipt[2] = vtname;
            receipt[3] = location;
            receipt[4] = city;

            for(int i = 0; i<receipt.length; i++){
                System.out.println(receipt[i]);
            }

        } catch (SQLException e){
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return receipt;
    }

    // query 4
    // Fred implemented below FIX IT
    /**clerkReturnVehicle returns a receipt with information for the customer
     first checks to see if the rid exists in Returns
     throws a SQL exception if the car has not been taken out*/

    public String[] clerkReturnVehicle(int rid, int odometer, int fulltank) {
        String[] receipt = new String[15];

        ////////////FOR STORING RATES ALL THEsE HAVE TO BE RETURNED/////////////////
        float wrate;
        float drate;
        float hrate;
        float wirate;
        float dirate;
        float hirate;
        float krate;
        ///////////////////////////////////////////////////////////////////////////

        try{
            ////////////////QUERIES needed for this transaction/////////////////////////////////////
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM rentals WHERE rid = " + rid);
            if (!rs.next()) {
                throw new SQLException();
            }
            String licenseplate = rs.getString("vlicense");
            Instant todaysdate = Instant.now().truncatedTo(ChronoUnit.DAYS);
            Instant startdate = rs.getTimestamp("fromDate").toInstant();
            int oldreading = rs.getInt("odometer");

            rs.close();
            ResultSet rs0 = stmt.executeQuery("SELECT * FROM vehicles WHERE vlicense = \'" + licenseplate + "\'"); //TODO check this
            if (!rs0.next()){
                throw new SQLException();
            }

            String type = rs0.getString("vtname");
            rs0.close();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM vehicleTypes WHERE vtname = \'" + type + "\'");
            if (!rs1.next()){
                throw new SQLException();
            }

            ZoneId z = ZoneId.of( "America/Vancouver" ) ;

            long daysbetween = ChronoUnit.DAYS.between(startdate, todaysdate);
            long weeks = daysbetween/7;
            long day = daysbetween % 7;
            int hours = todaysdate.atZone(z).getHour();

            wrate = rs1.getFloat("wrate");
            drate = rs1.getFloat("drate");
            hrate = rs1.getFloat("hrate");
            wirate = rs1.getFloat("wirate");
            dirate = rs1.getFloat("dirate");
            hirate = rs1.getFloat("hirate");
            krate = rs1.getFloat("krate");

            float totalcost = (wrate*weeks) + (wirate*weeks) + (drate*day)+(dirate*day) + (hrate*hours) + (hirate*hours) + (krate * (oldreading-odometer));

            rs1.close();
            stmt.close();
            ///////////////////////////////////////////////////////////////////////////////////////////


            PreparedStatement ps = connection.prepareStatement("INSERT INTO returns VALUES (?,?,?,?,?,?)");

            String s = new String(""+Instant.now().truncatedTo(ChronoUnit.DAYS)).substring(0,10);

            ps.setInt(1, rid);
            ps.setTimestamp(2, Timestamp.from(Instant.now()) );
            ps.setString(3, s);
            ps.setInt(4, odometer);
            ps.setInt(5, fulltank);
            ps.setFloat(6,totalcost);


            ps.executeUpdate();


            connection.commit();

            ps.close();

            receipt[0] = Integer.toString(rid);
            receipt[1] = ""+Instant.now();
            receipt[2] = Integer.toString(odometer);
            receipt[3] = Integer.toString(fulltank);
            receipt[4] = "Total Cost:  ";
            receipt[5] =  "Weekly Rate: " + wrate + " * " + weeks;
            receipt[6] =  "Weekly Insurance Rate: " + wirate + " * " + weeks;
            receipt[7] =  "Daily Rate: " + drate + " * " + day;
            receipt[8] =  "Daily Insurance Rate: " + dirate + " * " + day;
            receipt[9] =  "Hourly Rate: " + hrate + " * " + hours;
            receipt[10] = "Hourly Insurance Rate: " + hirate + " * " + hours;
            receipt[11] = "Kilometer Rate: " + krate + " * " + Integer.toString(oldreading-odometer) + " Kilometers Driven";
            receipt[12] = "-------------------------------------------------------------------------------------------";
            receipt[13] = ""+totalcost;


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }

        return receipt;
    }



    /**
     clerkGenerateReport generates a set of tuples as laid out in the deliverable outline
     Put 1: Daily Rentals
     2: Daily Rentals for Branch
     3: Daily Returns
     4: Daily Returns for Branch
     */
    // query 5
    public ClerkGenerateReportForRentalModel clerkGenerateReportForRental() {
            ClerkGenerateReportForRentalModel result = new ClerkGenerateReportForRentalModel();
            try {
                // get rented vehicles
                String s = new String(""+Instant.now()).substring(0,10);
                Statement stmt = connection.createStatement();
                String executeStr = "SELECT * FROM rentals, vehicles WHERE rentals.fromQueryDate = \'"
                        + s
                        + "\' AND rentals.vlicense = vehicles.vlicense";
                executeStr += " ORDER BY vehicles.location, vehicles.vtname";
                ResultSet rs = stmt.executeQuery(executeStr);

                ArrayList<CustomerGetAvailableVehicleModel> carList = new ArrayList<>();
                while (rs.next()) {
                    CustomerGetAvailableVehicleModel model = new CustomerGetAvailableVehicleModel();
                    model.setVlicense(rs.getString("vlicense"));
                    model.setMake(rs.getString("make"));
                    model.setModel(rs.getString("model"));
                    model.setYear(rs.getInt("year"));
                    model.setColor(rs.getString("color"));
                    model.setVtname(rs.getString("vtname"));
                    model.setLocation(rs.getString("location"));
                    carList.add(model);
                }
//                if (carList.size() == 0) {
//                    result.setRentedVehicles(new CustomerGetAvailableVehicleModel[0]);
//                    System.out.println("carlist is empty");
//                } else {
                    result.setRentedVehicles(carList.toArray(new CustomerGetAvailableVehicleModel[carList.size()]));
//                }
                rs.close();
                stmt.close();

                // get number rented per vehicle type
                ArrayList<Integer> perVT = new ArrayList<>();
                Statement stmt2 = connection.createStatement();
                System.out.println(Instant.now().truncatedTo(ChronoUnit.DAYS));
                System.out.println(s);
                String executeStr2 = "SELECT COUNT(*) FROM rentals, vehicles WHERE rentals.fromQueryDate = \'"
                        + s
                        + "\' AND rentals.vlicense = vehicles.vlicense";
                executeStr2 += " GROUP BY vehicles.vtname";
                System.out.println(executeStr2);
                ResultSet rs2 = stmt2.executeQuery(executeStr2);
                while (rs2.next()) {
                    perVT.add(rs2.getInt(1));  // should return just an int
                }
                result.setRentedPerVehicleType(perVT.stream().mapToInt(i -> i).toArray());
                rs2.close();
                stmt2.close();

                // get corresponding vehicle type
                ArrayList<String> VT = new ArrayList<>();
                Statement stmt5 = connection.createStatement();
                String executeStr5 = "SELECT DISTINCT vehicles.vtname FROM rentals, vehicles WHERE rentals.fromQueryDate = \'"
                        + s
                        + "\' AND rentals.vlicense = vehicles.vlicense";
                executeStr5 += " GROUP BY vehicles.vtname";
                ResultSet rs5 = stmt5.executeQuery(executeStr5);
                while (rs5.next()) {
                    VT.add(rs5.getString(1));
                }
                result.setVehicleTypesRented(VT.toArray(new String[VT.size()]));
                rs5.close();
                stmt5.close();

                // get number rentals per branch
                ArrayList<Integer> perBranch = new ArrayList<>();
                Statement stmt3 = connection.createStatement();
                String executeStr3 = "SELECT COUNT(*) FROM rentals, vehicles WHERE rentals.fromQueryDate = \'"
                        + s
                        + "\' AND rentals.vlicense = vehicles.vlicense";
                executeStr3 += " GROUP BY vehicles.location";
                ResultSet rs3 = stmt3.executeQuery(executeStr3);
                while (rs3.next()) {
                    perBranch.add(rs3.getInt(1));  // should return just an int
                }
                result.setRentalsPerBranch(perBranch.stream().mapToInt(i -> i).toArray());
                rs3.close();
                stmt3.close();

                // get corresponding branch
                ArrayList<String> branches = new ArrayList<>();
                Statement stmt6 = connection.createStatement();
                String executeStr6 = "SELECT DISTINCT vehicles.location FROM rentals, vehicles WHERE rentals.fromQueryDate = \'"
                        + s
                        + "\' AND rentals.vlicense = vehicles.vlicense";
                executeStr6 += " GROUP BY vehicles.location";
                ResultSet rs6 = stmt6.executeQuery(executeStr6);
                while (rs6.next()) {
                    branches.add(rs6.getString(1));
                }
                result.setBranches(branches.toArray(new String[branches.size()]));
                rs6.close();
                stmt6.close();

                //get number of total rentals
                Statement stmt4 = connection.createStatement();
                String executeStr4 = "SELECT COUNT(*) FROM rentals WHERE rentals.fromQueryDate = \'"
                        + s + "\'";
                ResultSet rs4 = stmt4.executeQuery(executeStr4);
                rs4.next();      // NOT SURE IF THIS IS NEEDED TO GET FIRST AND ONLY ELEMENT
                result.setTotalRentals(rs4.getInt(1));
                rs4.close();
                stmt4.close();

                System.out.println(result.getRentedVehicles());
                System.out.println(result.getBranches());
                System.out.println(result.getTotalRentals());


            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;

    }

    // query 6
    public ClerkGenerateReportForBranchRentalModel clerkGenerateReportForBranchRental(String location, String city) {

        ClerkGenerateReportForBranchRentalModel result = new ClerkGenerateReportForBranchRentalModel();
        try {
            String s = new String(""+Instant.now()).substring(0,10);

            Statement stmt = connection.createStatement();
            String executeStr = "SELECT * FROM rentals, vehicles WHERE rentals.fromQueryDate = \'"
                    + s + "\' AND location =  \'"
                    + location + "\' AND city = \'"
                    + city + "\' AND vehicles.vlicense = rentals.vlicense";
            executeStr += " ORDER BY vehicles.vtname";
            ResultSet rs = stmt.executeQuery(executeStr);

            ArrayList<CustomerGetAvailableVehicleModel> carList = new ArrayList<>();
            while (rs.next()) {
                CustomerGetAvailableVehicleModel model = new CustomerGetAvailableVehicleModel();
                model.setVlicense(rs.getString("vlicense"));
                model.setMake(rs.getString("make"));
                model.setModel(rs.getString("model"));
                model.setYear(rs.getInt("year"));
                model.setColor(rs.getString("color"));
                model.setVtname(rs.getString("vtname"));
                model.setLocation(rs.getString("location"));
                carList.add(model);
            }
            result.setRentedVehicles(carList.toArray(new CustomerGetAvailableVehicleModel[carList.size()]));
            rs.close();
            stmt.close();

            // get number rented per vehicle type
            ArrayList<Integer> perVT = new ArrayList<>();
            Statement stmt2 = connection.createStatement();
            String executeStr2 = "SELECT COUNT(*) FROM rentals, vehicles WHERE rentals.fromQueryDate = \'"
                    + s + "\' AND location =  \'"
                    + location + "\' AND city = \'"
                    + city + "\' "
                    + " AND rentals.vlicense = vehicles.vlicense";
            executeStr2 += " GROUP BY vehicles.vtname";
            ResultSet rs2 = stmt2.executeQuery(executeStr2);
            while (rs2.next()) {
                perVT.add(rs2.getInt(1));  // should return just an int
            }
            result.setRentedPerVehicleType(perVT.stream().mapToInt(i -> i).toArray());
            rs2.close();
            stmt2.close();

            // get corresponding vehicle type
            ArrayList<String> VT = new ArrayList<>();
            Statement stmt5 = connection.createStatement();
            String executeStr5 = "SELECT DISTINCT vehicles.vtname FROM rentals, vehicles WHERE rentals.fromQueryDate = \'"
                    + s + "\' AND location =  \'"
                    + location + "\' AND city = \'"
                    + city + "\' "
                    + " AND rentals.vlicense = vehicles.vlicense";
            executeStr5 += " GROUP BY vehicles.vtname";
            ResultSet rs5 = stmt5.executeQuery(executeStr5);
            while (rs5.next()) {
                VT.add(rs5.getString(1));
            }
            result.setVehicleTypesRented(VT.toArray(new String[VT.size()]));
            rs5.close();
            stmt5.close();

            //get number of total rentals
            Statement stmt4 = connection.createStatement();
            String executeStr4 = "SELECT COUNT(*) FROM rentals, vehicles WHERE rentals.fromQueryDate = \'"
                    + s + "\' AND location =  \'"
                    + location + "\' AND city = \'"
                    + city + "\' "
                    + " AND rentals.vlicense = vehicles.vlicense";
            ResultSet rs4 = stmt4.executeQuery(executeStr4);
            rs4.next();      // NOT SURE IF THIS IS NEEDED TO GET FIRST AND ONLY ELEMENT
            result.setTotalRentals(rs4.getInt(1));
            rs4.close();
            stmt4.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // query 7
    public ClerkGenerateReportForReturnModel clerkGenerateReportForReturn() {
        ClerkGenerateReportForReturnModel result = new ClerkGenerateReportForReturnModel();
        try {
            String s = new String(""+Instant.now()).substring(0,10);

            Statement stmt = connection.createStatement();
            String executeStr = "SELECT * FROM rentals, vehicles, returns WHERE returns.queryDate = \'"
                    + s
                    + "\' AND  returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            executeStr += " ORDER BY vehicles.location, vehicles.vtname";
            ResultSet rs = stmt.executeQuery(executeStr);

            ArrayList<CustomerGetAvailableVehicleModel> carList = new ArrayList<>();
            while (rs.next()) {
                CustomerGetAvailableVehicleModel model = new CustomerGetAvailableVehicleModel();
                model.setVlicense(rs.getString("vlicense"));
                model.setMake(rs.getString("make"));
                model.setModel(rs.getString("model"));
                model.setYear(rs.getInt("year"));
                model.setColor(rs.getString("color"));
                model.setVtname(rs.getString("vtname"));
                model.setLocation(rs.getString("location"));
                carList.add(model);
            }
            result.setReturnedVehicles(carList.toArray(new CustomerGetAvailableVehicleModel[carList.size()]));
            rs.close();
            stmt.close();

            // get number returns per vehicle type
            ArrayList<Integer> perVT = new ArrayList<>();
            Statement stmt2 = connection.createStatement();
            String executeStr2 = "SELECT COUNT(*) FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s
                    + "\' AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            executeStr2 += " GROUP BY vehicles.vtname";
            ResultSet rs2 = stmt2.executeQuery(executeStr2);
            while (rs2.next()) {
                perVT.add(rs2.getInt(1));  // should return just an int
            }
            result.setReturnsPerVehicleType(perVT.stream().mapToInt(i -> i).toArray());
            rs2.close();
            stmt2.close();

            // get number returns per branch
            ArrayList<Integer> perBranch = new ArrayList<>();
            Statement stmt3 = connection.createStatement();
            String executeStr3 = "SELECT COUNT(*) FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s
                    + "\' AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            executeStr3 += " GROUP BY vehicles.location";
            ResultSet rs3 = stmt3.executeQuery(executeStr3);
            while (rs3.next()) {
                perBranch.add(rs3.getInt(1));  // should return just an int
            }
            result.setReturnsPerBranch(perBranch.stream().mapToInt(i -> i).toArray());
            rs3.close();
            stmt3.close();


            // get revenue per vehicle type
            ArrayList<Float> revPerVT = new ArrayList<>();
            Statement stmt4 = connection.createStatement();
            String executeStr4 = "SELECT SUM(returns.value) FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s
                    + "\' AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            executeStr4 += " GROUP BY vehicles.vtname";
            ResultSet rs4 = stmt4.executeQuery(executeStr4);
            while (rs4.next()) {
                revPerVT.add(rs4.getFloat(1));  // should return just an int
            }
            float[] floatArrayPerVT = new float[revPerVT.size()];
            int i = 0;
            for (Float f : revPerVT) {
                floatArrayPerVT[i++] = (f != null ? f : Float.NaN);
            }
            result.setRevenuePerVehicleType(floatArrayPerVT);
            rs4.close();
            stmt4.close();

            // get revenue per branch
            ArrayList<Float> revPerBranch = new ArrayList<>();
            Statement stmt5 = connection.createStatement();
            String executeStr5 = "SELECT SUM(returns.value) FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s
                    + "\' AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            executeStr5 += " GROUP BY vehicles.location";
            ResultSet rs5 = stmt5.executeQuery(executeStr5);
            while (rs5.next()) {
                revPerBranch.add(rs5.getFloat(1));  // should return just an int
            }
            float[] floatArrayPerBranch = new float[revPerBranch.size()];
            i = 0;
            for (Float f : revPerBranch) {
                floatArrayPerBranch[i++] = (f != null ? f : Float.NaN);
            }
            result.setRevenuePerBranch(floatArrayPerBranch);
            rs5.close();
            stmt5.close();

            // get corresponding vehicle type
            ArrayList<String> VT = new ArrayList<>();
            Statement stmt7 = connection.createStatement();
            String executeStr7 = "SELECT DISTINCT vehicles.vtname FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s
                    + "\' AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            executeStr7 += " GROUP BY vehicles.vtname";
            ResultSet rs7 = stmt7.executeQuery(executeStr7);
            while (rs7.next()) {
                VT.add(rs7.getString(1));
            }
            result.setVehicleTypesReturned(VT.toArray(new String[VT.size()]));
            rs7.close();
            stmt7.close();


            // get corresponding branch
            ArrayList<String> branches = new ArrayList<>();
            Statement stmt8 = connection.createStatement();
            String executeStr8 = "SELECT DISTINCT vehicles.location FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s
                    + "\' AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            executeStr8 += " GROUP BY vehicles.location";
            ResultSet rs8 = stmt8.executeQuery(executeStr8);
            while (rs8.next()) {
                branches.add(rs8.getString(1));
            }
            result.setBranches(branches.toArray(new String[branches.size()]));
            rs8.close();
            stmt8.close();

            //get number of total returns
            Statement stmt9 = connection.createStatement();
            String executeStr9 = "SELECT COUNT(*) FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s
                    + "\' AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            ResultSet rs9 = stmt9.executeQuery(executeStr9);
            rs9.next();      // NOT SURE IF THIS IS NEEDED TO GET FIRST AND ONLY ELEMENT
            result.setTotalReturns(rs9.getInt(1));
            rs9.close();
            stmt9.close();

            //get number of total revenue
            Statement stmt6 = connection.createStatement();
            String executeStr6 = "SELECT SUM(returns.value) FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s
                    + "\' AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            ResultSet rs6 = stmt6.executeQuery(executeStr6);
            rs6.next();      // NOT SURE IF THIS IS NEEDED TO GET FIRST AND ONLY ELEMENT
            result.setTotalRevenue(rs6.getFloat(1));
            rs6.close();
            stmt6.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // query 8
    public ClerkGenerateReportForBranchReturnModel clerkGenerateReportForBranchReturn(String location, String city) {
        ClerkGenerateReportForBranchReturnModel result = new ClerkGenerateReportForBranchReturnModel();
        try {
            String s = new String(""+Instant.now()).substring(0,10);

            Statement stmt = connection.createStatement();
            String executeStr = "SELECT * FROM rentals, vehicles, returns WHERE returns.queryDate = \'"
                    + s + "\' AND location =  \'"
                    + location + "\' AND city = \'"
                    + city + "\' "
                    + " AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            executeStr += " ORDER BY vehicles.location, vehicles.vtname";
            ResultSet rs = stmt.executeQuery(executeStr);

            ArrayList<CustomerGetAvailableVehicleModel> carList = new ArrayList<>();
            while (rs.next()) {
                CustomerGetAvailableVehicleModel model = new CustomerGetAvailableVehicleModel();
                model.setVlicense(rs.getString("vlicense"));
                model.setMake(rs.getString("make"));
                model.setModel(rs.getString("model"));
                model.setYear(rs.getInt("year"));
                model.setColor(rs.getString("color"));
                model.setVtname(rs.getString("vtname"));
                model.setLocation(rs.getString("location"));
                carList.add(model);
            }
            result.setRentedVehicles(carList.toArray(new CustomerGetAvailableVehicleModel[carList.size()]));
            rs.close();
            stmt.close();

            // get number returns per vehicle type
            ArrayList<Integer> perVT = new ArrayList<>();
            Statement stmt2 = connection.createStatement();
            String executeStr2 = "SELECT COUNT(*) FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s + "\' AND location =  \'"
                    + location + "\' AND city = \'"
                    + city + "\' "
                    + " AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            executeStr2 += " GROUP BY vehicles.vtname";
            ResultSet rs2 = stmt2.executeQuery(executeStr2);
            while (rs2.next()) {
                perVT.add(rs2.getInt(1));  // should return just an int
            }
            result.setReturnsPerVehicleType(perVT.stream().mapToInt(i -> i).toArray());
            rs2.close();
            stmt2.close();

            // get revenue per vehicle type
            ArrayList<Float> revPerVT = new ArrayList<>();
            Statement stmt4 = connection.createStatement();
            String executeStr4 = "SELECT SUM(returns.value) FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s + "\' AND location =  \'"
                    + location + "\' AND city = \'"
                    + city + "\' "
                    + " AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            executeStr4 += " GROUP BY vehicles.vtname";
            ResultSet rs4 = stmt4.executeQuery(executeStr4);
            while (rs4.next()) {
                revPerVT.add(rs4.getFloat(1));  // should return just an int
            }
            float[] floatArrayPerVT = new float[revPerVT.size()];
            int i = 0;
            for (Float f : revPerVT) {
                floatArrayPerVT[i++] = (f != null ? f : Float.NaN);
            }
            result.setRevenuePerVehicleType(floatArrayPerVT);
            rs4.close();
            stmt4.close();


            // get corresponding vehicle type
            ArrayList<String> VT = new ArrayList<>();
            Statement stmt7 = connection.createStatement();
            String executeStr7 = "SELECT DISTINCT vehicles.vtname FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s + "\' AND location =  \'"
                    + location + "\' AND city = \'"
                    + city + "\' "
                    + " AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            executeStr7 += " GROUP BY vehicles.vtname";
            ResultSet rs7 = stmt7.executeQuery(executeStr7);
            while (rs7.next()) {
                VT.add(rs7.getString(1));
            }
            result.setVehicleTypesReturned(VT.toArray(new String[VT.size()]));
            rs7.close();
            stmt7.close();

            //get number of total returns
            Statement stmt5 = connection.createStatement();
            String executeStr5 = "SELECT COUNT(*) FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s + "\' AND location =  \'"
                    + location + "\' AND city = \'"
                    + city + "\' "
                    + " AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            ResultSet rs5 = stmt5.executeQuery(executeStr5);
            rs5.next();      // NOT SURE IF THIS IS NEEDED TO GET FIRST AND ONLY ELEMENT
            result.setTotalReturns(rs5.getInt(1));
            rs5.close();
            stmt5.close();

            //get number of total revenue
            Statement stmt6 = connection.createStatement();
            String executeStr6 = "SELECT SUM(returns.value) FROM rentals, returns, vehicles WHERE returns.queryDate = \'"
                    + s + "\' AND location =  \'"
                    + location + "\' AND city = \'"
                    + city + "\' "
                    + " AND returns.rid = rentals.rid AND rentals.vlicense = vehicles.vlicense";
            ResultSet rs6 = stmt6.executeQuery(executeStr6);
            rs6.next();      // NOT SURE IF THIS IS NEEDED TO GET FIRST AND ONLY ELEMENT
            result.setTotalRevenue(rs6.getFloat(1));
            rs6.close();
            stmt6.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<ArrayList> viewAllTables() {
        ArrayList<ArrayList> results = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet r0 = stmt.executeQuery("SELECT * FROM VEHICLETYPES");
            ArrayList<ArrayList> result0 = new ArrayList<ArrayList>();
            while(r0.next()) {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(r0.getString("vtname"));
                temp.add(r0.getString("features"));
                temp.add(Float.toString(r0.getFloat("wrate")));
                temp.add(Float.toString(r0.getFloat("drate")));
                temp.add(Float.toString(r0.getFloat("hrate")));
                temp.add(Float.toString(r0.getFloat("wirate")));
                temp.add(Float.toString(r0.getFloat("dirate")));
                temp.add(Float.toString(r0.getFloat("hirate")));
                temp.add(Float.toString(r0.getFloat("krate")));
                result0.add(temp);
            }
            r0.close();

            ResultSet r1 = stmt.executeQuery("SELECT  * FROM CUSTOMERS");
            ArrayList<ArrayList> result1 = new ArrayList<ArrayList>();
            while(r1.next()) {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(Integer.toString(r1.getInt("dlicense")));
                temp.add(r1.getString("name"));
                temp.add(r1.getString("cellphone"));
                temp.add(r1.getString("address"));
                result1.add(temp);
            }
            r1.close();

            ResultSet r2 = stmt.executeQuery("SELECT  * FROM RESERVATIONS");
            ArrayList<ArrayList> result2 = new ArrayList<ArrayList>();
            while(r2.next()) {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(Integer.toString(r2.getInt("confNo")));
                temp.add(r2.getString("vtname"));
                temp.add(r2.getString("vlicense"));
                temp.add(Integer.toString(r2.getInt("dlicense")));
                temp.add(""+ r2.getTimestamp("fromDate").toInstant());
                temp.add("" + r2.getTimestamp("toDate").toInstant());
                temp.add(""+ r2.getString("fromQueryDate"));
                temp.add("" + r2.getString("toQueryDate"));
                result2.add(temp);
            }
            r2.close();

            ResultSet r3 = stmt.executeQuery("SELECT  * FROM VEHICLES");
            ArrayList<ArrayList> result3 = new ArrayList<ArrayList>();
            while(r3.next()) {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(r3.getString("vlicense"));
                temp.add(r3.getString("make"));
                temp.add(r3.getString("model"));
                temp.add(""+ r3.getInt("year"));
                temp.add(r3.getString("color"));
                temp.add(""+r3.getInt("odometer"));
                temp.add(r3.getString("status"));
                temp.add(r3.getString("vtname"));
                temp.add(r3.getString("location"));
                temp.add(r3.getString("city"));
                result3.add(temp);
            }
            r3.close();


            ResultSet r4 = stmt.executeQuery( "SELECT * FROM RENTALS");
            ArrayList<ArrayList> result4 = new ArrayList<ArrayList>();
            while(r4.next()) {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(""+r4.getInt("rid"));
                temp.add(r4.getString("vlicense"));
                temp.add(""+r4.getInt("dlicense"));
                temp.add(""+ r4.getTimestamp("fromDate").toInstant());
                temp.add("" + r4.getTimestamp("toDate").toInstant());
                temp.add(""+ r4.getString("fromQueryDate"));
                temp.add("" + r4.getString("toQueryDate"));
                temp.add(""+ r4.getInt("odometer"));
                temp.add(r4.getString("cardName"));
                temp.add(r4.getString("cardNo"));
                temp.add(r4.getString("expDate"));
                temp.add(""+r4.getInt("confNo"));
                result4.add(temp);
            }
            r4.close();

            ResultSet r5 = stmt.executeQuery("SELECT * FROM RETURNS");
            ArrayList<ArrayList> result5 = new ArrayList<ArrayList>();
            while(r5.next()) {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(Integer.toString(r5.getInt("rid")));
                temp.add("" + r5.getTimestamp("return_date").toInstant());
                temp.add("" + r5.getInt("odometer"));
                temp.add("" + r5.getInt("fulltank"));
                temp.add("" + r5.getFloat("value"));
                result5.add(temp);
            }
            r5.close();

            results.add(result0);
            results.add(result1);
            results.add(result2);
            results.add(result3);
            results.add(result4);
            results.add(result5);





        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }


//        return result.toArray(new ManipulateCustomersModel[result.size()]);
//          get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}

    /******************************PARTNERS*******************************/

    // TRY TO MAKE A QUERY
    // View the number of available vehicles for a specific car type, location, and time interval.
    // The user should be able to provide any subset of {car type, location, time interval} to
    // view the available vehicles. If the user provides no information, your application should
    // automatically return a list of all vehicles (at that branch) sorted in some reasonable way
    // for the user to peruse.



    // If a customer is new, add the customer’s details to the database.


    /******************belows are demo examples, including "delete", "insert","getBranchInfo","update" query***************/
//	public void deleteBranch(int branchId) {
//		try {
//			PreparedStatement ps = connection.prepareStatement("DELETE FROM branch WHERE branch_id = ?");
//			ps.setInt(1, branchId);
//
//			int rowCount = ps.executeUpdate();
//			if (rowCount == 0) {
//				System.out.println(WARNING_TAG + " Branch " + branchId + " does not exist!");
//			}
//
//			connection.commit();
//
//			ps.close();
//		} catch (SQLException e) {
//			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//			rollbackConnection();
//		}
//	}

    //	public void insertBranch() {
//		try {
//			PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?,?,?,?)");
//			ps.setInt(1, model.getId());
//			ps.setString(2, model.getName());
//			ps.setString(3, model.getAddress());
//			ps.setString(4, model.getCity());
//			if (model.getPhoneNumber() == 0) {
//				ps.setNull(5, java.sql.Types.INTEGER);
//			} else {
//				ps.setInt(5, model.getPhoneNumber());
//			}
//
//			ps.executeUpdate();
//			connection.commit();
//
//			ps.close();
//		} catch (SQLException e) {
//			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//			rollbackConnection();
//		}
//	}
//	public DemoBranchModel[] getBranchInfo() {
//		ArrayList<DemoBranchModel> result = new ArrayList<DemoBranchModel>();
//
//		try {
//			Statement stmt = connection.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM branch");
//
//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}
//
//			while(rs.next()) {
//				DemoBranchModel model = new DemoBranchModel();
//				model.setAddress(rs.getString("branch_addr"));
//				model.setCity(rs.getString("branch_city"));
//				model.setId(rs.getInt("branch_id"));
//				model.setName(rs.getString("branch_name"));
//				model.setPhoneNumber(rs.getInt("branch_phone"));
//				result.add(model);
//			}
//
//			rs.close();
//			stmt.close();
//		} catch (SQLException e) {
//			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//		}
//
//		return result.toArray(new DemoBranchModel[result.size()]);
//	}
//
//	public void updateBranch(int id, String name) {
//		try {
//		  PreparedStatement ps = connection.prepareStatement("UPDATE branch SET branch_name = ? WHERE branch_id = ?");
//		  ps.setString(1, name);
//		  ps.setInt(2, id);
//
//		  int rowCount = ps.executeUpdate();
//		  if (rowCount == 0) {
//		      System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
//		  }
//
//		  connection.commit();
//
//		  ps.close();
//		} catch (SQLException e) {
//			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//			rollbackConnection();
//		}
//	}
}
