package model;

public class DrivingLicense {
    private String licenseNo;
    private boolean isCategoryA;
    private boolean isCategoryB;
    private boolean isCategoryC;
    private boolean isCategoryD;

    public DrivingLicense() {}

    public DrivingLicense(String licenseNo, boolean isCategoryA, boolean isCategoryB,
                          boolean isCategoryC, boolean isCategoryD) {
        this.licenseNo = licenseNo;
        this.isCategoryA = isCategoryA;
        this.isCategoryB = isCategoryB;
        this.isCategoryC = isCategoryC;
        this.isCategoryD = isCategoryD;
    }

    public String getLicenseNo() { return licenseNo; }
    public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; }

    public boolean isCategoryA() { return isCategoryA; }
    public void setCategoryA(boolean categoryA) { isCategoryA = categoryA; }

    public boolean isCategoryB() { return isCategoryB; }
    public void setCategoryB(boolean categoryB) { isCategoryB = categoryB; }

    public boolean isCategoryC() { return isCategoryC; }
    public void setCategoryC(boolean categoryC) { isCategoryC = categoryC; }

    public boolean isCategoryD() { return isCategoryD; }
    public void setCategoryD(boolean categoryD) { isCategoryD = categoryD; }
}