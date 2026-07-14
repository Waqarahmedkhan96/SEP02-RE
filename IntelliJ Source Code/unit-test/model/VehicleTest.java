package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VehicleTest {
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
        vehicle.setVehicleId(10);
        vehicle.setModel("Corolla");
        vehicle.setVehicleType("Car");
        vehicle.setColor("Black");
        vehicle.setEngine("Hybrid");
        vehicle.setPlateNo("AB12345");
        vehicle.setPriceHour(100.0);
        vehicle.setDeposit(500.0);
        vehicle.setNoOfTire(4);
        vehicle.setLateFee(25.0);
        vehicle.setRequiredLicense("B");
        vehicle.setCondition("Good");
        vehicle.setNoOfSeats(5);
        vehicle.setCurrentState("available");
    }

    // Z - Zero: blank state is treated as available by the state factory.
    @Test
    void z_vehicleWithoutExplicitStateShouldBeAvailable() {
        Vehicle newVehicle = new Vehicle();

        assertTrue(newVehicle.isAvailableForBooking());
    }

    // O - One: one vehicle stores its identity details.
    @Test
    void o_shouldStoreOneVehicleIdentity() {
        assertEquals(10, vehicle.getVehicleId());
        assertEquals("Corolla", vehicle.getModel());
        assertEquals("Car", vehicle.getVehicleType());
        assertEquals("AB12345", vehicle.getPlateNo());
    }

    // O - One: one vehicle stores its rental price details.
    @Test
    void o_shouldStoreOneVehiclePriceDetails() {
        assertEquals(100.0, vehicle.getPriceHour());
        assertEquals(500.0, vehicle.getDeposit());
        assertEquals(25.0, vehicle.getLateFee());
    }

    // S - Simple scenario: available vehicle becomes rented.
    @Test
    void s_availableVehicleCanBeMarkedRented() {
        vehicle.markRented();

        assertEquals("rented", vehicle.getCurrentState());
    }

    // S - Simple scenario: rented vehicle can be released.
    @Test
    void s_rentedVehicleCanBeReleasedBackToAvailable() {
        vehicle.setCurrentState("rented");

        vehicle.markAvailable();

        assertEquals("available", vehicle.getCurrentState());
    }

    // M - Many / more complex: vehicle stores physical details used for filtering and display.
    @Test
    void m_shouldStoreVehiclePhysicalDetails() {
        assertEquals("Black", vehicle.getColor());
        assertEquals("Hybrid", vehicle.getEngine());
        assertEquals(4, vehicle.getNoOfTire());
        assertEquals("B", vehicle.getRequiredLicense());
        assertEquals("Good", vehicle.getCondition());
        assertEquals(5, vehicle.getNoOfSeats());
    }

    // B - Boundary: maintenance is a valid state, but it blocks booking.
    @Test
    void b_maintenanceVehicleCannotBeBooked() {
        vehicle.setCurrentState("maintenance");

        assertFalse(vehicle.isAvailableForBooking());
    }

    // E - Exceptional behavior: renting a maintenance vehicle fails in a defined way.
    @Test
    void e_maintenanceVehicleThrowsWhenMarkedRented() {
        vehicle.setCurrentState("maintenance");

        assertThrows(IllegalStateException.class, vehicle::markRented);
    }
}
