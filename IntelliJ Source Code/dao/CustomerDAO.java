package dao;

import model.Customer;
import model.DrivingLicense;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO {
    int create(Customer customer, DrivingLicense license) throws Exception;
    boolean update(Customer customer, DrivingLicense license) throws Exception;
    List<Customer> getAllCustomers() throws SQLException;
}
