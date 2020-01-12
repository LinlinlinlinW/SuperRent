package ca.ubc.cs304.model.ModelForManipulation;

/**
 * This class tends to be an example, useless(?)
 */
public class DemoBranchModel {
	private String address;
	private String city;
	private int id;
	private String name;
	private int phoneNumber;
	
	public DemoBranchModel() {	}

	public void setAddress(String address) {
        this.address = address;
    }
	public String getAddress() {
		return address;
	}

	public void setCity(String city) {
        this.city = city;
    }
	public String getCity() {
		return city;
	}

	public void setId(int id) {
	    this.id = id;
    }
	public int getId() {
		return id;
	}

	public void setName(String name){
	    this.name = name;
    }
	public String getName() {
		return name;
	}

	public void setPhoneNumber(int phoneNumber) {
	    this.phoneNumber = phoneNumber;
    }
	public int getPhoneNumber() {
		return phoneNumber;
	}
}
