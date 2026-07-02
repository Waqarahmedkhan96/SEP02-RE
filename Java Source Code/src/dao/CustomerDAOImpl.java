package dao;

import model.Customer;
import model.DrivingLicense;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public void create(Customer customer) {
        String insertLicenseSql = "INSERT INTO driving_license " +
                "(license_no, is_category_a, is_category_b, is_category_c, is_category_d) " +
                "VALUES (?, ?, ?, ?, ?)";

        String insertCustomerSql = "INSERT INTO customer " +
                "(name, phone_no, email, cpr, passport_no, license_no) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // start transaction

            // Note: this assumes a DrivingLicense object is attached to Customer,
            // or that customer.getLicenseNo() plus category flags are passed in separately.
            // Adjust based on how your registration form actually collects this data.

            try (PreparedStatement licenseStmt = conn.prepareStatement(insertLicenseSql)) {
                licenseStmt.setString(1, customer.getLicenseNo());
                licenseStmt.setBoolean(2, false); // placeholder — see note below
                licenseStmt.setBoolean(3, false);
                licenseStmt.setBoolean(4, false);
                licenseStmt.setBoolean(5, false);
                licenseStmt.executeUpdate();
            }

            try (PreparedStatement customerStmt = conn.prepareStatement(insertCustomerSql)) {
                customerStmt.setString(1, customer.getName());
                customerStmt.setString(2, customer.getPhoneNo());
                customerStmt.setString(3, customer.getEmail());
                customerStmt.setString(4, customer.getCpr());
                customerStmt.setString(5, customer.getPassportNo());
                customerStmt.setString(6, customer.getLicenseNo());
                customerStmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // undo both inserts if anything failed
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }
}