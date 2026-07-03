package dao;

public class DAOFactory {
    public static CustomerDAO getCustomerDAO() {
        return new CustomerDAOImpl();
    }

    public static BookingDAO getBookingDAO() {
        return new BookingDAOImpl();
    }
}
