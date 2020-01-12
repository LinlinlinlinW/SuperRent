package ca.ubc.cs304.model.ModelForManipulation;

/**
 * The intent for this class is to update/store information about a single vehicle
 */
public class VehiclesModel {
  private String vlicense;
  private String make;
  private String model;
  private int year;
  private String color;
  private int odometer;
  private String status;
  private String vtname;
  private String location;
  private String city;

  public VehiclesModel() {}

  public void setVlicense(String vlicense) {this.vlicense = vlicense;}
  public String getVlicense() {
    return vlicense;
  }

  public void setMake(String make) {this.make = make;}
  public String getMake() {
    return make;
  }

  public void setModel(String model) {this.model = model;}
  public String getModel() {
    return model;
  }

  public void setYear(int year) {this.year = year;}
  public int getYear() {
    return year;
  }

  public void setColor(String color) {this.color = color;}
  public String getColor() {
    return color;
  }

  public void setOdometer(int odometer) {this.odometer = odometer;}
  public int getOdometer() {
    return odometer;
  }

  public void setStatus(String status) {this.status = status;}
  public String getStatus() {
    return status;
  }

  public void setVtname(String vtname) {this.vtname = vtname;}
  public String getVtname() {
    return vtname;
  }

  public void setLocation(String location) {this.location = location;}
  public String getLocation() {
    return location;
  }

  public void setCity(String city){this.city = city;}
  public String getCity() {
    return city;
  }

}