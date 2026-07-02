package model;

public class Customer {
    private int customerId;
    private String name;
    private String phoneNo;
    private String email;
    private String cpr;
    private String passportNo;
    private String licenseNo; // FK -> DrivingLicense

    public Customer() {}

    public Customer(String name, String phoneNo, String email,
                    String cpr, String passportNo, String licenseNo) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.cpr = cpr;
        this.passportNo = passportNo;
        this.licenseNo = licenseNo;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpr() { return cpr; }
    public void setCpr(String cpr) { this.cpr = cpr; }

    public String getPassportNo() { return passportNo; }
    public void setPassportNo(String passportNo) { this.passportNo = passportNo; }

    public String getLicenseNo() { return licenseNo; }
    public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; }
}