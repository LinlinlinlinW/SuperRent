package ca.ubc.cs304.model.ModelForService;

public class ClerkGenerateReportForBranchRentalModel {
    CustomerGetAvailableVehicleModel[] rentedVehicles;
    int[] rentedPerVehicleType;
    String[] vehicleTypesRented;
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

    public void setTotalRentals(int totalRentals) {
        this.totalRentals = totalRentals;
    }
    public int getTotalRentals() {
        return totalRentals;
    }

}
