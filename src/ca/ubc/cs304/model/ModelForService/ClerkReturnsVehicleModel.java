package ca.ubc.cs304.model.ModelForService;

import java.sql.Date;
import java.sql.Time;

/**
 * The intent for this class is to update/store information about a return
 */
public class ClerkReturnsVehicleModel {
  // input
  private String rid;
  private int odometer;
  private int fulltank;  // either 1 or 0


  // show up in the receipt
  private int confNo;
  private String date; // grab the current date in the computer
  private String location;
  private float value;


  public ClerkReturnsVehicleModel() {}

  public void setRid(String rid) {this.rid = rid;}
  public String getRid() {
    return rid;
  }

  public void setDate(String date) {this.date = date;}
  public String getDate() {
    return date;
  }

  public void setOdometer(int odometer) {this.odometer = odometer;}
  public int getOdometer() {
    return odometer;
  }

  public void setFulltank(int fulltank) {this.fulltank = fulltank;}
  public int getFulltank() {
    return fulltank;
  }

  public void setValue(float value) { this.value = value;}
  public float getValue() {
    return value;
  }

  public void setConfNo(int confNo) {this.confNo =confNo; }
  public int getConfNo() {return this.confNo;}

  //TODO one more set

}