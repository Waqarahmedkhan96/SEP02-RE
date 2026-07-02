package dao;

public class DAOFactory {
    public static CustomerDAO getCustomerDAO() {
        return new CustomerDAOImpl();
    }
}
