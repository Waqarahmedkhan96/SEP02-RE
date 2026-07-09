package shared;

import java.io.Serializable;

public class CreateCustomerRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String phoneNo;
    private String email;
    private String cpr;
    private String passportNo;
    private String licenseNo;
    private boolean categoryA, categoryB, categoryC, categoryD;

    public CreateCustomerRequest(String name, String phoneNo, String email, String cpr,
                                 String passportNo, String licenseNo,
                                 boolean categoryA, boolean categoryB,
                                 boolean categoryC, boolean categoryD) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.cpr = cpr;
        this.passportNo = passportNo;
        this.licenseNo = licenseNo;
        this.categoryA = categoryA;
        this.categoryB = categoryB;
        this.categoryC = categoryC;
        this.categoryD = categoryD;
    }

    public String getName() { return name; }
    public String getPhoneNo() { return phoneNo; }
    public String getEmail() { return email; }
    public String getCpr() { return cpr; }
    public String getPassportNo() { return passportNo; }
    public String getLicenseNo() { return licenseNo; }
    public boolean isCategoryA() { return categoryA; }
    public boolean isCategoryB() { return categoryB; }
    public boolean isCategoryC() { return categoryC; }
    public boolean isCategoryD() { return categoryD; }
}