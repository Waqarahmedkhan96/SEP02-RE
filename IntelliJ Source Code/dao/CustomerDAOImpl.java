package dao;

import model.Customer;
import model.DrivingLicense;
import application.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public boolean update(Customer customer, DrivingLicense license) throws SQLException {
        String upsertLicenseSql = "INSERT INTO driving_license " +
                "(license_no, is_category_a, is_category_b, is_category_c, is_category_d) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (license_no) DO UPDATE SET " +
                "is_category_a = EXCLUDED.is_category_a, " +
                "is_category_b = EXCLUDED.is_category_b, " +
                "is_category_c = EXCLUDED.is_category_c, " +
                "is_category_d = EXCLUDED.is_category_d";

        String updateCustomerSql = "UPDATE customer SET " +
                "name = ?, phone_no = ?, email = ?, cpr = ?, passport_no = ?, license_no = ? " +
                "WHERE customer_id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement licenseStmt = conn.prepareStatement(upsertLicenseSql)) {
                licenseStmt.setString(1, license.getLicenseNo());
                licenseStmt.setBoolean(2, license.isCategoryA());
                licenseStmt.setBoolean(3, license.isCategoryB());
                licenseStmt.setBoolean(4, license.isCategoryC());
                licenseStmt.setBoolean(5, license.isCategoryD());
                licenseStmt.executeUpdate();
            }

            int rowsUpdated;
            try (PreparedStatement customerStmt = conn.prepareStatement(updateCustomerSql)) {
                customerStmt.setString(1, customer.getName());
                customerStmt.setString(2, customer.getPhoneNo());
                customerStmt.setString(3, customer.getEmail());
                customerStmt.setString(4, customer.getCpr());
                customerStmt.setString(5, customer.getPassportNo());
                customerStmt.setString(6, license.getLicenseNo());
                customerStmt.setInt(7, customer.getCustomerId());
                rowsUpdated = customerStmt.executeUpdate();
            }

            conn.commit();
            return rowsUpdated > 0;
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

    @Override
    public List<Customer> getAllCustomers() throws SQLException {
        String sql = "SELECT c.customer_id, c.name, c.phone_no, c.email, c.cpr, c.passport_no, c.license_no, " +
                "dl.is_category_a, dl.is_category_b, dl.is_category_c, dl.is_category_d " +
                "FROM customer c " +
                "LEFT JOIN driving_license dl ON dl.license_no = c.license_no " +
                "ORDER BY c.customer_id";

        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setPhoneNo(rs.getString("phone_no"));
                customer.setEmail(rs.getString("email"));
                customer.setCpr(rs.getString("cpr"));
                customer.setPassportNo(rs.getString("passport_no"));
                customer.setLicenseNo(rs.getString("license_no"));
                customer.setCategoryA(rs.getBoolean("is_category_a"));
                customer.setCategoryB(rs.getBoolean("is_category_b"));
                customer.setCategoryC(rs.getBoolean("is_category_c"));
                customer.setCategoryD(rs.getBoolean("is_category_d"));
                customers.add(customer);
            }
        }
        return customers;
    }
}
