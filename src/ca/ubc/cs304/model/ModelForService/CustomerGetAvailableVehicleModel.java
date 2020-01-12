package ca.ubc.cs304.model.ModelForService;

public class CustomerGetAvailableVehicleModel {
    private String vtname;
    private String location;
    private String vlicense;
    // DD/MM/YYYY

    private String fromDate;
    private String toDate;

    // vehicle details
    private String make;
    private String model;
    private int year;
    private String color;

    public void setVtname(String vtname) {
        this.vtname = vtname;
    }
    public String getVtname (){
        return this.vtname;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getLocation() {
        return this.location;
    }

    public void setVlicense(String vlicense) {
        this.vlicense = vlicense;
    }
    public String getVlicense() {
        return this.vlicense;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
    public String getFromDate() {
        return this.fromDate;
    }

    public void setToDate(String toDate){
        this.toDate = toDate;
    }
    public String getToDate() {
        return this.toDate;
    }

    public void setMake(String make){
        this.make = make;
    }
    public String getMake() {
        return this.make;
    }

    public void setModel(String model){
        this.model = model;
    }
    public String getModel() {
        return this.model;
    }

    public void setYear(int year){
        this.year = year;
    }
    public int getYear() {
        return this.year;
    }

    public void setColor(String color){
        this.color = color;
    }
    public String getColor() {
        return this.color;
    }
}
