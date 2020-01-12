package ca.ubc.cs304.model.ModelForService;

public class ClerkGenerateReportForReturnModel {
    CustomerGetAvailableVehicleModel[] returnedVehicles;
    int[] returnsPerVehicleType;
    float[] revenuePerVehicleType;
    String[] vehicleTypesReturned;
    int[] returnsPerBranch;
    float[] revenuePerBranch;
    String[] Branches;
    int totalReturns;
    float totalRevenue;

    public void setReturnedVehicles(CustomerGetAvailableVehicleModel[] rentedVehicles) {
        this.returnedVehicles = rentedVehicles;
    }
    public CustomerGetAvailableVehicleModel[] getReturnedVehicles() {
        return returnedVehicles;
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

    public void setReturnsPerBranch(int[] returnsPerBranch) {
        this.returnsPerBranch = returnsPerBranch;
    }
    public int[] getReturnsPerBranch() {
        return returnsPerBranch;
    }

    public void setRevenuePerBranch(float[] revenuePerBranch) {
        this.revenuePerBranch = revenuePerBranch;
    }
    public float[] getRevenuePerBranch() {
        return revenuePerBranch;
    }

    public void setBranches(String[] branches) {
        Branches = branches;
    }
    public String[] getBranches() {
        return Branches;
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