package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerTest {

    // Z - Zero: a new customer has no driving categories selected by default.
    @Test
    void z_newCustomerShouldHaveNoLicenseCategoriesSelected() {
        Customer customer = new Customer();

        assertFalse(customer.isCategoryA());
        assertFalse(customer.isCategoryB());
        assertFalse(customer.isCategoryC());
        assertFalse(customer.isCategoryD());
    }

    // O - One: one customer stores one set of contact and license information.
    @Test
    void o_shouldCreateOneCustomerWithContactAndLicenseInformation() {
        Customer customer = new Customer(
                "Waqar Ahmed",
                "12345678",
                "waqar@example.com",
                "0101011234",
                "PA1234567",
                "DL12345"
        );

        assertEquals("Waqar Ahmed", customer.getName());
        assertEquals("12345678", customer.getPhoneNo());
        assertEquals("waqar@example.com", customer.getEmail());
        assertEquals("0101011234", customer.getCpr());
        assertEquals("PA1234567", customer.getPassportNo());
        assertEquals("DL12345", customer.getLicenseNo());
    }

    // M - Many: several driving license categories can be stored together.
    @Test
    void m_shouldStoreManyDrivingLicenseCategories() {
        Customer customer = new Customer();

        customer.setCategoryA(true);
        customer.setCategoryB(true);
        customer.setCategoryC(false);
        customer.setCategoryD(true);

        assertTrue(customer.isCategoryA());
        assertTrue(customer.isCategoryB());
        assertFalse(customer.isCategoryC());
        assertTrue(customer.isCategoryD());
    }

    // B - Boundary: CPR can be absent when passport number is available.
    @Test
    void b_shouldAllowPassportWithoutCpr() {
        Customer customer = new Customer("Tourist", "12345678", "tourist@example.com",
                "", "PA1234567", "DL12345");

        assertEquals("", customer.getCpr());
        assertEquals("PA1234567", customer.getPassportNo());
    }
}
