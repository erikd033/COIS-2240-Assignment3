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
	
}

