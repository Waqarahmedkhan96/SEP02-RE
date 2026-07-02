package dao;

import model.Customer;
import model.DrivingLicense;
import utils.DatabaseConnection;

import java.sql.*;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public int create(Customer customer, DrivingLicense license) throws SQLException {
        String insertLicenseSql = "INSERT INTO driving_license " +
                "(license_no, is_category_a, is_category_b, is_category_c, is_category_d) " +
                "VALUES (?, ?, ?, ?, ?)";

        String insertCustomerSql = "INSERT INTO customer " +
                "(name, phone_no, email, cpr, passport_no, license_no) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING customer_id";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement licenseStmt = conn.prepareStatement(insertLicenseSql)) {
                licenseStmt.setString(1, license.getLicenseNo());
                licenseStmt.setBoolean(2, license.isCategoryA());
                licenseStmt.setBoolean(3, license.isCategoryB());
                licenseStmt.setBoolean(4, license.isCategoryC());
                licenseStmt.setBoolean(5, license.isCategoryD());
                licenseStmt.executeUpdate();
            }

            int generatedId;
            try (PreparedStatement customerStmt = conn.prepareStatement(insertCustomerSql)) {
                customerStmt.setString(1, customer.getName());
                customerStmt.setString(2, customer.getPhoneNo());
                customerStmt.setString(3, customer.getEmail());
                customerStmt.setString(4, customer.getCpr());
                customerStmt.setString(5, customer.getPassportNo());
                customerStmt.setString(6, license.getLicenseNo());

                ResultSet rs = customerStmt.executeQuery();
                rs.next();
                generatedId = rs.getInt("customer_id");
            }

            conn.commit();
            return generatedId;

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}