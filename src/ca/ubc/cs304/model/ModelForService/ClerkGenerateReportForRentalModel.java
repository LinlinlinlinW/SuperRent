package ca.ubc.cs304.model.ModelForService;

// case 1 in generating report
public class ClerkGenerateReportForRentalModel {
    CustomerGetAvailableVehicleModel[] rentedVehicles;
    int[] rentedPerVehicleType;
    String[] vehicleTypesRented;
    int[] rentalsPerBranch;
    String[] Branches;
    int totalRentals;

    public void setRentedVehicles(CustomerGetAvailableVehicleModel[] rentedVehicles) {
        this.rentedVehicles = rentedVehicles;
    }
    public CustomerGetAvailableVehicleModel[] getRentedVehicles() {
        return rentedVehicles;
    }

    public void setRentedPerVehicleType(int[] rentedPerVehicleType) {
        this.rentedPerVehicleType = rentedPerVehicleType;
    }
    public int[] getRentedPerVehicleType() {
        return rentedPerVehicleType;
    }

    public void setVehicleTypesRented(String[] vehicleTypesRented) {
        this.vehicleTypesRented = vehicleTypesRented;
    }
    public String[] getVehicleTypesRented() {
        return vehicleTypesRented;
    }

    public void setRentalsPerBranch(int[] rentalsPerBranch) {
        this.rentalsPerBranch = rentalsPerBranch;
    }
    public int[] getRentalsPerBranch() {
        return rentalsPerBranch;
    }

    public void setBranches(String[] branches) {
        Branches = branches;
    }
    public String[] getBranches() {
        return Branches;
    }

    public void setTotalRentals(int totalRentals) {
        this.totalRentals = totalRentals;
    }
    public int getTotalRentals() {
        return totalRentals;
    }
}