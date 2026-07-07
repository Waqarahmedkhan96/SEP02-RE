package mockdata;

import model.Customer;

import java.util.List;

public class MockCustomers {

    public static List<Customer> getCustomers() {
        return List.of(
                customer(1, "Sara Ahmed", "22114455", "sara@example.com", "010190-1111", "P10001", "DL10001"),
                customer(2, "Mads Jensen", "22334455", "mads@example.com", "020290-2222", "P10002", "DL10002"),
                customer(3, "Lina Ali", "22445566", "lina@example.com", "030390-3333", "P10003", "DL10003"),
                customer(4, "Noah Petersen", "22556677", "noah@example.com", "040490-4444", "P10004", "DL10004"),
                customer(5, "Emma Hansen", "22667788", "emma@example.com", "050590-5555", "P10005", "DL10005"),
                customer(6, "Ali Khan", "22778899", "ali@example.com", "060690-6666", "P10006", "DL10006"),
                customer(7, "Sofia Larsen", "22889900", "sofia@example.com", "070790-7777", "P10007", "DL10007"),
                customer(8, "Jonas Holm", "22990011", "jonas@example.com", "080890-8888", "P10008", "DL10008"),
                customer(9, "Aisha Noor", "22001122", "aisha@example.com", "090990-9999", "P10009", "DL10009"),
                customer(10, "Victor Berg", "22112233", "victor@example.com", "101090-1010", "P10010", "DL10010")
        );
    }

    private static Customer customer(int id, String name, String phone, String email,
                                     String cpr, String passportNo, String licenseNo) {
        Customer customer = new Customer(name, phone, email, cpr, passportNo, licenseNo);
        customer.setCustomerId(id);
        return customer;
    }
}
