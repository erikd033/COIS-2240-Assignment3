public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { AVAILABLE, RESERVED, RENTED, MAINTENANCE, OUTOFSERVICE }

    public Vehicle(String make, String model, int year) {
        this.make = capitalize(make);
        this.model = capitalize(model);
        this.year = year;
        this.status = VehicleStatus.AVAILABLE;
        this.licensePlate = null;
    }
    
    public Vehicle() {
        this(null, null, 0);
    }

    // TASK 2 PART 1
    public void setLicensePlate(String plate) {
        if (!isValidPlate(plate)) {
            throw new IllegalArgumentException("Invalid license plate format. Must be three letters followed by three digits (e.g., ABC123).");
        }
        this.licensePlate = plate.toUpperCase();
    }
    
    private boolean isValidPlate(String plate) {
        if (plate == null || plate.length() != 6) {
            return false;
        }

        for (int i = 0; i < 3; i++) {
            if (!Character.isLetter(plate.charAt(i)) || !Character.isUpperCase(plate.charAt(i))) {
                return false;
            }
        }

        for (int i = 3; i < 6; i++) {
            if (!Character.isDigit(plate.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    // Capitalize method
    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public void setStatus(VehicleStatus status) {
    	this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }

    public String getMake() { return make; }

    public String getModel() { return model;}

    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }

}
