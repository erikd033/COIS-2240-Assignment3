import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

class VehicleRentalTest {
// TASK 2 PART 1
		@Test
		void testLicensePlateValidation() {
		Vehicle vehicle = new Vehicle() {};

		// Test valid plates
		vehicle.setLicensePlate("AAA100");
		assertEquals("AAA100", vehicle.getLicensePlate());

		vehicle.setLicensePlate("ABC567");
		assertEquals("ABC567", vehicle.getLicensePlate());

		vehicle.setLicensePlate("ZZZ999");
		assertEquals("ZZZ999", vehicle.getLicensePlate());

		// Test invalid plates
		assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate(""));
		assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate(null));
		assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("AAA1000"));
		assertThrows(IllegalArgumentException.class, () -> vehicle.setLicensePlate("ZZZ99"));
		}
	
// TASK 2 PART 2
		@Test
		void testRentAndReturnVehicle() {
			Vehicle vehicle = new Vehicle() {};
			Customer customer = new Customer(1, "Bjorn Dyrmishi") {};
			
	        LocalDate date = LocalDate.of(2025, 4, 1);
	        double amount = 50.0;
	        double extraFee = 3.0;
			
			assertEquals(Vehicle.VehicleStatus.AVAILABLE, vehicle.getStatus(), "Error: The vehicle should be initially available.");
			RentalSystem rentalSystem = RentalSystem.getInstance();
	        
	        boolean isRented = rentalSystem.rentVehicle(vehicle, customer, date, amount );
	        assertTrue(isRented, "Error: The vehicle should be successfully rented.");
	        assertEquals(Vehicle.VehicleStatus.RENTED, vehicle.getStatus(), "Error: The vehicle status should be RENTED.");
	        
	        boolean isReturned = rentalSystem.returnVehicle(vehicle, customer, date, extraFee );
	        assertTrue(isReturned, "Error: The vehicle should be successfully returned.");
	        assertEquals(Vehicle.VehicleStatus.AVAILABLE, vehicle.getStatus(), "Error: The vehicle status should be AVAILABLE.");
		}
		
// TASK 3 PART 3		
		@Test
		void testSingletonRentalSystem() throws Exception {
	        Constructor<RentalSystem> constructor = RentalSystem.class.getDeclaredConstructor();
	        int constructorModifiers = constructor.getModifiers();

	        assertEquals(Modifier.PRIVATE, constructorModifiers, "Constructor should be private to enforce Singleton pattern.");
	        RentalSystem instance = RentalSystem.getInstance();
	        

	        assertNotNull(instance, "Instance obtained from getInstance() should not be null.");
	        constructor.setAccessible(true); 
	        
	        try {
	            RentalSystem instance2 = constructor.newInstance();
	            fail("Reflection should not allow creation of another instance.");
	        } catch (Exception e) {
	            assertTrue(e.getCause() instanceof IllegalStateException, "Expected IllegalStateException due to reflection.");
	        }
	        RentalSystem instance3 = RentalSystem.getInstance();
	        assertSame(instance, instance3, "Both instances should refer to the same object, ensuring Singleton behavior.");
		}
}

