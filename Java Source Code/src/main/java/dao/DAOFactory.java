package dao;

public class DAOFactory {
    public static CustomerDAO getCustomerDAO() {
        return new CustomerDAOImpl();
    }

    public static BookingDAO getBookingDAO() {
        return new BookingDAOImpl();
    }

    public static VehicleDAO getVehicleDAO() {
        return new VehicleDAOImpl();
    }
}
