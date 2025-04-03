import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.*;

public class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    private static RentalSystem instance;
    
    // TASK 2 PART 3
    private RentalSystem() {
        loadData();
        if (instance != null) {
            throw new IllegalStateException("Instance already created");
        }
    }

    public static RentalSystem getInstance() {
        if (instance == null) {
            instance = new RentalSystem();
        }
        return instance;
    }

    public boolean addVehicle(Vehicle vehicle) {
        if (findVehicleByPlate(vehicle.getLicensePlate()) != null) {
            System.out.println("Vehicle with this license plate already exists.");
            return false;
        }
        vehicles.add(vehicle);
        saveVehicle(vehicle);
        return true;
    }

    public boolean addCustomer(Customer customer) {
        if (findCustomerById(customer.getCustomerId()) != null) {
            System.out.println("Customer with this ID already exists.");
            return false;
        }
        customers.add(customer);
        saveCustomer(customer);
        return true;
    }

  //  TASK 2 PART 2
  public boolean rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
            rentalHistory.addRecord(record);
            saveRecord(record);
            System.out.println("Vehicle rented to " + customer.getCustomerName());
            return true;
        }
        else {
            System.out.println("Vehicle is not available for renting.");
            return false;
        }
    }

    //  TASK 2 PART 2
    public boolean returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            rentalHistory.addRecord(record);
            saveRecord(record);
            System.out.println("Vehicle returned by " + customer.getCustomerName());
            return true;
        }
        else {
            System.out.println("Vehicle is not rented.");
            return false;
        }
    }  
    
    private void saveVehicle(Vehicle vehicle) {
        try (FileWriter writer = new FileWriter("vehicles.txt", true)) {
        	String type = vehicle instanceof Car ? "Car" : vehicle instanceof Motorcycle ? "Motorcycle" : "Truck";
            String data = type + "," + vehicle.getLicensePlate() + "," + vehicle.getMake() + "," + vehicle.getModel() + "," + vehicle.getYear();

            if (vehicle instanceof Car) {
                data += "," + ((Car) vehicle).getNumSeats(); // Add number of seats for Car
            } else if (vehicle instanceof Truck) {
                data += "," + ((Truck) vehicle).getCargoCapacity(); // Add cargo capacity for Truck
            }

            data += "\n";
            writer.write(data);
            System.out.println("Vehicle saved: " + data);
        } 
        catch (IOException e) {
            System.out.println("Error saving vehicle: " + e.getMessage());
        }
    }
    
    private void saveCustomer(Customer customer) {
        try (FileWriter writer = new FileWriter("customers.txt", true)) {
            String data = customer.getCustomerId() + "," + customer.getCustomerName() + "\n";
            writer.write(data);
            System.out.println("Customer saved: " + data);
        } 
        catch (IOException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }
    
    private void saveRecord(RentalRecord record) {
        try (FileWriter writer = new FileWriter("rental_records.txt", true)) {
            String data = record.getVehicle().getLicensePlate() + "," +
                    record.getCustomer().getCustomerId() + "," +
                    record.getDate() + "," +
                    record.getAmount() + "," +
                    record.getType() + "\n";
            writer.write(data);
            System.out.println("Rental record saved: " + data);
        } 
        catch (IOException e) {
            System.out.println("Error saving rental record: " + e.getMessage());
        }
    }

    private void loadData() {
        loadVehicles();
        loadCustomers();
        loadRentalRecords();
    }

    private void loadVehicles() {
        String line = null; 
        try (BufferedReader reader = new BufferedReader(new FileReader("vehicles.txt"))) {
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String type = data[0];
                String licensePlate = data[1];
                String make = data[2];
                String model = data[3];
                int year = Integer.parseInt(data[4]);
    
                Vehicle vehicle;
                if (type.equals("Car")) {
                    if (data.length < 6) {
                        System.out.println("Error: Not enough data fields for Car in line: " + line);
                        continue;
                    }
                    int numSeats = Integer.parseInt(data[5]);
                    vehicle = new Car(make, model, year, numSeats);
                } else if (type.equals("Motorcycle")) {
                    boolean hasSidecar = false;
                    vehicle = new Motorcycle(make, model, year, hasSidecar);
                } else {
                    if (data.length < 6) {
                        System.out.println("Error: Not enough data fields for Truck in line: " + line);
                        continue;
                    }
                    double cargoCapacity = Double.parseDouble(data[5]);
                    vehicle = new Truck(make, model, year, cargoCapacity);
                }
                vehicle.setLicensePlate(licensePlate);
                vehicles.add(vehicle);
            }
        } catch (IOException e) {
            System.out.println("Error loading vehicles: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int customerId = Integer.parseInt(data[0]);
                String customerName = data[1];
                customers.add(new Customer(customerId, customerName));
            }
        } catch (IOException e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }

    private void loadRentalRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader("rental_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String licensePlate = data[0];
                int customerId = Integer.parseInt(data[1]);
                LocalDate date = LocalDate.parse(data[2]);
                double amount = Double.parseDouble(data[3]);
                String type = data[4];

                Vehicle vehicle = findVehicleByPlate(licensePlate);
                Customer customer = findCustomerById(customerId);
                if (vehicle != null && customer != null) {
                    RentalRecord record = new RentalRecord(vehicle, customer, date, amount, type);
                    rentalHistory.addRecord(record);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading rental records: " + e.getMessage());
        }
    }

    public void displayAvailableVehicles() {
    	System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    	System.out.println("---------------------------------------------------------------------------------");
    	 
        for (Vehicle v : vehicles) {
            if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
            }
        }
        System.out.println();
    }
    
    public void displayAllVehicles() {
        for (Vehicle v : vehicles) {
            System.out.println("  " + v.getInfo());
        }
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }

    public Customer findCustomerByName(String name) {
        for (Customer c : customers)
            if (c.getCustomerName().equalsIgnoreCase(name))
                return c;
        return null;
    }
}