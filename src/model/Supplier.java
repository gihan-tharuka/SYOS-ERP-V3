package model;

public class Supplier extends User {
    private String companyName;
    private String contactPerson;
    private String mobile;

    public Supplier(String username, String password, String companyName, String contactPerson, String email, String mobile) {
        super(username, password, email);
        this.companyName = companyName;
        this.contactPerson = contactPerson;
        this.mobile = mobile;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getMobile() {
        return mobile;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}


