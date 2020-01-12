package ca.ubc.cs304.model.ModelForService;

public class ClerkGenerateReportForBranchReturnModel {
    CustomerGetAvailableVehicleModel[] rentedVehicles;
    int[] returnsPerVehicleType;
    float[] revenuePerVehicleType;
    String[] vehicleTypesReturned;
    int totalReturns;
    float totalRevenue;

    public void setRentedVehicles(CustomerGetAvailableVehicleModel[] rentedVehicles) {
        this.rentedVehicles = rentedVehicles;
    }
    public CustomerGetAvailableVehicleModel[] getRentedVehicles() {
        return rentedVehicles;
    }

    public void setReturnsPerVehicleType(int[] returnsPerVehicleType) {
        this.returnsPerVehicleType = returnsPerVehicleType;
    }
    public int[] getReturnsPerVehicleType() {
        return returnsPerVehicleType;
    }

    public void setRevenuePerVehicleType(float[] revenuePerVehicleType) {
        this.revenuePerVehicleType = revenuePerVehicleType;
    }
    public float[] getRevenuePerVehicleType() {
        return revenuePerVehicleType;
    }

    public void setVehicleTypesReturned(String[] vehicleTypesReturned) {
        this.vehicleTypesReturned = vehicleTypesReturned;
    }
    public String[] getVehicleTypesReturned() {
        return vehicleTypesReturned;
    }

    public void setTotalReturns(int totalReturns) {
        this.totalReturns = totalReturns;
    }
    public int getTotalReturns() {
        return totalReturns;
    }

    public void setTotalRevenue(float totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    public float getTotalRevenue() {
        return totalRevenue;
    }
}
