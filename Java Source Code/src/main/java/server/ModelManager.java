package server;

import dao.CustomerDAO;
import dao.DAOFactory;
import model.Customer;
import model.DrivingLicense;
import shared.CreateCustomerRequest;
import shared.CreateCustomerResponse;

public class ModelManager {

    public CreateCustomerResponse createCustomer(CreateCustomerRequest req) {
        // basic validation — expand as your team defines actual rules
        if (req.getName() == null || req.getName().isBlank()) {
            return new CreateCustomerResponse(false, "Name is required", -1);
        }
        if (req.getLicenseNo() == null || req.getLicenseNo().isBlank()) {
            return new CreateCustomerResponse(false, "License number is required", -1);
        }

        Customer customer = new Customer(req.getName(), req.getPhoneNo(), req.getEmail(),
                req.getCpr(), req.getPassportNo(), req.getLicenseNo());

        DrivingLicense license = new DrivingLicense(req.getLicenseNo(),
                req.isCategoryA(), req.isCategoryB(), req.isCategoryC(), req.isCategoryD());

        try {
            CustomerDAO dao = DAOFactory.getCustomerDAO();
            int newId = dao.create(customer, license);
            return new CreateCustomerResponse(true, "Customer created", newId);
        } catch (Exception e) {
            e.printStackTrace();
            return new CreateCustomerResponse(false, "Database error: " + e.getMessage(), -1);
        }
    }
}