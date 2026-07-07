package dao;

import model.Customer;
import model.DrivingLicense;

public interface CustomerDAO {
    int create(Customer customer, DrivingLicense license) throws Exception;
    void update(Customer customer, DrivingLicense license) throws Exception;
}
